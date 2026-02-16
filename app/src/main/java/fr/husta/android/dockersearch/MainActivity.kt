package fr.husta.android.dockersearch

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.ExpandableListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.husta.android.dockersearch.databinding.ActivityMainBinding
import fr.husta.android.dockersearch.databinding.DialogAboutBinding
import fr.husta.android.dockersearch.docker.DockerRegistryClient
import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult
import fr.husta.android.dockersearch.docker.model.ImageSearchResult
import fr.husta.android.dockersearch.docker.model.comparator.DefaultImageSearchComparator
import fr.husta.android.dockersearch.listadapter.DockerImageExpandableListAdapter
import fr.husta.android.dockersearch.listadapter.SuggestionAdapter
import fr.husta.android.dockersearch.search.CustomSearchRecentSuggestions
import fr.husta.android.dockersearch.search.RecentSearchProvider
import fr.husta.android.dockersearch.utils.AppInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Objects

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var listView: ExpandableListView? = null

    private var dockerImageExpandableListAdapter: DockerImageExpandableListAdapter? = null

    private lateinit var preferences: SharedPreferences

    private var selectedTheme = 0
    private var selectedDialogOption = 0

    private var lastSearchQuery: String? = null

    private var suggestionAdapter: SuggestionAdapter? = null

    private val disposables = CompositeDisposable()
    private val dockerRegistryClient = DockerRegistryClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        // read preferences at start
        preferences = getPreferences(MODE_PRIVATE)
        selectedTheme = preferences.getInt(KEY_PREF_SAVED_DARK_MODE, 2)
        selectedDialogOption = selectedTheme
        // ensure correct theme is applied
        applyTheme(selectedTheme)

        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate : " + this.getLocalClassName())
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        //        setSupportActionBar(binding.searchBar);
        APP_PACKAGE_NAME = getApplicationContext().getPackageName()

        if (getIntent() != null) {
            handleIntent(getIntent())
        }

        checkInternetConnection()

        listView = binding.listView
        ViewCompat.setNestedScrollingEnabled(listView!!, true)
        if (savedInstanceState == null) {
            dockerImageExpandableListAdapter = DockerImageExpandableListAdapter(
                this@MainActivity,
                ArrayList<ImageSearchResult?>()
            )
        } else {
            Log.d(TAG, "onCreate: state to be restored ?")
            val savedArrayList = savedInstanceState.getParcelableArrayList<ImageSearchResult?>(
                KEY_IMAGE_LIST_ADAPTER
            )
            dockerImageExpandableListAdapter =
                DockerImageExpandableListAdapter(this@MainActivity, savedArrayList)
        }
        listView!!.setAdapter(dockerImageExpandableListAdapter)

        // listView.setOnGroupClickListener( );
        // listView.setOnChildClickListener( );
        listView!!.setOnGroupExpandListener { groupPosition: Int ->
            val groupCount = listView!!.expandableListAdapter.groupCount
            for (i in 0..<groupCount) {
                if (groupPosition != i && listView!!.isGroupExpanded(i)) {
                    listView!!.collapseGroup(i)
                }
            }
        }

        binding.searchBar.inflateMenu(R.menu.options_menu)
        binding.searchBar.setOnMenuItemClickListener { item: MenuItem? ->
            this.onOptionsItemSelected(
                item!!
            )
        }

        val searchRecentSuggestions = CustomSearchRecentSuggestions(this)

        suggestionAdapter = SuggestionAdapter(
            searchRecentSuggestions.getRecentSearches(),
            { text: String? ->
                binding.searchBar.setText(text)
                binding.searchView.hide()
                onQueryTextSubmitCustom(
                    text,
                    {
                        suggestionAdapter!!.removeItem(text)
                        suggestionAdapter!!.addItem(text)
                        binding.progressIndicator.show()
                    },
                    { binding.progressIndicator.hide() })
            },
            { text: String? ->
                val message = getString(R.string.text_delete_search_from_history, text)
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.msg_delete_search_from_history)
                    .setMessage(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog: DialogInterface?, which: Int ->
                        searchRecentSuggestions.removeSuggestion(text)
                        suggestionAdapter!!.removeItem(text)
                    }
                    .setNegativeButton(
                        android.R.string.cancel
                    ) { dialog: DialogInterface?, which: Int -> dialog!!.dismiss() }
                    .show()
            })

        binding.suggestionsList.setLayoutManager(LinearLayoutManager(this))
        binding.suggestionsList.setAdapter(suggestionAdapter)

        binding.searchView
            .getEditText() // When user presses enter
            .setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
                binding.searchBar.setText(binding.searchView.getText())
                binding.searchView.hide()

                onQueryTextSubmitCustom(
                    v!!.getText().toString(),
                    {
                        suggestionAdapter!!.removeItem(v.getText().toString())
                        suggestionAdapter!!.addItem(v.getText().toString())
                        binding.progressIndicator.show()
                    },
                    { binding.progressIndicator.hide() })
                false
            }

        // Pour le filtrage en temps réel des suggestions
        binding.searchView
            .getEditText()
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    Log.d(TAG, "onTextChanged: " + s.toString())
                    // Appelle la méthode de filtrage de l'adapter avec le nouveau texte
                    suggestionAdapter!!.filter(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

        val swipeRefreshLayout = binding.swipeRefreshImages
        swipeRefreshLayout.setColorSchemeResources(
            R.color.md_theme_primary,
            R.color.md_theme_primary
        )
        swipeRefreshLayout.setOnRefreshListener { this.onSwipeRefresh() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.getAction()) {
            // SearchManager.QUERY is the key that a SearchManager will use to send a query string
            // to an Activity.
            val query = intent.getStringExtra(SearchManager.QUERY)

            // save into suggestions
//            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
//                    RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE);
//            suggestions.saveRecentQuery(query, null);

            // si clic sur suggestion, zone saisie = suggestion
            // searchView.setQuery(query, false);
            onQueryTextSubmitCustom(
                query,
                { binding.progressIndicator.show() },
                { binding.progressIndicator.hide() })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState (1): " + this.getLocalClassName())
        Log.d(TAG, "onSaveInstanceState: listView => " + listView!!.getAdapter().getCount())
        outState.putParcelableArrayList(
            KEY_IMAGE_LIST_ADAPTER,
            dockerImageExpandableListAdapter!!.getGroupList()
        )
        if (lastSearchQuery != null) {
            outState.putString(KEY_LAST_SEARCH, lastSearchQuery)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState : " + this.getLocalClassName())
        lastSearchQuery = savedInstanceState.getString(KEY_LAST_SEARCH, "")
    }

    override fun onResume() {
        // read preferences at start
        preferences = getPreferences(MODE_PRIVATE)
        selectedTheme = preferences.getInt(KEY_PREF_SAVED_DARK_MODE, 2)
        // ensure correct theme is applied
        applyTheme(selectedTheme)

        super.onResume()
        Log.d(TAG, "onResume : " + this.getLocalClassName())
    }

    override fun onNightModeChanged(mode: Int) {
        when (mode) {
            AppCompatDelegate.MODE_NIGHT_NO -> Log.d(
                TAG,
                String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_NO")
            )

            AppCompatDelegate.MODE_NIGHT_YES -> Log.d(
                TAG,
                String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_YES")
            )

            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> Log.d(
                TAG,
                String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_AUTO_BATTERY")
            )

            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> Log.d(
                TAG,
                String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_FOLLOW_SYSTEM")
            )

            else -> Log.d(TAG, String.format("onNightModeChanged : mode = %d", mode))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater()
        inflater.inflate(R.menu.options_menu, menu)

        //        MenuItem searchItem = menu.findItem(R.id.menu_search);
//        searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                return onQueryTextSubmitCustom(query,
//                        () -> binding.progressIndicator.show(),
//                        () -> binding.progressIndicator.hide());
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                return false;
//            }
//        });
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.menu_clear_search_history) {
            clickClearSearchHistory(item)
            return true
        } else if (id == R.id.menu_choose_theme) {
            clickChooseTheme(item)
            return true
        } else if (id == R.id.menu_submit_issue) {
            clickSubmitIssue(item)
            return true
        } else if (id == R.id.menu_contribute) {
            clickContribute(item)
            return true
        } else if (id == R.id.menu_check_latest_release) {
            clickCheckLatestRelease(item)
            return true
        } else if (id == R.id.menu_note_app) {
            clickNoteApp(item)
            return true
        } else if (id == R.id.menu_about) {
            clickAbout(item)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onQueryTextSubmitCustom(
        query: String?,
        onStart: Runnable,
        onEnd: Runnable
    ): Boolean {
        Log.d(TAG, "onQueryTextSubmit : " + query)
        this.lastSearchQuery = query

        // save query
        val suggestions = SearchRecentSuggestions(
            this,
            RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE
        )
        suggestions.saveRecentQuery(query, null)

        // User pressed the search button
        val disposable = dockerRegistryClient.searchImagesAsync(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(Consumer { disp1: Disposable? -> onStart.run() })
            .doOnTerminate { onEnd.run() }
            .subscribe(
                Consumer { data: ContainerImageSearchResult? ->
                    Log.d(
                        TAG,
                        "searchImagesAsync.onResponse: returned " + data!!.getResults().size + " out of " + data.getNumResults()
                    )
                    data.getResults().sortWith(DefaultImageSearchComparator.defaultComparator())

                    dockerImageExpandableListAdapter!!.notifyDataSetInvalidated() // necessaire ?
                    // Collapse all
                    for (i in 0..<dockerImageExpandableListAdapter!!.getGroupCount()) {
                        listView!!.collapseGroup(i)
                    }

                    // dockerImageExpandableListAdapter.setNotifyOnChange(false);
                    dockerImageExpandableListAdapter!!.getGroupList().clear()
                    dockerImageExpandableListAdapter!!.getGroupList().addAll(data.getResults())
                    dockerImageExpandableListAdapter!!.notifyDataSetChanged()
                },
                Consumer { throwable: Throwable? ->
                    Log.e(TAG, throwable!!.message, throwable)
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.msg_error, throwable.message),
                        Toast.LENGTH_LONG
                    ).show()
                })
        disposables.add(disposable)

        // fermer le clavier de saisie
        binding.searchView.clearFocus()

        return true
    }

    fun onSwipeRefresh() {
        Log.d(TAG, "Swipe : refresh requested")
        val swipeRefreshLayout = binding.swipeRefreshImages
        binding.searchView.getEditText()
            .setText(if (lastSearchQuery == null) "" else lastSearchQuery)
        onQueryTextSubmitCustom(
            binding.searchView.getEditText().getText().toString(),
            {},
            { swipeRefreshLayout.setRefreshing(false) })
    }

    fun startActivityTagList(context: Context?, data: ImageSearchResult) {
        val starter = Intent(context, TagListActivity::class.java)
        starter.putExtra(TagListActivity.DATA_IMG_NAME, data.getName())
        startActivity(starter)
    }

    fun checkInternetConnection() {
        if (!this.isDeviceOnline) {
            // display error
            Toast.makeText(this, R.string.msg_no_network_connection, Toast.LENGTH_SHORT).show()
        }
    }

    fun clickClearSearchHistory(item: MenuItem?) {
        val suggestions = SearchRecentSuggestions(
            this,
            RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE
        )
        suggestions.clearHistory()
        suggestionAdapter!!.clear()

        Toast.makeText(this, R.string.msg_cleared_search_history, Toast.LENGTH_SHORT).show()
    }

    fun clickChooseTheme(item: MenuItem?) {
        createThemeChooserAlertDialog().show()
    }

    private fun createThemeChooserAlertDialog(): AlertDialog {
        return MaterialAlertDialogBuilder(this)
            .setTitle(R.string.choose_theme) // list : 'Light', 'Dark', 'Set by Battery Saver / System'
            // See RECO : https://developer.android.com/guide/topics/ui/look-and-feel/darktheme#changing_themes_in-app
            .setSingleChoiceItems(
                R.array.themes_list, selectedTheme
            ) { dialog: DialogInterface?, which: Int ->
                selectedTheme = which
                preferences.edit {
                    putInt(KEY_PREF_SAVED_DARK_MODE, selectedTheme)
                }
                applyTheme(selectedTheme) // forces dismiss dialog
            }
            .setPositiveButton(
                android.R.string.ok
            ) { dialog: DialogInterface?, which: Int -> dialog!!.dismiss() }
            .create()
    }

    private fun applyTheme(selectedTheme: Int) {
        // See https://developer.android.com/guide/topics/ui/look-and-feel/darktheme#changing_themes_in-app
        when (selectedTheme) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            } else  // API 29+
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    fun clickAbout(item: MenuItem?) {
        val binding = DialogAboutBinding.inflate(getLayoutInflater())
        val applicationVersion = AppInfo.getApplicationVersion(this)

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        val textView = binding.aboutCredits
        val defaultColor = textView.getTextColors().getDefaultColor()
        textView.setTextColor(defaultColor)

        val textViewVersion = binding.aboutVersion
        textViewVersion.setText(
            String.format(
                getString(R.string.msg_about_version),
                applicationVersion
            )
        )

        val applicationInfo = getApplicationInfo()
        val packageManager = getPackageManager()
        val icon = packageManager.getApplicationIcon(applicationInfo)
        MaterialAlertDialogBuilder(this)
            .setIcon(icon)
            .setTitle(R.string.app_name)
            .setView(binding.getRoot())
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }

    fun clickSubmitIssue(item: MenuItem?) {
        openUrlInBrowser(("$PROJECT_GITHUB_URL/issues").toUri())
    }

    fun clickContribute(item: MenuItem?) {
        openUrlInBrowser(PROJECT_GITHUB_URL.toUri())
    }

    fun clickCheckLatestRelease(item: MenuItem?) {
        openUrlInBrowser(("$PROJECT_GITHUB_URL/releases/latest").toUri())
    }

    fun clickNoteApp(item: MenuItem?) {
        // https://developer.android.com/distribute/tools/promote/linking.html#android-app
        // Ex : details?id=com.google.android.apps.maps

        val appPackageName: String? = APP_PACKAGE_NAME

        // openUrlInBrowser(Uri.parse("https://play.google.com/store/apps/details?id="+ appPackageName));
        // openUrlInBrowser(Uri.parse("market://details?id=" + appPackageName));
        openInMarket(("market://details?id=$appPackageName").toUri())
    }

    fun openUrlInBrowser(uri: Uri?) {
        Objects.requireNonNull<Uri?>(uri)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        startActivity(intent)
    }

    /**
     * URI may start with "https://play.google.com/store/apps/details?id=" or "market://details?id=".
     * 
     * @param uri
     */
    fun openInMarket(uri: Uri?) {
        Objects.requireNonNull<Uri?>(uri)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity( //Intent.createChooser(
                intent
            )
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, R.string.msg_err_playstore_not_installed, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val isDeviceOnline: Boolean
        /**
         * Checks whether the device currently has a network connection.
         * 
         * @return true if the device has a network connection, false otherwise.
         */
        get() {
            val connMgr =
                getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.getActiveNetworkInfo()
            return (networkInfo != null && networkInfo.isConnected())
        }

    //    /**
    //     * Check that Google Play services APK is installed and up to date.
    //     * => https://developers.google.com/android/reference/com/google/android/gms/common/GoogleApiAvailability
    //     *
    //     * @return true if Google Play Services is available and up to
    //     * date on this device; false otherwise.
    //     */
    //    private boolean isGooglePlayServicesAvailable()
    //    {
    //        GoogleApiAvailability apiAvailability =
    //                GoogleApiAvailability.getInstance();
    //        final int connectionStatusCode =
    //                apiAvailability.isGooglePlayServicesAvailable(this);
    //        return connectionStatusCode == ConnectionResult.SUCCESS;
    //    }
    override fun onDestroy() {
        super.onDestroy()
        this.disposables.clear()
    }

    companion object {
        const val PROJECT_GITHUB_URL: String = "https://github.com/ghusta/android-docker-search"

        const val DOCKER_HUB_BASE_URL: String = "https://hub.docker.com"

        private const val TAG = "MAIN"
        const val KEY_IMAGE_LIST_ADAPTER: String = "KEY_IMAGE_LIST_ADAPTER"
        const val KEY_LAST_SEARCH: String = "KEY_LAST_SEARCH"

        const val KEY_PREF_SAVED_DARK_MODE: String = "last_dark_mode"

        /**
         * Fetched in AndroidManifest.xml
         */
        var APP_PACKAGE_NAME: String? = null
    }
}
