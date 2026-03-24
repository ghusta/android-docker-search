package fr.husta.android.dockersearch.docker;

import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import io.reactivex.rxjava3.core.Observable;

/**
 * Docker Repository, following <a href="https://developer.android.com/topic/architecture/data-layer">Data layer</a>.
 */
public class DockerRepository {

    private final DockerRegistryClient dockerRegistryClient;

    public DockerRepository() {
        this.dockerRegistryClient = new DockerRegistryClient();
    }

    public Observable<ContainerImageSearchResult> searchImagesAsync(String term) {
        return dockerRegistryClient.searchImagesAsync(term);
    }

    public Observable<ContainerRepositoryTagV2> listTagsV2(String repository, int page) {
        return dockerRegistryClient.listTagsV2(repository, page);
    }

    public Observable<ContainerRepositoryTagV2> listTagsV2(String repository, int page, int pageSize) {
        return dockerRegistryClient.listTagsV2(repository, page, pageSize);
    }
}
