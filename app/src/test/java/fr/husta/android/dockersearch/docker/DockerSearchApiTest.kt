package fr.husta.android.dockersearch.docker

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.Proxy
import java.util.Date
import java.util.concurrent.TimeUnit

class DockerSearchApiTest {

    lateinit var dockerSearchApi: DockerSearchApi

    @Before
    fun setUp() {
        val moshi = Moshi.Builder()
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY)
            .build()

        val retrofitMain = Retrofit.Builder()
            .baseUrl(BASE_REGISTRY_URI)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        dockerSearchApi = retrofitMain.create(DockerSearchApi::class.java)
    }

    @Test
    fun searchImages() = runTest {
        val result = dockerSearchApi.searchImages("postgres")
        assert(result.results.size > 10)
    }

    companion object {
        const val BASE_REGISTRY_URI = "https://registry.hub.docker.com/"
    }

}