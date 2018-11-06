package fr.husta.android.dockersearch.docker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class ImageVariantByTagV2
{

    /**
     * Image digest, should start with "sha256:..."
     */
    private String digest;

    /**
     * For example : "linux"
     */
    private String os;

    private String osVersion;

    /**
     * For example : "amd64" or "386" or "arm"
     */
    private String architecture;

    /**
     * For example : "v7" or null
     */
    private String variant;

    /**
     * Compressed size in bytes.
     */
    private Long size;

    /**
     * For example : "active" or "inactive"
     */
    private String status;

    private ZonedDateTime lastPushed;

    public ImageVariantByTagV2()
    {
    }

    public String getDigest()
    {
        return digest;
    }

    public void setDigest(String digest)
    {
        this.digest = digest;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getOsVersion()
    {
        return osVersion;
    }

    public void setOsVersion(String osVersion)
    {
        this.osVersion = osVersion;
    }

    public String getArchitecture()
    {
        return architecture;
    }

    public void setArchitecture(String architecture)
    {
        this.architecture = architecture;
    }

    public String getVariant()
    {
        return variant;
    }

    public void setVariant(String variant)
    {
        this.variant = variant;
    }

    public Long getSize()
    {
        return size;
    }

    public void setSize(Long size)
    {
        this.size = size;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public ZonedDateTime getLastPushed()
    {
        return lastPushed;
    }

    public void setLastPushed(ZonedDateTime lastPushed)
    {
        this.lastPushed = lastPushed;
    }
}
