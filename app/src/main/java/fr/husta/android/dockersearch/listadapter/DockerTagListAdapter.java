package fr.husta.android.dockersearch.listadapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.joda.time.DateTime;

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
            viewHolder.setName(convertView.findViewById(R.id.listitem_tag_name));
            viewHolder.setSize(convertView.findViewById(R.id.listitem_tag_size));
            viewHolder.setLastUpdated(convertView.findViewById(R.id.listitem_tag_last_updated));
        }

        RepositoryTagV2 item = getItem(position);
        if (item != null)
        {
            viewHolder.getName().setText(item.getName());
            if (item.getFullSize() != null)
            {
                viewHolder.getSize().setText(
                        ByteSizeFormatterUtils.Android.formatShortSize(convertView.getContext(), item.getFullSize()));
            }
            else
            {
                viewHolder.getSize().setText("-");
            }
            if (item.getLastUpdated() != null)
            {
                long elapsedMillis = DateTime.now().getMillis() - item.getLastUpdated().getMillis();
                viewHolder.getLastUpdated().setText(
                        DateUtils.getRelativeTimeSpanString(item.getLastUpdated().getMillis()));
            }
            else
            {
                viewHolder.getLastUpdated().setText("-");
            }
        }

        return convertView;
    }

}
