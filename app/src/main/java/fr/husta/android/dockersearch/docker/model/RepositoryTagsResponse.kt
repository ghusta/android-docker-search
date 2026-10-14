package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryTagsResponse(

    @Json(name = "count")
    val totalCount: Int,

    @Json(name = "results")
    val results: List<RepositoryTagKotlin>

)