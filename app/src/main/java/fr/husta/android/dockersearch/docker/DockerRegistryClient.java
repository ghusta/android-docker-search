package fr.husta.android.dockersearch.docker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.util.concurrent.TimeUnit;

import fr.husta.android.dockersearch.AppConstants;
import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DockerRegistryClient
{

    /**
     * @deprecated HTTP 301 with "Location: https://registry.hub.docker.com/"
     */
    @Deprecated
    private static final String BASE_URI = "https://index.docker.io";
    private static final String BASE_REGISTRY_URI = "https://registry.hub.docker.com";

    /**
     * Retrofit using async callbacks
     */
    private final Retrofit retrofitDockerIndex;
    /**
     * Retrofit using RxJava2
     */
    private final Retrofit retrofitRxJava2;

    public DockerRegistryClient()
    {
        ObjectMapper objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JodaModule());

        retrofitDockerIndex = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        retrofitRxJava2 = new Retrofit.Builder()
                .baseUrl(BASE_REGISTRY_URI)
                .client(new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public Observable<ContainerImageSearchResult> searchImagesAsync(String term)
    {
        final int pageSize = AppConstants.IMAGE_SEARCH_PAGE_SIZE;

        DockerSearchRestService dockerSearchService = retrofitDockerIndex.create(DockerSearchRestService.class);
        return dockerSearchService.searchImages(term, pageSize);
    }

    public Observable<ContainerRepositoryTagV2> listTagsV2(String repository)
    {
        return listTagsV2(repository, 1);
    }

    public Observable<ContainerRepositoryTagV2> listTagsV2(String repository, int page)
    {
        return listTagsV2(repository, page, AppConstants.TAG_LIST_PAGE_SIZE);
    }

    public Observable<ContainerRepositoryTagV2> listTagsV2(String repository, int page, int pageSize)
    {
        DockerSearchRestService dockerSearchService = retrofitRxJava2.create(DockerSearchRestService.class);
        return dockerSearchService.listTagsV2(repository, page, pageSize);
    }
}
