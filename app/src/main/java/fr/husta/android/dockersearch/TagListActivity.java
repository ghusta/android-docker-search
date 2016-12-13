package fr.husta.android.dockersearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.husta.android.dockersearch.docker.DockerRegistryClient;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import fr.husta.android.dockersearch.listadapter.DockerTagListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagListActivity extends AppCompatActivity
{

    private static final String TAG = "TAG_LIST";

    public static final String DATA_IMG_NAME = "DATA_IMG_NAME";

    private DockerTagListAdapter dockerTagListAdapter;

    private ListView listView;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("MAIN_DEBUG", "onCreate : " + this.getLocalClassName());
        setContentView(R.layout.activity_taglist);

        Intent intent = getIntent();
        String imgName = intent.getStringExtra(DATA_IMG_NAME);

        // https://developer.android.com/training/implementing-navigation/ancestral.html#up
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Log.d(TAG, "SupportActionBar.title = " + getSupportActionBar().getTitle());
//            getSupportActionBar().setTitle("Tags : " + imgName);
//            getSupportActionBar().setSubtitle(null);
            getSupportActionBar().setTitle("Tags");
            getSupportActionBar().setSubtitle("Image : " + imgName);
        }

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage(getString(R.string.msg_searching));

        listView = (ListView) findViewById(R.id.tags_listview);

        dockerTagListAdapter = new DockerTagListAdapter(TagListActivity.this, new ArrayList<RepositoryTag>());
        listView.setAdapter(dockerTagListAdapter);

        // Fetch list tags
        progressBar.show();
        final DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        dockerRegistryClient.listTagsAsync(imgName, new Callback<List<RepositoryTag>>()
        {
            @Override
            public void onResponse(Call<List<RepositoryTag>> call, Response<List<RepositoryTag>> response)
            {
                List<RepositoryTag> listTags = response.body();

                dockerTagListAdapter = new DockerTagListAdapter(TagListActivity.this, listTags);
                listView.setAdapter(dockerTagListAdapter);

//                dockerImageListAdapter.setNotifyOnChange(false);
//                dockerImageListAdapter.clear();
//                dockerImageListAdapter.addAll(body.getResults());
//                dockerImageListAdapter.notifyDataSetChanged();

                progressBar.hide();
            }

            @Override
            public void onFailure(Call<List<RepositoryTag>> call, Throwable t)
            {
                progressBar.hide();

                Toast.makeText(TagListActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
