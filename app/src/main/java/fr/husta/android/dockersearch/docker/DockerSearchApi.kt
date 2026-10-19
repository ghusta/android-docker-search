package fr.husta.android.dockersearch.docker

import fr.husta.android.dockersearch.AppConstants
import fr.husta.android.dockersearch.docker.model.RepositoryTagsResponse
import fr.husta.android.dockersearch.docker.model.SearchItemsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DockerSearchApi {

    /**
     * @param term Term to search.
     * @param size Between 1 and 100. Optional : 25 by default.
     */
    @GET("v1/search")
    suspend fun searchImages(
        @Query("q") term: String,
        @Query("n") size: Int = 25
    ): SearchItemsResponse

    /**
     * @param repository Like 'library/centos' (param must be encoded because it may contain /)
     * @param page       Ex 1
     * @param pageSize   Ex 25
     */
    @GET("v2/repositories/{repository}/tags")
    suspend fun listTags(
        @Path("repository", encoded = true) repository: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = AppConstants.TAG_LIST_PAGE_SIZE
    ): RepositoryTagsResponse

}