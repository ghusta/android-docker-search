package fr.husta.android.dockersearch.docker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerImageSearchResult
{

    @JsonProperty("num_pages")
    private int numPages;

    @JsonProperty("page")
    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("num_results")
    private int numResults;

    @JsonProperty("results")
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
