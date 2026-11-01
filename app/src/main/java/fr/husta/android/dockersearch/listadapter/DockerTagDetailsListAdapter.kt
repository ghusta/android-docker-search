package fr.husta.android.dockersearch.listadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import fr.husta.android.dockersearch.R
import fr.husta.android.dockersearch.docker.model.ImageVariantByTag
import fr.husta.android.dockersearch.view.DockerTagDetailViewHolder

class DockerTagDetailsListAdapter(
    context: Context,
    objects: MutableList<ImageVariantByTag>
) : ArrayAdapter<ImageVariantByTag>(context, 0, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.list_docker_tag_detail_item,
                parent, false
            )
        }

        var viewHolder = convertView.tag as DockerTagDetailViewHolder?
        if (viewHolder == null) {
            viewHolder = DockerTagDetailViewHolder(
                convertView.findViewById(R.id.listitem_tag_variant_digest),
                convertView.findViewById(R.id.listitem_tag_variant_desc)
            )
        }

        val item = getItem(position)
        if (item != null) {
            var dig = item.digest
            if (item.digest.startsWith("sha256:")) {
                dig = item.digest.substring("sha256:".length)
            }
            if (dig.length > 12) {
                viewHolder.digest.text = dig.substring(0, 12)
            } else {
                viewHolder.digest.text = dig
            }

            val os = if (item.os.isNullOrEmpty()) "--" else item.os
            val arch = if (item.architecture.isNullOrEmpty()) "--" else item.architecture
            val variant = item.variant?.takeIf { it.isNotBlank() }?.let { "/$it" } ?: ""

            viewHolder.description.text = "$os/$arch$variant"
        }

        return convertView
    }
}
