package fr.husta.android.dockersearch.listadapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import fr.husta.android.dockersearch.R
import fr.husta.android.dockersearch.docker.model.RepositoryTag
import fr.husta.android.dockersearch.utils.format.unit.ByteSizeFormatterUtils.Android.formatShortSize
import fr.husta.android.dockersearch.view.DockerTagViewHolder

class DockerTagListAdapter(
    context: Context,
    objects: MutableList<RepositoryTag>
) : ArrayAdapter<RepositoryTag>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.list_docker_tag_item,
                parent, false
            )
        }

        var viewHolder = convertView.tag as DockerTagViewHolder?
        if (viewHolder == null) {
            viewHolder = DockerTagViewHolder(
                convertView.findViewById(R.id.listitem_tag_name),
                convertView.findViewById(R.id.listitem_tag_size),
                convertView.findViewById(R.id.listitem_tag_last_updated)
            )
        }

        val item = getItem(position)
        if (item != null) {
            viewHolder.name.text = item.name
            if (item.fullSize != null) {
                viewHolder.size.setText(
                    formatShortSize(convertView.context, item.fullSize)
                )
            } else {
                viewHolder.size.text = "-"
            }
            if (item.lastUpdated != null) {
                viewHolder.lastUpdated.text = DateUtils.getRelativeTimeSpanString(
                    item.lastUpdated.toEpochMilli()
                )
            } else {
                viewHolder.lastUpdated.text = "-"
            }
        }

        return convertView
    }
}
