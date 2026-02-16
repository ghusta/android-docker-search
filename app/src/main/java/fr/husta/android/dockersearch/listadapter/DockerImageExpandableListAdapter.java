package fr.husta.android.dockersearch.listadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CompactDecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsSession;
import fr.husta.android.dockersearch.AppConstants;
import fr.husta.android.dockersearch.MainActivity;
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

    private final Context context;
    private final List<ImageSearchResult> groupList;
    private final LayoutInflater inflater;

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
            viewHolder.name = convertView.findViewById(R.id.listitem_image_name);
            viewHolder.description = convertView.findViewById(R.id.listitem_image_desc);
            viewHolder.stars = convertView.findViewById(R.id.listitem_image_stars);
            viewHolder.official = convertView.findViewById(R.id.listitem_image_official);
        }

        ImageSearchResult item = getGroup(groupPosition);
        if (item != null)
        {
            viewHolder.name.setText(item.getName());
            viewHolder.description.setText(item.getDescription());
            String formattedStarCount = formatCompactNumber(item.getStarCount());
            viewHolder.stars.setText(formattedStarCount);
            if (item.isOfficial())
            {
                viewHolder.official.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.official.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    private String formatCompactNumber(int inputNumber)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {
            CompactDecimalFormat compactDecimalFormat = CompactDecimalFormat.getInstance(Locale.getDefault(), CompactDecimalFormat.CompactStyle.SHORT);
//            compactDecimalFormat.setMinimumSignificantDigits(1);
//            compactDecimalFormat.setMaximumSignificantDigits(3);
            return compactDecimalFormat.format(inputNumber);
        }
        else
        {
            // fallback to function
            return formatCompactNumberBeforeNougat(inputNumber, Locale.getDefault());
        }
    }

    public static String formatCompactNumberBeforeNougat(long count, Locale locale)
    {
        if (count < 1000)
        {
            return "" + count;
        }
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        DecimalFormat df = new DecimalFormat("0.#", symbols);
        String nb = df.format(count / Math.pow(1000, exp));
        return String.format("%s %c", nb, "kMGTPE".charAt(exp - 1));
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_docker_image_child_item, null);
        }

        final ImageSearchResult item = getGroup(groupPosition);

        Button btnTags = convertView.findViewById(R.id.btn_image_tags);
        btnTags.setOnClickListener(view ->
        {
            Intent starter = new Intent(context, TagListActivity.class);
            starter.putExtra(TagListActivity.DATA_IMG_NAME, item.getName());
            Activity activity = (Activity) parent.getContext();
            activity.startActivity(starter);
        });

        Button btnViewPage = convertView.findViewById(R.id.btn_image_hub_page);
        btnViewPage.setOnClickListener(view ->
        {
            Uri uri;
            if (item.isOfficial())
            {
                uri = Uri.parse(DOCKER_HUB_BASE_URL + "/_/" + item.getName());
            }
            else
            {
                uri = Uri.parse(DOCKER_HUB_BASE_URL + "/r/" + item.getName());
            }

            MainActivity hostActivity = (MainActivity) context;
            if (AppConstants.USE_CHROME_CUSTOM_TABS)
            {
                CustomTabsSession customTabsSession = null;
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder(customTabsSession);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    intentBuilder.setToolbarColor(parent.getResources().getColor(R.color.md_theme_primary, context.getTheme()));
                }
                else
                {
                    intentBuilder.setToolbarColor(parent.getResources().getColor(R.color.md_theme_primary));
                }
                intentBuilder.setShowTitle(true); // affiche title page au dessus URL
                intentBuilder.setShareState(CustomTabsIntent.SHARE_STATE_ON);

                // Setting a custom back button
                Bitmap iconArrow = BitmapFactory.decodeResource(hostActivity.getResources(),
                        R.drawable.ic_arrow_back);
                intentBuilder.setCloseButtonIcon(iconArrow);

                CustomTabsIntent customTabsIntent = intentBuilder.build();
                // Add your app as the referrer
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                {
                    customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                            Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + context.getPackageName()));
                }
                customTabsIntent.launchUrl(context, uri);
            }
            else
            {
                throw new UnsupportedOperationException("Not implemented");
            }
        });

        Button btnShare = convertView.findViewById(R.id.btn_image_share);
        btnShare.setOnClickListener(view ->
        {
            Uri uri;
            if (item.isOfficial())
            {
                uri = Uri.parse(DOCKER_HUB_BASE_URL + "/_/" + item.getName());
            }
            else
            {
                uri = Uri.parse(DOCKER_HUB_BASE_URL + "/r/" + item.getName());
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            final String subject = "Docker Image : " + item.getName();
            String body = "";
            if (item.getDescription() != null && item.getDescription().trim().length() != 0)
            {
                body += item.getDescription() + "\n\n";
            }
            body += uri.toString();
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            shareIntent.putExtra(Intent.EXTRA_TEXT, body);
            Activity activity = (Activity) parent.getContext();
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share)));
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

}
