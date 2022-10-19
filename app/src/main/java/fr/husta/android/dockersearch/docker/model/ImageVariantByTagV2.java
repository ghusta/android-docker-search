package fr.husta.android.dockersearch.docker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
public class ImageVariantByTagV2 implements Parcelable
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

    protected ImageVariantByTagV2(Parcel in)
    {
        digest = in.readString();
        os = in.readString();
        osVersion = in.readString();
        architecture = in.readString();
        variant = in.readString();
//        if (in.readByte() == 0)
//        {
//            size = null;
//        }
//        else
//        {
//            size = in.readLong();
//        }
        // status = in.readString();
    }

    public static final Creator<ImageVariantByTagV2> CREATOR = new Creator<ImageVariantByTagV2>()
    {
        @Override
        public ImageVariantByTagV2 createFromParcel(Parcel in)
        {
            return new ImageVariantByTagV2(in);
        }

        @Override
        public ImageVariantByTagV2[] newArray(int size)
        {
            return new ImageVariantByTagV2[size];
        }
    };

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

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.digest);
        dest.writeString(this.os);
        dest.writeString(this.osVersion);
        dest.writeString(this.architecture);
        dest.writeString(this.variant);
        // dest.writeLong(this.size);
    }
}
