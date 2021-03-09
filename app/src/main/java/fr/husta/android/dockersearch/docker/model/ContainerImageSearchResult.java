package fr.husta.android.dockersearch.docker.model;

import java.util.ArrayList;
import java.util.List;

public class ContainerImageSearchResult
{

    private int numPages;

    private int page;

    private int pageSize;

    private int numResults;

    private List<ImageSearchResult> results = new ArrayList<>();

    public int getNumPages()
    {
        return numPages;
    }

    public void setNumPages(int numPages)
    {
        this.numPages = numPages;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getNumResults()
    {
        return numResults;
    }

    public void setNumResults(int numResults)
    {
        this.numResults = numResults;
    }

    public List<ImageSearchResult> getResults()
    {
        return results;
    }

    public void setResults(List<ImageSearchResult> results)
    {
        this.results = results;
    }
}
