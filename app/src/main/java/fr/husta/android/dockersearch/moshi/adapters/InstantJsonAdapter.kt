package fr.husta.android.dockersearch.docker.moshi.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantJsonAdapter {

    @ToJson
    fun toJson(instant: Instant?): String? {
        return instant?.toString()
    }

    @FromJson
    fun fromJson(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

}