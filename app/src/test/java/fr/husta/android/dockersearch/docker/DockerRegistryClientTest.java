package fr.husta.android.dockersearch.docker;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import fr.husta.android.dockersearch.docker.model.RepositoryTagV2;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.google.common.truth.Truth.assertThat;

public class DockerRegistryClientTest
{

    private static final long _30_SECONDS_IN_MILLIS = 30 * 1000L;
    private static final long _45_SECONDS_IN_MILLIS = 45 * 1000L;

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void searchImagesAsync() throws Exception
    {
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String term;

        term = "tomcat";
        Observable<ContainerImageSearchResult> containerImageSearchResultObservable = dockerRegistryClient.searchImagesAsync(term);
        ContainerImageSearchResult containerImageSearchResult = containerImageSearchResultObservable
                .blockingFirst();
        assertThat(containerImageSearchResult).isNotNull();
        assertThat(containerImageSearchResult.getResults()).isNotEmpty();
    }

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void listTagsV2() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

//        repo = "tomcat";
        repo = "library/tomcat";
        // ContainerRepositoryTagV2 containerRepositoryTagV2 = dockerRegistryClient.listTagsV2(repo);
        AtomicReference<ContainerRepositoryTagV2> containerRepositoryTagV2 = new AtomicReference<>();
        Disposable disposable = dockerRegistryClient.listTagsV2(repo).subscribe(containerRepositoryTagV2::set);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.get().getTags();

        long end = System.currentTimeMillis();
        System.out.println("Reponse en : " + (end - start) + " ms");
        System.out.println("Page count = " + repositoryTags.size());
        System.out.println("Total count = " + containerRepositoryTagV2.get().getTotalCount());
        assertThat(repositoryTags.size()).isGreaterThan(1);
        RepositoryTagV2 firstTag = repositoryTags.get(0);
        System.out.println(String.format("%s / %,d bytes / %s",
                firstTag.getName(), firstTag.getFullSize(), firstTag.getLastUpdated()));

        disposable.dispose();
    }

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void listTagsV2_Jenkins() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

//        repo = "tomcat";
        repo = "library/jenkins";
        AtomicReference<ContainerRepositoryTagV2> containerRepositoryTagV2 = new AtomicReference<>();
        Disposable disposable = dockerRegistryClient.listTagsV2(repo).subscribe(containerRepositoryTagV2::set);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.get().getTags();

        System.out.println("Page count = " + repositoryTags.size());
        assertThat(repositoryTags.size()).isGreaterThan(1);
        RepositoryTagV2 firstTag = repositoryTags.get(0);
        System.out.println(String.format("%s / %,d bytes / %s",
                firstTag.getName(), firstTag.getFullSize(), firstTag.getLastUpdated()));

        disposable.dispose();
    }

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void listTagsV2_pageFull() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

//        repo = "tomcat";
        repo = "library/tomcat";
        int pageSize = 5;
        AtomicReference<ContainerRepositoryTagV2> containerRepositoryTagV2 = new AtomicReference<>();
        Disposable disposable = dockerRegistryClient.listTagsV2(repo, 1, pageSize).subscribe(containerRepositoryTagV2::set);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.get().getTags();
        assertThat(containerRepositoryTagV2.get().getPreviousUrl()).isNull();
        assertThat(containerRepositoryTagV2.get().getNextUrl()).isNotNull();
        assertThat(containerRepositoryTagV2.get().getTotalCount()).isGreaterThan(pageSize);

        long end = System.currentTimeMillis();
        System.out.println("Reponse en : " + (end - start) + " ms");
        System.out.println("Page count = " + repositoryTags.size());
        System.out.println("Total count = " + containerRepositoryTagV2.get().getTotalCount());
        assertThat(repositoryTags.size()).isGreaterThan(1);
        RepositoryTagV2 firstTag = repositoryTags.get(0);
        System.out.println(String.format("%s / %,d bytes / %s",
                firstTag.getName(), firstTag.getFullSize(), firstTag.getLastUpdated()));

        disposable.dispose();
    }

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void listTagsV2_lastPage() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

        repo = "library/tomcat";
        int pageSize = 5;
        // 1st request
        AtomicReference<ContainerRepositoryTagV2> containerRepositoryTagV2 = new AtomicReference<>();
        Disposable disposable = dockerRegistryClient.listTagsV2(repo, 1, pageSize).subscribe(containerRepositoryTagV2::set);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.get().getTags();
        assertThat(containerRepositoryTagV2.get().getPreviousUrl()).isNull();
        assertThat(containerRepositoryTagV2.get().getNextUrl()).isNotNull();
        assertThat(containerRepositoryTagV2.get().getTotalCount()).isGreaterThan(pageSize);
        System.out.println("Next URL : " + containerRepositoryTagV2.get().getNextUrl());

        int lastPage = containerRepositoryTagV2.get().getTotalCount() / pageSize;
        if (containerRepositoryTagV2.get().getTotalCount() % pageSize != 0)
        {
            lastPage++;
        }

        // 2nd request
        AtomicReference<ContainerRepositoryTagV2> containerRepositoryTagV2_lastPage = new AtomicReference<>();
        Disposable disposable2 = dockerRegistryClient.listTagsV2(repo, lastPage, pageSize).subscribe(containerRepositoryTagV2_lastPage::set);
        repositoryTags = containerRepositoryTagV2_lastPage.get().getTags();
        assertThat(containerRepositoryTagV2_lastPage.get().getPreviousUrl()).isNotNull();
        assertThat(containerRepositoryTagV2_lastPage.get().getNextUrl()).isNull();
        System.out.println("Prev URL : " + containerRepositoryTagV2_lastPage.get().getPreviousUrl());

        RepositoryTagV2 lastTag = repositoryTags.get(repositoryTags.size() - 1);
        System.out.println(String.format("%s / %,d bytes / %s",
                lastTag.getName(), lastTag.getFullSize(), lastTag.getLastUpdated()));

        disposable.dispose();
        disposable2.dispose();
    }

}