package fr.husta.android.dockersearch.docker.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageVariantByTag(

    @Json(name = "digest")
    val digest: String,

    @Json(name = "os")
    val os: String,

    @Json(name = "os_version")
    val osVersion: String?,

    @Json(name = "architecture")
    val architecture: String,

    @Json(name = "variant")
    val variant: String?

)
