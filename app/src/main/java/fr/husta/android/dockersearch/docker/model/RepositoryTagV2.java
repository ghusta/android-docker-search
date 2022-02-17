package fr.husta.android.dockersearch.docker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class RepositoryTagV2
{

    private String name;

    /**
     * Size in bytes.
     */
    private Long fullSize;

    /**
     * Original value in ISO-8601.
     * Ex : "2017-06-09T20:52:13.939283Z".
     */
    private DateTime lastUpdated;

    // java.time.Instant, java.time.* -> from Android O

    @JsonProperty("images")
    private List<ImageVariantByTagV2> images = new ArrayList<>();

    public RepositoryTagV2()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getFullSize()
    {
        return fullSize;
    }

    public void setFullSize(Long fullSize)
    {
        this.fullSize = fullSize;
    }

    public DateTime getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public List<ImageVariantByTagV2> getImages()
    {
        return images;
    }

    public void setImages(List<ImageVariantByTagV2> images)
    {
        this.images = images;
    }
}
