package fr.husta.android.dockersearch.view;

import android.widget.TextView;

public class DockerTagDetailViewHolder
{

    private TextView digest;
    private TextView description;

    public TextView getDigest()
    {
        return digest;
    }

    public void setDigest(TextView digest)
    {
        this.digest = digest;
    }

    public TextView getDescription()
    {
        return description;
    }

    public void setDescription(TextView description)
    {
        this.description = description;
    }
}
