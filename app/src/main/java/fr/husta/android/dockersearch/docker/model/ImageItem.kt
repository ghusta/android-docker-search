package fr.husta.android.dockersearch.docker.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ImageItem(

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "star_count")
    val starCount: Int = 0,

    @Json(name = "is_official")
    val isOfficial: Boolean = false,

    @Json(name = "is_automated")
    val isAutomated: Boolean = false
) : Parcelable