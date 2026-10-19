package fr.husta.android.dockersearch.docker.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryTagsResponse(

    @Json(name = "count")
    val totalCount: Int,

    @JsonProperty("next")
    val nextUrl: String?,

    @JsonProperty("previous")
    val previousUrl: String?,

    @Json(name = "results")
    val results: List<RepositoryTagKotlin>

)