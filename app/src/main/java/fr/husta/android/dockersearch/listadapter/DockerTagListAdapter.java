package fr.husta.android.dockersearch.listadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.husta.android.dockersearch.R;
import fr.husta.android.dockersearch.docker.model.RepositoryTag;
import fr.husta.android.dockersearch.view.DockerTagViewHolder;

public class DockerTagListAdapter extends ArrayAdapter<RepositoryTag>
{

    /**
     * Constructor.
     *
     * @param context
     * @param objects
     */
    public DockerTagListAdapter(Context context, List<RepositoryTag> objects)
    {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_docker_tag_item,
                    parent, false);
        }

        DockerTagViewHolder viewHolder = (DockerTagViewHolder) convertView.getTag();
        if (viewHolder == null)
        {
            viewHolder = new DockerTagViewHolder();
            viewHolder.setName((TextView) convertView.findViewById(R.id.listitem_tag_name));
        }

        RepositoryTag item = getItem(position);
        if (item != null)
        {
            viewHolder.getName().setText(item.getName());
        }

        return convertView;
    }

}
