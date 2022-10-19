package fr.husta.android.dockersearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import fr.husta.android.dockersearch.docker.DockerRegistryClient;
import fr.husta.android.dockersearch.docker.model.RepositoryTagV2;
import fr.husta.android.dockersearch.listadapter.DockerTagListAdapter;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TagListActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener
{

    private static final String TAG = "TAG_LIST";

    public static final String DATA_IMG_NAME = "DATA_IMG_NAME";

    private DockerTagListAdapter dockerTagListAdapter;

    private ListView listView;
    private FloatingActionButton fabNextPage;

    private ProgressDialog progressBar;

    private int currentPage = -1;
    private boolean hasNextPage = false;
    private String imageName;

    private CompositeDisposable disposables = new CompositeDisposable();
    private DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("MAIN_DEBUG", "onCreate : " + this.getLocalClassName());
        setContentView(R.layout.activity_taglist);

        Intent intent = getIntent();
        imageName = intent.getStringExtra(DATA_IMG_NAME);

        // https://developer.android.com/training/implementing-navigation/ancestral.html#up
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Log.d(TAG, "SupportActionBar.title = " + getSupportActionBar().getTitle());
            getSupportActionBar().setTitle("Tags");
            getSupportActionBar().setSubtitle("Image : " + imageName);
        }

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage(getString(R.string.msg_searching));

        listView = findViewById(R.id.tags_listview);
        fabNextPage = findViewById(R.id.fab_tags_next_page);
        fabNextPage.setOnClickListener(view -> loadNextPage(view));

        dockerTagListAdapter = new DockerTagListAdapter(TagListActivity.this, new ArrayList<>());
        listView.setAdapter(dockerTagListAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            RepositoryTagV2 item = (RepositoryTagV2) listView.getItemAtPosition(i);
            int count = item.getImageVariants().size();
            Snackbar.make(fabNextPage, getResources().getQuantityString(R.plurals.msg_count_tag_images, count, count), Snackbar.LENGTH_SHORT)
                    //.setAnchorView(fabNextPage)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                    .setAction("Details", view1 -> {
                        Log.d(TAG, "Tag : " + item.getName());
                        startActivityTagDetails(this, item);
                    })
                    .show();
        });

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_tags);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorSecondary);
        swipeRefreshLayout.setOnRefreshListener(this);

        requestTagsList(imageName, 1);
    }

    public void startActivityTagDetails(Context context, RepositoryTagV2 data)
    {
        Intent starter = new Intent(context, TagDetailsActivity.class);
        starter.putExtra(TagDetailsActivity.DATA_TAG_NAME, data.getName());
        starter.putParcelableArrayListExtra(TagDetailsActivity.DATA_IMG_VARIANT_ARRAY, new ArrayList<>(data.getImageVariants()));
        startActivity(starter);
    }

    private void requestTagsList(String imgName, final int pageNumber)
    {
        // Fetch list tags (first page)
        Disposable disposable = dockerRegistryClient.listTagsV2(imageNameToRepository(imgName), pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(data -> progressBar.show())
                .subscribe(data -> {
                            List<RepositoryTagV2> listTags = data.getTags();
                            int count = data.getTotalCount();

                            dockerTagListAdapter.addAll(listTags);
                            // dockerTagListAdapter.notifyDataSetChanged();

                            currentPage = pageNumber;
                            Log.d(TAG, "requestTagsList() - currentPage = " + currentPage);
                            hasNextPage = data.getNextUrl() != null;
                            Log.d(TAG, "requestTagsList() - hasNextPage = " + hasNextPage);

                            // Lint incorrectly reported :
                            // Error: VisibilityAwareImageButton.setVisibility can only be called from within
                            // the same library group (groupId=com.google.android.material) [RestrictedApi]
                            // See : https://stackoverflow.com/questions/50343634/android-p-visibilityawareimagebutton-setvisibility-can-only-be-called-from-the-s
                            if (hasNextPage)
                            {
                                fabNextPage.show();
                            }
                            else
                            {
                                fabNextPage.hide();
                            }
                        }, throwable -> {
                            progressBar.hide();
                            Toast.makeText(TagListActivity.this, getString(R.string.msg_error, throwable.getMessage()), Toast.LENGTH_LONG).show();
                        }, () -> progressBar.hide()
                );
        disposables.add(disposable);
    }

    public static String imageNameToRepository(final String imageName)
    {
        if (imageName == null)
        {
            throw new IllegalArgumentException();
        }
        if (imageName.contains("/"))
        {
            return imageName;
        }
        else
        {
            return "library/" + imageName;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_tags, menu);

        // MenuItem warningItem = menu.findItem(R.id.menu_warning_taglist);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean ret;
        switch (item.getItemId())
        {
            case R.id.menu_warning_taglist:
                // Toast.makeText(this, "Warning... Tag list not up to date", Toast.LENGTH_SHORT).show();
                clickWarning(item);
                ret = true;
                break;

            default:
                ret = false;
        }

        return ret;
    }

    /**
     * Disclaimer !!!
     *
     * @param item
     */
    public void clickWarning(MenuItem item)
    {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.dialog_warning_taglist, null, false);

        TextView textView = messageView.findViewById(R.id.txt_warning_taglist);
        textView.setText(
                "Tag list may not be up to date. \n" +
                        "See bug #687 ( https://github.com/docker/hub-feedback/issues/687 )");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning").setIcon(R.drawable.ic_warning_black_24dp);
        builder.setView(messageView);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create();
        builder.show();
    }

    public void loadNextPage(View view)
    {
        if (hasNextPage)
        {
            requestTagsList(imageName, currentPage + 1);
        }
    }

    @Override
    public void onRefresh()
    {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_tags);
        swipeRefreshLayout.setRefreshing(false);
        dockerTagListAdapter.clear();
        requestTagsList(imageName, 1);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (progressBar != null)
        {
            progressBar.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        this.disposables.clear();
        if (progressBar != null)
        {
            progressBar.dismiss();
        }
    }
}
