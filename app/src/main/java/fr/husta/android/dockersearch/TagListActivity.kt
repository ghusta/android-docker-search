package fr.husta.android.dockersearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import fr.husta.android.dockersearch.databinding.ActivityTaglistBinding
import fr.husta.android.dockersearch.databinding.DialogWarningTaglistBinding
import fr.husta.android.dockersearch.docker.DockerRegistryClient
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2
import fr.husta.android.dockersearch.docker.model.ImageVariantByTagV2
import fr.husta.android.dockersearch.docker.model.RepositoryTagV2
import fr.husta.android.dockersearch.listadapter.DockerTagListAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class TagListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaglistBinding

    private var dialogWarningTaglistBinding: DialogWarningTaglistBinding? = null

    private var dockerTagListAdapter: DockerTagListAdapter? = null

    private var listView: ListView? = null
    private var fabNextPage: FloatingActionButton? = null

    private var currentPage = -1
    private var hasNextPage = false
    private var imageName: String? = null

    private val disposables = CompositeDisposable()
    private val dockerRegistryClient = DockerRegistryClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MAIN_DEBUG", "onCreate : " + this.getLocalClassName())
        binding = ActivityTaglistBinding.inflate(getLayoutInflater())
        dialogWarningTaglistBinding = DialogWarningTaglistBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        setSupportActionBar(binding.topAppBar)

        val intent = getIntent()
        imageName = intent.getStringExtra(DATA_IMG_NAME)

        // https://developer.android.com/training/implementing-navigation/ancestral.html#up
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            Log.d(TAG, "SupportActionBar.title = ${actionBar.title}")
            actionBar.title = "Tags"
            actionBar.subtitle = "Image : $imageName"
        }

        listView = binding.tagsListview
        ViewCompat.setNestedScrollingEnabled(listView!!, true)
        fabNextPage = binding.fabTagsNextPage
        fabNextPage!!.setOnClickListener { view: View? -> loadNextPage(view) }

        dockerTagListAdapter =
            DockerTagListAdapter(this@TagListActivity, ArrayList<RepositoryTagV2?>())
        listView!!.setAdapter(dockerTagListAdapter)

        listView!!.setOnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
            val item = listView!!.getItemAtPosition(i) as RepositoryTagV2
            val count = TagDetailsActivity.filterShownVariants(item.getImageVariants()).size
            Snackbar.make(
                listView!!,
                getResources().getQuantityString(R.plurals.msg_count_tag_images, count, count),
                BaseTransientBottomBar.LENGTH_SHORT
            )
                .setAnchorView(if (fabNextPage!!.isOrWillBeShown()) fabNextPage else null)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                .setAction(R.string.snackbar_action_details) { view1: View? ->
                    Log.d(TAG, "Tag : " + item.getName())
                    startActivityTagDetails(this, item)
                }
                .show()
        }

        val swipeRefreshLayout = binding.swipeRefreshTags
        swipeRefreshLayout.setColorSchemeResources(
            R.color.md_theme_primary,
            R.color.md_theme_primary
        )
        swipeRefreshLayout.setOnRefreshListener { this.onSwipeRefresh() }

        requestTagsList(
            imageName!!, 1,
            {
                dockerTagListAdapter!!.clear()
                binding.progressIndicator.show()
            },
            { binding.progressIndicator.hide() })
    }

    fun startActivityTagDetails(context: Context?, data: RepositoryTagV2) {
        val starter = Intent(context, TagDetailsActivity::class.java)
        starter.putExtra(TagDetailsActivity.DATA_TAG_NAME, data.name)
        starter.putParcelableArrayListExtra(
            TagDetailsActivity.DATA_IMG_VARIANT_ARRAY,
            ArrayList<ImageVariantByTagV2?>(data.imageVariants)
        )
        startActivity(starter)
    }

    private fun requestTagsList(
        imgName: String, pageNumber: Int,
        onStart: Runnable, onEnd: Runnable
    ) {
        // Fetch list tags (first page)
        val disposable = dockerRegistryClient.listTagsV2(imageNameToRepository(imgName), pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(Consumer { data: Disposable? -> onStart.run() })
            .subscribe(Consumer { data: ContainerRepositoryTagV2? ->
                val listTags = data!!.getTags()
                dockerTagListAdapter!!.addAll(listTags)

                // dockerTagListAdapter.notifyDataSetChanged();
                currentPage = pageNumber
                Log.d(TAG, "requestTagsList() - currentPage = " + currentPage)
                hasNextPage = data.getNextUrl() != null
                Log.d(TAG, "requestTagsList() - hasNextPage = " + hasNextPage)

                // Lint incorrectly reported :
                // Error: VisibilityAwareImageButton.setVisibility can only be called from within
                // the same library group (groupId=com.google.android.material) [RestrictedApi]
                // See : https://stackoverflow.com/questions/50343634/android-p-visibilityawareimagebutton-setvisibility-can-only-be-called-from-the-s
                if (hasNextPage) {
                    fabNextPage!!.show()
                } else {
                    fabNextPage!!.hide()
                }
            }, Consumer { throwable: Throwable? ->
                onEnd.run()
                Toast.makeText(
                    this@TagListActivity,
                    getString(R.string.msg_error, throwable!!.message),
                    Toast.LENGTH_LONG
                ).show()
            }
            ) { onEnd.run() }
        disposables.add(disposable)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = getMenuInflater()
        inflater.inflate(R.menu.options_menu_tags, menu)

        // MenuItem warningItem = menu.findItem(R.id.menu_warning_taglist);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.menu_warning_taglist) {
            // Toast.makeText(this, "Warning... Tag list not up to date", Toast.LENGTH_SHORT).show();
            clickWarning(item)
            return true
        }

        return false
    }

    /**
     * Disclaimer !!!
     * 
     * @param item
     */
    fun clickWarning(item: MenuItem?) {
        // Inflate the about message contents
        val messageView: View = dialogWarningTaglistBinding!!.getRoot()

        val textView = dialogWarningTaglistBinding!!.txtWarningTaglist
        textView.setText(
            "Tag list may not be up to date. \n" +
                    "See bug #687 ( https://github.com/docker/hub-feedback/issues/687 )"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Warning").setIcon(R.drawable.ic_warning_black_24dp)
        builder.setView(messageView)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.create()
        builder.show()
    }

    fun loadNextPage(view: View?) {
        if (hasNextPage) {
            requestTagsList(
                imageName!!, currentPage + 1,
                { binding.progressIndicator.show() },
                { binding.progressIndicator.hide() })
        }
    }

    fun onSwipeRefresh() {
        val swipeRefreshLayout = binding.swipeRefreshTags
        requestTagsList(
            imageName!!, 1,
            { dockerTagListAdapter!!.clear() },
            { swipeRefreshLayout.isRefreshing = false })
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.disposables.clear()
    }

    companion object {
        private const val TAG = "TAG_LIST"

        const val DATA_IMG_NAME: String = "DATA_IMG_NAME"

        fun imageNameToRepository(imageName: String): String {
            return if (imageName.contains("/")) {
                imageName
            } else {
                "library/$imageName"
            }
        }
    }
}
