package fr.husta.android.dockersearch.docker;

import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Docker Registry V1 REST API Contract.
 */
public interface DockerSearchRestService
{

    /**
     * @param term
     * @param size Between 1 and 100. Optional : 25 by default.
     * @return
     */
    @GET("v1/search")
    Observable<ContainerImageSearchResult> searchImages(@Query("q") String term, @Query("n") Integer size);

    /**
     * @param repository Like 'library/centos' (param must be encoded because contains /)
     * @param page       Ex 1
     * @param pageSize   Ex 25
     * @return
     */
    @GET("v2/repositories/{repository}/tags/")
    Observable<ContainerRepositoryTagV2> listTagsV2(@Path(value = "repository", encoded = true) String repository, @Query("page") Integer page, @Query("page_size") Integer pageSize);

}
