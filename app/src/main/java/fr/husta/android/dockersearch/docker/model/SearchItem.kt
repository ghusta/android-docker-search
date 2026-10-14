package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchItem(

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
)