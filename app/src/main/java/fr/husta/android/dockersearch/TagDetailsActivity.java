package fr.husta.android.dockersearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fr.husta.android.dockersearch.databinding.ActivityTagDetailsBinding;
import fr.husta.android.dockersearch.docker.model.ImageVariantByTagV2;
import fr.husta.android.dockersearch.listadapter.DockerTagDetailsListAdapter;

import static java.util.function.Predicate.not;

public class TagDetailsActivity extends AppCompatActivity {

    public static final String DATA_TAG_NAME = "DATA_TAG_NAME";
    public static final String DATA_IMG_VARIANT_ARRAY = "DATA_IMG_VARIANT_ARRAY";

    private ActivityTagDetailsBinding binding;

    private DockerTagDetailsListAdapter dockerTagDetailsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Enable edge-to-edge display
        EdgeToEdge.enable(this);

        binding = ActivityTagDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.activityTagdetails, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets displayCutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout());

            // Apply padding to the AppBarLayout to account for status bar and cutouts
            binding.appBarLayout.setPadding(
                    systemBars.left + displayCutout.left,
                    systemBars.top,
                    systemBars.right + displayCutout.right,
                    binding.appBarLayout.getPaddingBottom());

            binding.tagDetailsListview.setPadding(
                    systemBars.left + displayCutout.left,
                    binding.tagDetailsListview.getPaddingTop(),
                    systemBars.right + displayCutout.right,
                    systemBars.bottom);

            return insets;
        });

        setSupportActionBar(binding.topAppBar);

        Intent intent = getIntent();
        String tagName = intent.getStringExtra(DATA_TAG_NAME);
        Log.d("TAG_DETAILS", "Tag : " + tagName);
        List<ImageVariantByTagV2> imageVariants = intent.getParcelableArrayListExtra(DATA_IMG_VARIANT_ARRAY);
        Log.d("TAG_DETAILS", "List<ImageVariantByTagV2> # = " + imageVariants.size());
        // filter ImageVariantByTagV2 list to hide "unknown" value
        List<ImageVariantByTagV2> imageVariantsFiltered = filterShownVariants(imageVariants);

        // setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Details");
            getSupportActionBar().setSubtitle("Tag : " + tagName);
        }

        ViewCompat.setNestedScrollingEnabled(binding.tagDetailsListview, true);
        View headerView = getLayoutInflater().inflate(R.layout.list_docker_tag_detail_headers, binding.tagDetailsListview, false);
        binding.tagDetailsListview.addHeaderView(headerView, null, false);

        dockerTagDetailsListAdapter = new DockerTagDetailsListAdapter(TagDetailsActivity.this, new ArrayList<>());
        binding.tagDetailsListview.setAdapter(dockerTagDetailsListAdapter);
        dockerTagDetailsListAdapter.addAll(imageVariantsFiltered);
    }

    public static List<ImageVariantByTagV2> filterShownVariants(List<ImageVariantByTagV2> imageVariants) {
        return imageVariants.stream()
                .filter(not(isUnknownOrEmptyValue()))
                .collect(Collectors.toList());
    }

    private static Predicate<ImageVariantByTagV2> isUnknownOrEmptyValue() {
        return variant -> variant.getArchitecture().equals("unknown")
                || variant.getArchitecture().isEmpty()
                || variant.getOs().equals("unknown");
    }

}