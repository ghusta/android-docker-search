package fr.husta.android.dockersearch.docker.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.time.Instant

@JsonClass(generateAdapter = true)
@Parcelize
data class RepositoryTag(

    @Json(name = "name")
    val name: String,

    @Json(name = "full_size")
    val fullSize: Long?,

    @Json(name = "last_updated")
    val lastUpdated: Instant?,

    @Json(name = "images")
    val imageVariants: List<ImageVariantByTag>

) : Parcelable