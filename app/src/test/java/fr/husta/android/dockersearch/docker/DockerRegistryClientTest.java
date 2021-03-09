package fr.husta.android.dockersearch.docker;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import fr.husta.android.dockersearch.docker.model.ContainerImageSearchResult;
import fr.husta.android.dockersearch.docker.model.ContainerRepositoryTagV2;
import fr.husta.android.dockersearch.docker.model.ImageSearchResult;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import fr.husta.android.dockersearch.docker.model.RepositoryTagV2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.fail;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

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
    public void listTagsV2() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

//        repo = "tomcat";
        repo = "library/tomcat";
        ContainerRepositoryTagV2 containerRepositoryTagV2 = dockerRegistryClient.listTagsV2(repo);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.getTags();

        long end = System.currentTimeMillis();
        System.out.println("Reponse en : " + (end - start) + " ms");
        System.out.println("Page count = " + repositoryTags.size());
        System.out.println("Total count = " + containerRepositoryTagV2.getTotalCount());
        assertThat(repositoryTags.size()).isGreaterThan(1);
        RepositoryTagV2 firstTag = repositoryTags.get(0);
        System.out.println(String.format("%s / %,d bytes / %s",
                firstTag.getName(), firstTag.getFullSize(), firstTag.getLastUpdated()));
    }

    @Test(timeout = _30_SECONDS_IN_MILLIS)
    public void listTagsV2_Jenkins() throws Exception
    {
        long start = System.currentTimeMillis();
        DockerRegistryClient dockerRegistryClient = new DockerRegistryClient();
        String repo;

//        repo = "tomcat";
        repo = "library/jenkins";
        ContainerRepositoryTagV2 containerRepositoryTagV2 = dockerRegistryClient.listTagsV2(repo);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.getTags();

        System.out.println("Page count = " + repositoryTags.size());
        assertThat(repositoryTags.size()).isGreaterThan(1);
        RepositoryTagV2 firstTag = repositoryTags.get(0);
        System.out.println(String.format("%s / %,d bytes / %s",
                firstTag.getName(), firstTag.getFullSize(), firstTag.getLastUpdated()));
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
        ContainerRepositoryTagV2 containerRepositoryTagV2 = dockerRegistryClient.listTagsV2(repo, 1, pageSize);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.getTags();
        assertThat(containerRepositoryTagV2.getPreviousUrl()).isNull();
        assertThat(containerRepositoryTagV2.getNextUrl()).isNotNull();
        assertThat(containerRepositoryTagV2.getTotalCount()).isGreaterThan(pageSize);

        long end = System.currentTimeMillis();
        System.out.println("Reponse en : " + (end - start) + " ms");
        System.out.println("Page count = " + repositoryTags.size());
        System.out.println("Total count = " + containerRepositoryTagV2.getTotalCount());
        assertThat(repositoryTags.size()).isGreaterThan(1);
        RepositoryTagV2 firstTag = repositoryTags.get(0);
        System.out.println(String.format("%s / %,d bytes / %s",
                firstTag.getName(), firstTag.getFullSize(), firstTag.getLastUpdated()));
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
        ContainerRepositoryTagV2 containerRepositoryTagV2 = dockerRegistryClient.listTagsV2(repo, 1, pageSize);
        List<RepositoryTagV2> repositoryTags = containerRepositoryTagV2.getTags();
        assertThat(containerRepositoryTagV2.getPreviousUrl()).isNull();
        assertThat(containerRepositoryTagV2.getNextUrl()).isNotNull();
        assertThat(containerRepositoryTagV2.getTotalCount()).isGreaterThan(pageSize);
        System.out.println("Next URL : " + containerRepositoryTagV2.getNextUrl());

        int lastPage = containerRepositoryTagV2.getTotalCount() / pageSize;
        if (containerRepositoryTagV2.getTotalCount() % pageSize != 0)
        {
            lastPage++;
        }

        // 2nd request
        ContainerRepositoryTagV2 containerRepositoryTagV2_lastPage = dockerRegistryClient.listTagsV2(repo, lastPage, pageSize);
        repositoryTags = containerRepositoryTagV2_lastPage.getTags();
        assertThat(containerRepositoryTagV2_lastPage.getPreviousUrl()).isNotNull();
        assertThat(containerRepositoryTagV2_lastPage.getNextUrl()).isNull();
        System.out.println("Prev URL : " + containerRepositoryTagV2_lastPage.getPreviousUrl());

        RepositoryTagV2 lastTag = repositoryTags.get(repositoryTags.size() - 1);
        System.out.println(String.format("%s / %,d bytes / %s",
                lastTag.getName(), lastTag.getFullSize(), lastTag.getLastUpdated()));
    }

}