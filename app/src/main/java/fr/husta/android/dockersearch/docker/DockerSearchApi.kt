package fr.husta.android.dockersearch.docker

import fr.husta.android.dockersearch.docker.model.RepositoryTagsResponse
import fr.husta.android.dockersearch.docker.model.SearchItemsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DockerSearchApi {

    @GET("v1/search")
    suspend fun searchImages(@Query("q") term: String, @Query("n") size: Int = 25): SearchItemsResponse

    @GET("v2/repositories/{repository}/tags")
    suspend fun listTags(
        @Path("repository", encoded = true) repository: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 25
    ): RepositoryTagsResponse

}