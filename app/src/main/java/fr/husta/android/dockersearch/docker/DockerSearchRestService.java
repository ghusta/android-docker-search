package fr.husta.android.dockersearch.docker;

import java.util.List;

import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import retrofit2.Call;
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
    Call<ContainerImageSearchResult> searchImages(@Query("q") String term, @Query("n") int size);

    @GET("v1/repositories/{repository}/tags")
    Call<List<RepositoryTag>> listTags(@Path("repository") String repository);

}
