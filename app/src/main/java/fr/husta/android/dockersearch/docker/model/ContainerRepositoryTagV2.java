package fr.husta.android.dockersearch.docker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ContainerRepositoryTagV2
{

    @JsonProperty("count")
    private int totalCount;

    @JsonProperty("next")
    private String nextUrl;

    @JsonProperty("previous")
    private String previousUrl;

    @JsonProperty("results")
    private List<RepositoryTagV2> tags = new ArrayList<>();

    public int getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }

    public String getNextUrl()
    {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl)
    {
        this.nextUrl = nextUrl;
    }

    public String getPreviousUrl()
    {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl)
    {
        this.previousUrl = previousUrl;
    }

    public List<RepositoryTagV2> getTags()
    {
        return tags;
    }

    public void setTags(List<RepositoryTagV2> tags)
    {
        this.tags = tags;
    }
}
