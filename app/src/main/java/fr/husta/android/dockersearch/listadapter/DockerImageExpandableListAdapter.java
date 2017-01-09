package fr.husta.android.dockersearch.listadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.husta.android.dockersearch.ImageWebViewActivity;
import fr.husta.android.dockersearch.R;
import fr.husta.android.dockersearch.TagListActivity;
import fr.husta.android.dockersearch.docker.model.ImageSearchResult;
import fr.husta.android.dockersearch.view.DockerImageViewHolder;

import static fr.husta.android.dockersearch.MainActivity.DOCKER_HUB_BASE_URL;

/**
 * Always contains 1 unique child.
 */
public class DockerImageExpandableListAdapter
        extends BaseExpandableListAdapter
{

    private Context context;
    private List<ImageSearchResult> groupList;
    private LayoutInflater inflater;

    public DockerImageExpandableListAdapter(Context context, List<ImageSearchResult> groupList)
    {
        this.context = context;
        this.groupList = groupList;

        this.inflater = LayoutInflater.from(context);
    }

    public ArrayList<ImageSearchResult> getGroupList()
    {
        return (ArrayList<ImageSearchResult>) groupList;
    }

    @Override
    public int getGroupCount()
    {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return 1;
    }

    @Override
    public ImageSearchResult getGroup(int groupPosition)
    {
        return groupList.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition)
    {
        return String.format("#%s in #%s", childPosition, groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_docker_image_group_item, null);
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

        ImageSearchResult item = getGroup(groupPosition);
        if (item != null)
        {
            viewHolder.getName().setText(item.getName());
            viewHolder.getDescription().setText(item.getDescription());
            viewHolder.getStars().setText(String.valueOf(item.getStarCount()));
            if (item.isOfficial())
            {
                viewHolder.getOfficial().setVisibility(View.VISIBLE);
            } else
            {
                viewHolder.getOfficial().setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_docker_image_child_item, null);
        }

        final ImageSearchResult item = getGroup(groupPosition);

        ImageButton btnTags = (ImageButton) convertView.findViewById(R.id.btn_image_tags);
        btnTags.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent starter = new Intent(context, TagListActivity.class);
                starter.putExtra(TagListActivity.DATA_IMG_NAME, item.getName());
                Activity activity = (Activity) parent.getContext();
                activity.startActivity(starter);
            }
        });

        ImageButton btnViewPage = (ImageButton) convertView.findViewById(R.id.btn_image_hub_page);
        btnViewPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri;
                if (item.isOfficial())
                {
                    uri = Uri.parse(DOCKER_HUB_BASE_URL + "/_/" + item.getName());
                } else
                {
                    uri = Uri.parse(DOCKER_HUB_BASE_URL + "/r/" + item.getName());
                }

                Intent starter = new Intent(context, ImageWebViewActivity.class);
                starter.setData(uri);
                Activity activity = (Activity) parent.getContext();
                activity.startActivity(starter);

                // Launch image page in browser
                // openUrlInBrowser(uri);

            }
        });

        ImageButton btnShare = (ImageButton) convertView.findViewById(R.id.btn_image_share);
        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri;
                if (item.isOfficial())
                {
                    uri = Uri.parse(DOCKER_HUB_BASE_URL + "/_/" + item.getName());
                } else
                {
                    uri = Uri.parse(DOCKER_HUB_BASE_URL + "/r/" + item.getName());
                }

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                final String subject = "Docker Image : " + item.getName();
                final String body = uri.toString();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                Activity activity = (Activity) parent.getContext();
                activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share)));
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

}
