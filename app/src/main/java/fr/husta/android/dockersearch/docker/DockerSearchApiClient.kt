package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import fr.husta.android.dockersearch.docker.moshi.adapters.InstantJsonAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

const val BASE_REGISTRY_URI = "https://registry.hub.docker.com/"

class DockerSearchApiClient {

    val api: DockerSearchApi

    init {
        val moshi = Moshi.Builder()
            .add(InstantJsonAdapter())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_REGISTRY_URI)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(DockerSearchApi::class.java)
    }

}