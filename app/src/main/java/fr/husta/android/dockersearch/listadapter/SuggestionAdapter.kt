package fr.husta.android.dockersearch.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.husta.android.dockersearch.R
import java.util.Locale

class SuggestionAdapter(
    dataSet: List<String>,
    private val onItemClickListener: (String) -> Unit = {},
    private val onItemLongClickListener: (String) -> Unit = {}
) : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    private val suggestions: MutableList<String> = dataSet.toMutableList()
    private var suggestionsFiltered: List<String> = dataSet.toMutableList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.suggestionTextView.text = suggestionsFiltered[position]

        holder.setOnClickListener(
            holder.suggestionTextView.getText().toString(),
            onItemClickListener
        )
        holder.setOnLongClickListener(
            holder.suggestionTextView.getText().toString(),
            onItemLongClickListener
        )
    }

    override fun getItemCount() = suggestionsFiltered.size

    fun clear() {
        suggestions.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: String) {
        suggestions.add(0, item)
        notifyItemInserted(0)
    }

    fun removeItem(item: String) {
        val index = suggestions.indexOf(item)
        if (index != -1) {
            suggestions.remove(item)
            notifyItemRemoved(index)
        }
    }

    fun filter(text: String) {
        if (text.isEmpty()) {
            suggestionsFiltered = suggestions.toList()
        } else {
            val lowerCaseQuery = text.lowercase(Locale.getDefault())
            suggestionsFiltered = suggestions.filter {
                it.lowercase().contains(lowerCaseQuery)
            }
        }
        notifyDataSetChanged() // Notifie l'adapter que les données ont changé
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val suggestionTextView: TextView = itemView.findViewById(R.id.suggestion_text_view)

        fun setOnClickListener(text: String, listener: (String) -> Unit) =
            this.itemView.setOnClickListener { listener(text) }

        fun setOnLongClickListener(text: String, listener: (String) -> Unit) =
            this.itemView.setOnLongClickListener {
                listener(text)
                true
            }

    }

}