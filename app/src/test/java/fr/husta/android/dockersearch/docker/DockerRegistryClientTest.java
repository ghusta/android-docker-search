package fr.husta.android.dockersearch.docker;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ImageSearchResult;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class DockerRegistryClientTest
{

    private static final long _30_SECONDS_IN_MILLIS = 30 * 1000L;
    private static final long _45_SECONDS_IN_MILLIS = 45 * 1000L;

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    @Ignore
    public void searchImagesAsync() throws Exception
    {
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String term;

        term = "tomcat";
        dockerRegistryClient.searchImagesAsync(term, new Callback<ContainerImageSearchResult>()
        {
            @Override
            public void onResponse(Call<ContainerImageSearchResult> call, Response<ContainerImageSearchResult> response)
            {
                ContainerImageSearchResult body = response.body();
                List<ImageSearchResult> results = body.getResults();

                assertThat(results).isNotEmpty();
                System.out.println("Nb res = " + body.getNumResults());
            }

            @Override
            public void onFailure(Call<ContainerImageSearchResult> call, Throwable t)
            {
                fail();
            }
        });
    }

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void listTags() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

        repo = "tomcat";
        List<RepositoryTag> repositoryTags = dockerRegistryClient.listTags(repo);

        long end = System.currentTimeMillis();
        System.out.println("Reponse en : " + (end - start) + " ms");
        System.out.println(repositoryTags.size());
    }
}