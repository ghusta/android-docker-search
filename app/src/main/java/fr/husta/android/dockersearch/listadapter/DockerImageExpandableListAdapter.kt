package fr.husta.android.dockersearch.listadapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.CompactDecimalFormat
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.net.toUri
import fr.husta.android.dockersearch.AppConstants
import fr.husta.android.dockersearch.MainActivity
import fr.husta.android.dockersearch.R
import fr.husta.android.dockersearch.TagListActivity
import fr.husta.android.dockersearch.docker.model.ImageItem
import fr.husta.android.dockersearch.view.DockerImageViewHolder
import java.util.Locale

/**
 * Always contains 1 unique child.
 */
class DockerImageExpandableListAdapter(
    private val context: Context,
    val groupList: ArrayList<ImageItem>
) : BaseExpandableListAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getGroupCount(): Int {
        return groupList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): ImageItem {
        return groupList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return String.format("#%s in #%s", childPosition, groupPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_docker_image_group_item, null)
        }

        var viewHolder = convertView.tag as DockerImageViewHolder?
        if (viewHolder == null) {
            viewHolder = DockerImageViewHolder(
                convertView.findViewById(R.id.listitem_image_name),
                convertView.findViewById(R.id.listitem_image_desc),
                convertView.findViewById(R.id.listitem_image_stars),
                convertView.findViewById(R.id.listitem_image_official)
            )
        }

        val item = getGroup(groupPosition)
        viewHolder.name.text = item.name
        viewHolder.description.setText(item.description)
        val formattedStarCount = formatCompactNumber(item.starCount)
        viewHolder.stars.setText(formattedStarCount)
        if (item.isOfficial) {
            viewHolder.official.setVisibility(View.VISIBLE)
        } else {
            viewHolder.official.setVisibility(View.INVISIBLE)
        }

        return convertView
    }

    private fun formatCompactNumber(inputNumber: Int): String? {
        val compactDecimalFormat = CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        )
        return compactDecimalFormat.format(inputNumber.toLong())
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_docker_image_child_item, null)
        }

        val item = getGroup(groupPosition)

        val btnTags = convertView.findViewById<Button>(R.id.btn_image_tags)
        btnTags.setOnClickListener {
            val starter = Intent(context, TagListActivity::class.java)
            starter.putExtra(TagListActivity.DATA_IMG_NAME, item.name)
            val activity = parent.context as Activity
            activity.startActivity(starter)
        }

        val btnViewPage = convertView.findViewById<Button>(R.id.btn_image_hub_page)
        btnViewPage.setOnClickListener {
            val uri: Uri = if (item.isOfficial) {
                (MainActivity.DOCKER_HUB_BASE_URL + "/_/" + item.name).toUri()
            } else {
                (MainActivity.DOCKER_HUB_BASE_URL + "/r/" + item.name).toUri()
            }

            val hostActivity = context as MainActivity
            if (AppConstants.USE_CHROME_CUSTOM_TABS) {
                val customTabsSession: CustomTabsSession? = null
                val intentBuilder = CustomTabsIntent.Builder(customTabsSession)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intentBuilder.setToolbarColor(
                        parent.resources.getColor(R.color.md_theme_primary, context.getTheme())
                    )
                } else {
                    intentBuilder.setToolbarColor(
                        parent.resources.getColor(R.color.md_theme_primary)
                    )
                }
                intentBuilder.setShowTitle(true) // affiche title page au dessus URL
                intentBuilder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

                // Setting a custom back button
                val iconArrow = BitmapFactory.decodeResource(
                    hostActivity.resources,
                    R.drawable.ic_arrow_back
                )
                intentBuilder.setCloseButtonIcon(iconArrow)

                val customTabsIntent = intentBuilder.build()
                // Add your app as the referrer
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    customTabsIntent.intent.putExtra(
                        Intent.EXTRA_REFERRER,
                        (Intent.URI_ANDROID_APP_SCHEME.toString() + "//" + context.packageName).toUri()
                    )
                }
                customTabsIntent.launchUrl(context, uri)
            } else {
                throw UnsupportedOperationException("Not implemented")
            }
        }

        val btnShare = convertView.findViewById<Button>(R.id.btn_image_share)
        btnShare.setOnClickListener {
            val uri: Uri = if (item.isOfficial) {
                (MainActivity.DOCKER_HUB_BASE_URL + "/_/" + item.name).toUri()
            } else {
                (MainActivity.DOCKER_HUB_BASE_URL + "/r/" + item.name).toUri()
            }

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            val subject = "Docker Image : " + item.name
            var body = ""
            if (item.description != null && item.description.trim { it <= ' ' }.isNotEmpty()) {
                body += item.description + "\n\n"
            }
            body += uri.toString()
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, body)
            val activity = parent.context as Activity
            activity.startActivity(
                Intent.createChooser(
                    shareIntent,
                    activity.getString(R.string.share)
                )
            )
        }

        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

}
