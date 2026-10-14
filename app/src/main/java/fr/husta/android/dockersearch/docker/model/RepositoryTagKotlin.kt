package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class RepositoryTagKotlin(

    @Json(name = "name")
    val name: String,

    @Json(name = "full_size")
    val fullSize: Long,

    @Json(name = "last_updated")
    val lastUpdated: Instant,

    @Json(name = "images")
    val imageVariants: List<ImageVariantByTag>

)