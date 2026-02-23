package fr.husta.android.dockersearch.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import fr.husta.android.dockersearch.R;
import fr.husta.android.dockersearch.docker.model.ImageVariantByTagV2;
import fr.husta.android.dockersearch.view.DockerTagDetailViewHolder;

public class DockerTagDetailsListAdapter extends ArrayAdapter<ImageVariantByTagV2>
{

    /**
     * Constructor.
     *
     * @param context
     * @param objects
     */
    public DockerTagDetailsListAdapter(Context context, List<ImageVariantByTagV2> objects)
    {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_docker_tag_detail_item,
                    parent, false);
        }

        DockerTagDetailViewHolder viewHolder = (DockerTagDetailViewHolder) convertView.getTag();
        if (viewHolder == null)
        {
            viewHolder = new DockerTagDetailViewHolder(
                    convertView.findViewById(R.id.listitem_tag_variant_digest),
                    convertView.findViewById(R.id.listitem_tag_variant_desc));
        }

        ImageVariantByTagV2 item = getItem(position);
        if (item != null)
        {
            if (item.getDigest() != null)
            {
                String dig = item.getDigest();
                if (item.getDigest().startsWith("sha256:")){
                    dig = item.getDigest().substring("sha256:".length());
                }
                if (dig.length() > 12)
                {
                    viewHolder.getDigest().setText(dig.substring(0, 12));
                }
                else
                {
                    viewHolder.getDigest().setText(dig);
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s/%s",
                    StringUtils.defaultIfEmpty(item.getOs(), "--"),
                    StringUtils.defaultIfEmpty(item.getArchitecture(), "--")));
            if (StringUtils.isNotBlank(item.getVariant()))
            {
                sb.append("/").append(item.getVariant());
            }
            viewHolder.getDescription().setText(sb.toString());
        }

        return convertView;
    }

}
