package fr.husta.android.dockersearch.listadapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import fr.husta.android.dockersearch.R;
import fr.husta.android.dockersearch.docker.model.RepositoryTagV2;
import fr.husta.android.dockersearch.utils.format.unit.ByteSizeFormatterUtils;
import fr.husta.android.dockersearch.view.DockerTagViewHolder;

public class DockerTagListAdapter extends ArrayAdapter<RepositoryTagV2>
{

    /**
     * Constructor.
     *
     * @param context
     * @param objects
     */
    public DockerTagListAdapter(Context context, List<RepositoryTagV2> objects)
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
            viewHolder.name = convertView.findViewById(R.id.listitem_tag_name);
            viewHolder.size = convertView.findViewById(R.id.listitem_tag_size);
            viewHolder.lastUpdated = convertView.findViewById(R.id.listitem_tag_last_updated);
        }

        RepositoryTagV2 item = getItem(position);
        if (item != null)
        {
            viewHolder.name.setText(item.getName());
            if (item.getFullSize() != null)
            {
                viewHolder.size.setText(
                        ByteSizeFormatterUtils.Android.formatShortSize(convertView.getContext(), item.getFullSize()));
            }
            else
            {
                viewHolder.size.setText("-");
            }
            if (item.getLastUpdated() != null)
            {
                long elapsedMillis = System.currentTimeMillis() - item.getLastUpdated().toInstant().toEpochMilli();
                viewHolder.lastUpdated.setText(
                        DateUtils.getRelativeTimeSpanString(item.getLastUpdated().toInstant().toEpochMilli()));
            }
            else
            {
                viewHolder.lastUpdated.setText("-");
            }
        }

        return convertView;
    }

}
