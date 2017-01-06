package fr.husta.android.dockersearch.listadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.husta.android.dockersearch.R;
import fr.husta.android.dockersearch.docker.model.ImageSearchResult;
import fr.husta.android.dockersearch.view.DockerImageViewHolder;

@Deprecated
public class DockerImageListAdapter
        extends ArrayAdapter<ImageSearchResult>
{

    private List<ImageSearchResult> listReference;

    /**
     * Constructor.
     *
     * @param context
     * @param objects
     */
    public DockerImageListAdapter(Context context, List<ImageSearchResult> objects)
    {
        super(context, 0, objects);
        // keep reference
        listReference = objects;
    }

    public ArrayList<ImageSearchResult> getList()
    {
        return (ArrayList<ImageSearchResult>) listReference;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_docker_image_item,
//                    parent, false);
        }

        DockerImageViewHolder viewHolder = (DockerImageViewHolder) convertView.getTag();
        if (viewHolder == null)
        {
            viewHolder = new DockerImageViewHolder();
            viewHolder.setName((TextView) convertView.findViewById(R.id.listitem_image_name));
            viewHolder.setDescription((TextView) convertView.findViewById(R.id.listitem_image_desc));
            viewHolder.setStars((TextView) convertView.findViewById(R.id.listitem_image_stars));
            viewHolder.setOfficial((ImageView) convertView.findViewById(R.id.listitem_image_official));
        }

        ImageSearchResult item = getItem(position);
        if (item != null)
        {
            viewHolder.getName().setText(item.getName());
            viewHolder.getDescription().setText(item.getDescription());
            viewHolder.getStars().setText(String.valueOf(item.getStarCount()));
            if (item.isOfficial())
            {
                // viewHolder.getOfficial().setText(R.string.Official);
                viewHolder.getOfficial().setVisibility(View.VISIBLE);
            } else
            {
                // viewHolder.getOfficial().setText("");
                viewHolder.getOfficial().setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

}
