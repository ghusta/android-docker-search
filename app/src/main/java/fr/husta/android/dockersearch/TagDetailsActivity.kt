package fr.husta.android.dockersearch

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import fr.husta.android.dockersearch.databinding.ActivityTagDetailsBinding
import fr.husta.android.dockersearch.docker.model.ImageVariantByTag
import fr.husta.android.dockersearch.listadapter.DockerTagDetailsListAdapter

class TagDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTagDetailsBinding

    private var dockerTagDetailsListAdapter: DockerTagDetailsListAdapter? = null

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTagDetailsBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        setSupportActionBar(binding.topAppBar)

        val intent = getIntent()
        val tagName = intent.getStringExtra(DATA_TAG_NAME)
        Log.d("TAG_DETAILS", "Tag : $tagName")
        val imageVariants: MutableList<ImageVariantByTag>? =
            intent.getParcelableArrayListExtra(DATA_IMG_VARIANT_ARRAY)
        Log.d("TAG_DETAILS", "List<ImageVariantByTagV2> # = " + imageVariants?.size)
        // filter ImageVariantByTagV2 list to hide "unknown" value
        var imageVariantsFiltered: List<ImageVariantByTag> = listOf()
        if (imageVariants != null) {
            imageVariantsFiltered = filterShownVariants(imageVariants)
        }

        // setSupportActionBar(binding.toolbar);
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.title = "Details"
            actionBar.subtitle = "Tag : $tagName"
        }

        listView = binding.tagDetailsListview
        ViewCompat.setNestedScrollingEnabled(listView, true)
        val headerView =
            layoutInflater.inflate(R.layout.list_docker_tag_detail_headers, listView, false)
        listView.addHeaderView(headerView, null, false)

        dockerTagDetailsListAdapter =
            DockerTagDetailsListAdapter(this@TagDetailsActivity, ArrayList())
        listView.setAdapter(dockerTagDetailsListAdapter)
        dockerTagDetailsListAdapter!!.addAll(imageVariantsFiltered)
    }

    companion object {
        const val DATA_TAG_NAME: String = "DATA_TAG_NAME"
        const val DATA_IMG_VARIANT_ARRAY: String = "DATA_IMG_VARIANT_ARRAY"

        fun filterShownVariants(imageVariants: List<ImageVariantByTag>): List<ImageVariantByTag> {
            return imageVariants.filterNot { variant ->
                variant.architecture == "unknown" ||
                        variant.architecture == null ||
                        variant.os == "unknown"
            }
        }

    }
}