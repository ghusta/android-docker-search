package fr.husta.android.dockersearch.view;

import android.widget.ImageView;
import android.widget.TextView;

public class DockerImageViewHolder
{

    private TextView name;
    private TextView description;
    private TextView stars;
    private ImageView official;

    public TextView getName()
    {
        return name;
    }

    public void setName(TextView name)
    {
        this.name = name;
    }

    public TextView getDescription()
    {
        return description;
    }

    public void setDescription(TextView description)
    {
        this.description = description;
    }

    public TextView getStars()
    {
        return stars;
    }

    public void setStars(TextView stars)
    {
        this.stars = stars;
    }

    public ImageView getOfficial()
    {
        return official;
    }

    public void setOfficial(ImageView official)
    {
        this.official = official;
    }
}
