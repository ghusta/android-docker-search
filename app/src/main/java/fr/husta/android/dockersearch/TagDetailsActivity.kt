package fr.husta.android.dockersearch

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import fr.husta.android.dockersearch.databinding.ActivityTagDetailsBinding
import fr.husta.android.dockersearch.docker.model.ImageVariantByTagV2
import fr.husta.android.dockersearch.listadapter.DockerTagDetailsListAdapter
import java.util.function.Predicate
import java.util.stream.Collectors

class TagDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTagDetailsBinding

    private var dockerTagDetailsListAdapter: DockerTagDetailsListAdapter? = null

    private var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTagDetailsBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        setSupportActionBar(binding.topAppBar)

        val intent = getIntent()
        val tagName = intent.getStringExtra(DATA_TAG_NAME)
        Log.d("TAG_DETAILS", "Tag : " + tagName)
        val imageVariants: MutableList<ImageVariantByTagV2?>? =
            intent.getParcelableArrayListExtra<ImageVariantByTagV2?>(DATA_IMG_VARIANT_ARRAY)
        Log.d("TAG_DETAILS", "List<ImageVariantByTagV2> # = " + imageVariants!!.size)
        // filter ImageVariantByTagV2 list to hide "unknown" value
        val imageVariantsFiltered: MutableList<ImageVariantByTagV2?> =
            filterShownVariants(imageVariants)

        // setSupportActionBar(binding.toolbar);
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.title = "Details"
            actionBar.subtitle = "Tag : $tagName"
        }

        listView = binding.tagDetailsListview
        ViewCompat.setNestedScrollingEnabled(listView!!, true)
        val headerView =
            getLayoutInflater().inflate(R.layout.list_docker_tag_detail_headers, listView, false)
        listView!!.addHeaderView(headerView, null, false)

        dockerTagDetailsListAdapter =
            DockerTagDetailsListAdapter(this@TagDetailsActivity, ArrayList<ImageVariantByTagV2?>())
        listView!!.setAdapter(dockerTagDetailsListAdapter)
        dockerTagDetailsListAdapter!!.addAll(imageVariantsFiltered)
    }

    companion object {
        const val DATA_TAG_NAME: String = "DATA_TAG_NAME"
        const val DATA_IMG_VARIANT_ARRAY: String = "DATA_IMG_VARIANT_ARRAY"

        fun filterShownVariants(imageVariants: MutableList<ImageVariantByTagV2?>): MutableList<ImageVariantByTagV2?> {
            return imageVariants.stream()
                .filter(Predicate.not<ImageVariantByTagV2?>(isUnknownOrEmptyValue))
                .collect(Collectors.toList())
        }

        private val isUnknownOrEmptyValue: Predicate<ImageVariantByTagV2?>
            get() = Predicate { variant: ImageVariantByTagV2? ->
                variant!!.getArchitecture() == "unknown"
                        || variant.getArchitecture().isEmpty()
                        || variant.getOs() == "unknown"
            }
    }
}