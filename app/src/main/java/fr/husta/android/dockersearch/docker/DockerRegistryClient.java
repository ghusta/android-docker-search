package fr.husta.android.dockersearch.docker;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.husta.android.dockersearch.AppConstants;
import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DockerRegistryClient
{

    private static final String BASE_URI = "https://index.docker.io";

    public DockerRegistryClient()
    {
    }

    public void searchImagesAsync(String term, Callback<ContainerImageSearchResult> callback)
    {
        final int pageSize = AppConstants.IMAGE_SEARCH_PAGE_SIZE;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(
                        JacksonConverterFactory.create())
                .build();

        DockerSearchRestService dockerSearchService = retrofit.create(DockerSearchRestService.class);
        Call<ContainerImageSearchResult> call = dockerSearchService.searchImages(term, pageSize);
//        Log.d("DOCKER_CLIENT", "Calling Docker Registry API (searchImages)...");
        call.enqueue(callback);
    }

    public void listTagsAsync(String repository, Callback<List<RepositoryTag>> callback)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(
                        JacksonConverterFactory.create())
                .build();

        DockerSearchRestService dockerSearchService = retrofit.create(DockerSearchRestService.class);
        Call<List<RepositoryTag>> call = dockerSearchService.listTags(repository);
//        Log.d("DOCKER_CLIENT", "Calling Docker Registry API (listTags)...");
        call.enqueue(callback);
    }

    public List<RepositoryTag> listTags(String repository) throws IOException
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(
                        JacksonConverterFactory.create())
                .build();

        DockerSearchRestService dockerSearchService = retrofit.create(DockerSearchRestService.class);
        Call<List<RepositoryTag>> call = dockerSearchService.listTags(repository);
        return call.execute().body();
    }

}
