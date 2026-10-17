package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import fr.husta.android.dockersearch.docker.moshi.adapters.InstantJsonAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_REGISTRY_URI = "https://registry.hub.docker.com/"

object DockerSearchApiClient {

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(InstantJsonAdapter())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .proxy(null)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_REGISTRY_URI)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val api: DockerSearchApi by lazy {
        retrofit.create(DockerSearchApi::class.java)
    }

}