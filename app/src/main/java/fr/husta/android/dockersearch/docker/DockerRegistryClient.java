package fr.husta.android.dockersearch.docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.husta.android.dockersearch.AppConstants;
import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DockerRegistryClient
{

    /**
     * @deprecated HTTP 301 with "Location: https://registry.hub.docker.com/"
     */
    @Deprecated
    private static final String BASE_URI = "https://index.docker.io";
    private static final String BASE_REGISTRY_URI = "https://registry.hub.docker.com";

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

    @Deprecated
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

    @Deprecated
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

    public void listTagsV2Async(String repository, Callback<ContainerRepositoryTagV2> callback)
    {
        listTagsV2Async(repository, 1, callback);
    }

    public void listTagsV2Async(String repository, int page, Callback<ContainerRepositoryTagV2> callback)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_REGISTRY_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(
                        JacksonConverterFactory.create(new ObjectMapper().registerModule(new JodaModule())))
                .build();

        DockerSearchRestService dockerSearchService = retrofit.create(DockerSearchRestService.class);
        Call<ContainerRepositoryTagV2> call =
                dockerSearchService.listTagsV2(repository, page, AppConstants.TAG_LIST_PAGE_SIZE);
//        Log.d("DOCKER_CLIENT", "Calling Docker Registry API (listTags)...");
        call.enqueue(callback);
    }

    public ContainerRepositoryTagV2 listTagsV2(String repository) throws IOException
    {
        return listTagsV2(repository, 1);
    }

    public ContainerRepositoryTagV2 listTagsV2(String repository, int page) throws IOException
    {
        return listTagsV2(repository, page, AppConstants.TAG_LIST_PAGE_SIZE);
    }

    ContainerRepositoryTagV2 listTagsV2(String repository, int page, int pageSize) throws IOException
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_REGISTRY_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
//                        .addInterceptor(new RequestLoggingInterceptor())
                        .build())
                .addConverterFactory(
                        JacksonConverterFactory.create(new ObjectMapper().registerModule(new JodaModule())))
                .build();

        DockerSearchRestService dockerSearchService = retrofit.create(DockerSearchRestService.class);
        Call<ContainerRepositoryTagV2> call =
                dockerSearchService.listTagsV2(repository, page, pageSize);

        Response<ContainerRepositoryTagV2> response = call.execute();
//        int count = response.body().getTotalCount();
//        return response.body().getTags();
        if (response.isSuccessful())
        {
            return response.body();
        }
        else
        {
            ResponseBody errorBody = response.errorBody();
            throw new RuntimeException("Error (" + response.code() + ")");
        }
    }

}
