package fr.husta.android.dockersearch;

import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import fr.husta.android.dockersearch.docker.DockerRegistryClient;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import fr.husta.android.dockersearch.docker.model.RepositoryTagV2;
import fr.husta.android.dockersearch.listadapter.DockerTagListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_tags);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorSecondary);
        swipeRefreshLayout.setOnRefreshListener(this);

        requestTagsList(imageName, 1);
    }

    private void requestTagsList(String imgName, final int pageNumber)
    {
        // Fetch list tags (first page)
        progressBar.show();
        final DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        dockerRegistryClient.listTagsV2Async(imageNameToRepository(imgName), pageNumber, new Callback<ContainerRepositoryTagV2>()
        {
            @Override
            public void onResponse(Call<ContainerRepositoryTagV2> call, Response<ContainerRepositoryTagV2> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body() != null)
                    {
                        List<RepositoryTagV2> listTags = response.body().getTags();
                        int count = response.body().getTotalCount();

                        dockerTagListAdapter.addAll(listTags);
                        // dockerTagListAdapter.notifyDataSetChanged();

                        currentPage = pageNumber;
                        Log.d(TAG, "requestTagsList() - currentPage = " + currentPage);
                        hasNextPage = response.body().getNextUrl() != null;
                        Log.d(TAG, "requestTagsList() - hasNextPage = " + hasNextPage);

                        if (hasNextPage)
                        {
                            fabNextPage.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            fabNextPage.setVisibility(View.GONE);
                        }
                    }
                }

                progressBar.hide();
            }

            @Override
            public void onFailure(Call<ContainerRepositoryTagV2> call, Throwable t)
            {
                progressBar.hide();

                Toast.makeText(TagListActivity.this, getString(R.string.msg_error, t.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
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

}
