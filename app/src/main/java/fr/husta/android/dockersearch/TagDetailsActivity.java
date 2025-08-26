package fr.husta.android.dockersearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import fr.husta.android.dockersearch.databinding.ActivityTagDetailsBinding;
import fr.husta.android.dockersearch.docker.model.ImageVariantByTagV2;
import fr.husta.android.dockersearch.listadapter.DockerTagDetailsListAdapter;

import static java.util.function.Predicate.not;

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

        setSupportActionBar(binding.topAppBar);

        Intent intent = getIntent();
        String tagName = intent.getStringExtra(DATA_TAG_NAME);
        Log.d("TAG_DETAILS", "Tag : " + tagName);
        List<ImageVariantByTagV2> imageVariants = intent.getParcelableArrayListExtra(DATA_IMG_VARIANT_ARRAY);
        Log.d("TAG_DETAILS", "List<ImageVariantByTagV2> # = " + imageVariants.size());
        // filter ImageVariantByTagV2 list to hide "unknown" value
        List<ImageVariantByTagV2> imageVariantsFiltered = filterShownVariants(imageVariants);

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
        dockerTagDetailsListAdapter.addAll(imageVariantsFiltered);
    }

    public static List<ImageVariantByTagV2> filterShownVariants(List<ImageVariantByTagV2> imageVariants)
    {
        return imageVariants.stream()
                .filter(not(isUnknownOrEmptyValue()))
                .collect(Collectors.toList());
    }

    private static Predicate<ImageVariantByTagV2> isUnknownOrEmptyValue()
    {
        return variant -> variant.getArchitecture().equals("unknown")
                || variant.getArchitecture().isEmpty()
                || variant.getOs().equals("unknown");
    }

}