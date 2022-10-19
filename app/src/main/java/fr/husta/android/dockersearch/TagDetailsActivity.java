package fr.husta.android.dockersearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import fr.husta.android.dockersearch.databinding.ActivityTagDetailsBinding;
import fr.husta.android.dockersearch.docker.model.ImageVariantByTagV2;
import fr.husta.android.dockersearch.listadapter.DockerTagDetailsListAdapter;

public class TagDetailsActivity extends AppCompatActivity
{

    public static final String DATA_TAG_NAME = "DATA_TAG_NAME";
    public static final String DATA_IMG_VARIANT_ARRAY = "DATA_IMG_VARIANT_ARRAY";

    private ActivityTagDetailsBinding binding;

    private DockerTagDetailsListAdapter dockerTagDetailsListAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityTagDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String tagName = intent.getStringExtra(DATA_TAG_NAME);
        Log.d("TAG_DETAILS", "Tag : " + tagName);
        List<ImageVariantByTagV2> imageVariants = intent.getParcelableArrayListExtra(DATA_IMG_VARIANT_ARRAY);
        Log.d("TAG_DETAILS", "List<ImageVariantByTagV2> # = " + imageVariants.size());

        // setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Details");
            getSupportActionBar().setSubtitle("Tag : " + tagName);
        }

        listView = binding.tagDetailsListview;
        View headerView = getLayoutInflater().inflate(R.layout.list_docker_tag_detail_headers, listView, false);
        listView.addHeaderView(headerView, null, false);

        dockerTagDetailsListAdapter = new DockerTagDetailsListAdapter(TagDetailsActivity.this, new ArrayList<>());
        listView.setAdapter(dockerTagDetailsListAdapter);
        dockerTagDetailsListAdapter.addAll(imageVariants);
    }

}