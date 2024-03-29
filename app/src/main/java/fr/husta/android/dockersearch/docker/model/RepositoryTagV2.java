package fr.husta.android.dockersearch.docker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
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
    private ZonedDateTime lastUpdated;

    @JsonProperty("images")
    private List<ImageVariantByTagV2> imageVariants = new ArrayList<>();

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

    public ZonedDateTime getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public List<ImageVariantByTagV2> getImageVariants()
    {
        return imageVariants;
    }

    public void setImageVariants(List<ImageVariantByTagV2> imageVariants)
    {
        this.imageVariants = imageVariants;
    }
}
