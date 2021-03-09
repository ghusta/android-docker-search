package fr.husta.android.dockersearch.docker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import org.joda.time.DateTime;

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
}
