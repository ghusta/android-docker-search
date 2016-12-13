package fr.husta.android.dockersearch.view;

import android.widget.TextView;

public class DockerTagViewHolder
{

    private TextView layer;
    private TextView name;

    public TextView getLayer()
    {
        return layer;
    }

    public void setLayer(TextView layer)
    {
        this.layer = layer;
    }

    public TextView getName()
    {
        return name;
    }

    public void setName(TextView name)
    {
        this.name = name;
    }
}
