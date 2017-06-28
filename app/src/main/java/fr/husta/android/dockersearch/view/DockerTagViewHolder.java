package fr.husta.android.dockersearch.view;

import android.widget.TextView;

public class DockerTagViewHolder
{

    private TextView name;
    private TextView size;
    private TextView lastUpdated;

    public TextView getName()
    {
        return name;
    }

    public void setName(TextView name)
    {
        this.name = name;
    }

    public TextView getSize()
    {
        return size;
    }

    public void setSize(TextView size)
    {
        this.size = size;
    }

    public TextView getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(TextView lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }
}
