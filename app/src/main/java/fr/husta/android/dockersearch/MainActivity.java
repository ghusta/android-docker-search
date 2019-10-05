package fr.husta.android.dockersearch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import fr.husta.android.dockersearch.docker.DockerRegistryClient;
import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ImageSearchResult;
import fr.husta.android.dockersearch.docker.model.comparator.DefaultImageSearchComparator;
import fr.husta.android.dockersearch.listadapter.DockerImageExpandableListAdapter;
import fr.husta.android.dockersearch.search.RecentSearchProvider;
import fr.husta.android.dockersearch.utils.AppInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener
{

    public static final String PROJECT_GITHUB_URL = "https://github.com/ghusta/android-docker-search";

    public static final String DOCKER_HUB_BASE_URL = "https://hub.docker.com";

    private static final String TAG = "MAIN";
    public static final String KEY_IMAGE_LIST_ADAPTER = "KEY_IMAGE_LIST_ADAPTER";

    public static final String KEY_PREF_SAVED_DARK_MODE = "last_dark_mode";

    /**
     * Fetched in AndroidManifest.xml
     */
    public static String APP_PACKAGE_NAME;

    private SearchView searchView;

    private ExpandableListView listView;

    private ProgressDialog progressBar;

    private DockerImageExpandableListAdapter dockerImageExpandableListAdapter;

    private SharedPreferences preferences;

    private AlertDialog themeChooserDialog;

    private int selectedTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // read preferences at start
        preferences = getPreferences(MODE_PRIVATE);
        selectedTheme = preferences.getInt(KEY_PREF_SAVED_DARK_MODE, 2);
        // ensure correct theme is applied
        applyTheme(selectedTheme);

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate : " + this.getLocalClassName());
        setContentView(R.layout.activity_main);

        APP_PACKAGE_NAME = getApplicationContext().getPackageName();

        themeChooserDialog = initThemeChooserAlertDialog();

        if (getIntent() != null)
        {
            handleIntent(getIntent());
        }

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage(getString(R.string.msg_searching));

        checkInternetConnection();

        listView = findViewById(R.id.listView);
        if (savedInstanceState == null)
        {
            dockerImageExpandableListAdapter = new DockerImageExpandableListAdapter(MainActivity.this,
                    new ArrayList<>());
        }
        else
        {
            Log.d(TAG, "onCreate: state to be restored ?");
            ArrayList<ImageSearchResult> savedArrayList = savedInstanceState.getParcelableArrayList(KEY_IMAGE_LIST_ADAPTER);
            dockerImageExpandableListAdapter = new DockerImageExpandableListAdapter(MainActivity.this, savedArrayList);
        }
        listView.setAdapter(dockerImageExpandableListAdapter);

        // listView.setOnGroupClickListener( );
        // listView.setOnChildClickListener( );

        listView.setOnGroupExpandListener(groupPosition ->
        {
            int groupCount = listView.getExpandableListAdapter().getGroupCount();

            for (int i = 0; i < groupCount; i++)
            {
                if (groupPosition != i && listView.isGroupExpanded(i))
                {
                    listView.collapseGroup(i);
                }
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_images);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorSecondary);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent)
    {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            // SearchManager.QUERY is the key that a SearchManager will use to send a query string
            // to an Activity.
            String query = intent.getStringExtra(SearchManager.QUERY);

            // save into suggestions
//            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
//                    RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE);
//            suggestions.saveRecentQuery(query, null);

            // si clic sur suggestion, zone saisie = suggestion
            searchView.setQuery(query, false);

            onQueryTextSubmit(query);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState (1): " + this.getLocalClassName());
        Log.d(TAG, "onSaveInstanceState: listView => " + listView.getAdapter().getCount());
        outState.putParcelableArrayList(KEY_IMAGE_LIST_ADAPTER, dockerImageExpandableListAdapter.getGroupList());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState : " + this.getLocalClassName());

    }

    @Override
    protected void onResume()
    {
        // read preferences at start
        preferences = getPreferences(MODE_PRIVATE);
        selectedTheme = preferences.getInt(KEY_PREF_SAVED_DARK_MODE, 2);
        // ensure correct theme is applied
        applyTheme(selectedTheme);

        super.onResume();
        Log.d(TAG, "onResume : " + this.getLocalClassName());
    }

    @Override
    protected void onNightModeChanged(int mode)
    {
        switch (mode)
        {
            case MODE_NIGHT_NO:
                Log.d(TAG, String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_NO"));
                break;
            case MODE_NIGHT_YES:
                Log.d(TAG, String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_YES"));
                break;
            case MODE_NIGHT_AUTO_BATTERY:
                Log.d(TAG, String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_AUTO_BATTERY"));
                break;
            case MODE_NIGHT_FOLLOW_SYSTEM:
                Log.d(TAG, String.format("onNightModeChanged : mode = %s", "MODE_NIGHT_FOLLOW_SYSTEM"));
                break;
            default:
                Log.d(TAG, String.format("onNightModeChanged : mode = %d", mode));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        Log.d(TAG, "onQueryTextSubmit : " + query);

        progressBar.show();

        // save query
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE);
        suggestions.saveRecentQuery(query, null);

        // User pressed the search button
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        dockerRegistryClient.searchImagesAsync(query, new Callback<ContainerImageSearchResult>()
        {
            @Override
            public void onResponse(Call<ContainerImageSearchResult> call, Response<ContainerImageSearchResult> response)
            {
                ContainerImageSearchResult body = response.body();
                Log.d(TAG, "searchImagesAsync.onResponse: returned " + body.getResults().size() + " out of " + body.getNumResults());
                Collections.sort(body.getResults(), new DefaultImageSearchComparator());

                dockerImageExpandableListAdapter.notifyDataSetInvalidated(); // necessaire ?
                // Collapse all
                for (int i = 0; i < dockerImageExpandableListAdapter.getGroupCount(); i++)
                {
                    listView.collapseGroup(i);
                }

                // dockerImageExpandableListAdapter.setNotifyOnChange(false);
                dockerImageExpandableListAdapter.getGroupList().clear();
                dockerImageExpandableListAdapter.getGroupList().addAll(body.getResults());
                dockerImageExpandableListAdapter.notifyDataSetChanged();

                progressBar.hide();
            }

            @Override
            public void onFailure(Call<ContainerImageSearchResult> call, Throwable t)
            {
                Log.e(TAG, t.getMessage(), t);

                progressBar.hide();
                Toast.makeText(MainActivity.this, getString(R.string.msg_error, t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });

        // fermer le clavier de saisie
        searchView.clearFocus();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        // User changed the text
        return false;
    }

    @Override
    public void onRefresh()
    {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_images);
        swipeRefreshLayout.setRefreshing(false);
        CharSequence query = searchView.getQuery();
        searchView.setQuery(query, true);
    }

    public void startActivityTagList(Context context, ImageSearchResult data)
    {
        Intent starter = new Intent(context, TagListActivity.class);
        starter.putExtra(TagListActivity.DATA_IMG_NAME, data.getName());
        startActivity(starter);
    }

    public void checkInternetConnection()
    {
        if (!isDeviceOnline())
        {
            // display error
            Toast.makeText(this, R.string.msg_no_network_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickClearSearchHistory(MenuItem item)
    {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE);
        suggestions.clearHistory();

        Toast.makeText(this, R.string.msg_cleared_search_history, Toast.LENGTH_SHORT).show();
    }

    public void clickChooseTheme(MenuItem item)
    {
        themeChooserDialog.show();
    }

    private AlertDialog initThemeChooserAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_theme)
                // list : 'Light', 'Dark', 'Set by Battery Saver / System'
                // See RECO : https://developer.android.com/guide/topics/ui/look-and-feel/darktheme#changing_themes_in-app
                .setSingleChoiceItems(R.array.themes_list, selectedTheme, (dialog, which) -> {
                    selectedTheme = which;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(KEY_PREF_SAVED_DARK_MODE, selectedTheme);
                    editor.apply();

                    applyTheme(selectedTheme);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });

        return builder.create();
    }

    private void applyTheme(int selectedTheme)
    {
        // See https://developer.android.com/guide/topics/ui/look-and-feel/darktheme#changing_themes_in-app
        switch (selectedTheme)
        {
            case 0:
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                else // API 29+
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                break;
        }
    }

    public void clickAbout(MenuItem item)
    {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.dialog_about, null, false);

        String applicationVersion = AppInfo.getApplicationVersion(this);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        TextView textViewVersion = messageView.findViewById(R.id.about_version);
        textViewVersion.setText(String.format(
                getString(R.string.msg_about_version),
                applicationVersion
        ));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ApplicationInfo applicationInfo = getApplicationInfo();
        PackageManager packageManager = getPackageManager();
        Drawable icon = packageManager.getApplicationIcon(applicationInfo);
        builder.setIcon(icon);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create();
        builder.show();
    }

    public void clickSubmitIssue(MenuItem item)
    {
        openUrlInBrowser(Uri.parse(PROJECT_GITHUB_URL + "/issues"));
    }

    public void clickContribute(MenuItem item)
    {
        openUrlInBrowser(Uri.parse(PROJECT_GITHUB_URL));
    }

    public void clickNoteApp(MenuItem item)
    {
        // https://developer.android.com/distribute/tools/promote/linking.html#android-app
        // Ex : details?id=com.google.android.apps.maps

        String appPackageName = APP_PACKAGE_NAME;

        // openUrlInBrowser(Uri.parse("https://play.google.com/store/apps/details?id="+ appPackageName));
        // openUrlInBrowser(Uri.parse("market://details?id=" + appPackageName));
        openInMarket(Uri.parse("market://details?id=" + appPackageName));
    }

    public void openUrlInBrowser(Uri uri)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(uri);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }

    /**
     * URI may start with "https://play.google.com/store/apps/details?id=" or "market://details?id=".
     *
     * @param uri
     */
    public void openInMarket(Uri uri)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(uri);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try
        {
            startActivity(
                    //Intent.createChooser(
                    intent);
        } catch (ActivityNotFoundException e)
        {
            Toast.makeText(this, R.string.msg_err_playstore_not_installed, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline()
    {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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

}
