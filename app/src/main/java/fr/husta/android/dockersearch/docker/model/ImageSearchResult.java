package fr.husta.android.dockersearch.docker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, setterVisibility = NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageSearchResult implements Parcelable
{

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_official")
    private boolean official;

    @JsonProperty("is_automated")
    private boolean automated;

    @JsonProperty("is_trusted")
    private boolean trusted;

    @JsonProperty("star_count")
    private int starCount;

    /**
     *
     */
    public ImageSearchResult()
    {
    }

    /**
     * @param name
     * @param description
     */
    public ImageSearchResult(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * @param name
     * @param description
     * @param official
     * @param starCount
     */
    public ImageSearchResult(String name, String description, boolean official, int starCount)
    {
        this.name = name;
        this.description = description;
        this.official = official;
        this.starCount = starCount;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return the official
     */
    public boolean isOfficial()
    {
        return official;
    }

    /**
     * @return the automated
     */
    public boolean isAutomated()
    {
        return automated;
    }

    public boolean isTrusted()
    {
        return trusted;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the starCount
     */
    public int getStarCount()
    {
        return starCount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final ImageSearchResult that = (ImageSearchResult) o;

//        return Objects.equals(this.description, that.description) &&
//                Objects.equals(this.official, that.official) &&
//                Objects.equals(this.automated, that.automated) &&
//                Objects.equals(this.trusted, that.trusted) &&
//                Objects.equals(this.name, that.name) &&
//                Objects.equals(this.starCount, that.starCount);
        return new EqualsBuilder()
                .appendSuper(super.equals(that))
                .append(this.name, that.name)
                .append(this.description, that.description)
                .append(this.official, that.official)
                .append(this.automated, that.automated)
                .append(this.trusted, that.trusted)
                .append(this.starCount, that.starCount)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("official", official)
                .append("automated", automated)
                .append("trusted", trusted)
                .append("starCount", starCount)
                .append("description", description)
                .toString();
//        return MoreObjects.toStringHelper(this)
//                .add("name", name)
//                .add("official", official)
//                .add("automated", automated)
//                .add("trusted", trusted)
//                .add("starCount", starCount)
//                .add("description", description)
//                .toString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte(this.official ? (byte) 1 : (byte) 0);
        dest.writeByte(this.automated ? (byte) 1 : (byte) 0);
        dest.writeByte(this.trusted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.starCount);
    }

    protected ImageSearchResult(Parcel in)
    {
        this.name = in.readString();
        this.description = in.readString();
        this.official = in.readByte() != 0;
        this.automated = in.readByte() != 0;
        this.trusted = in.readByte() != 0;
        this.starCount = in.readInt();
    }

    public static final Parcelable.Creator<ImageSearchResult> CREATOR = new Parcelable.Creator<ImageSearchResult>()
    {
        @Override
        public ImageSearchResult createFromParcel(Parcel source)
        {
            return new ImageSearchResult(source);
        }

        @Override
        public ImageSearchResult[] newArray(int size)
        {
            return new ImageSearchResult[size];
        }
    };
}