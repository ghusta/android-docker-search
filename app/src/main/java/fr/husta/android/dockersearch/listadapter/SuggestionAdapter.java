package fr.husta.android.dockersearch.listadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import fr.husta.android.dockersearch.R;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder>
{

    private List<String> localDataSet;
    //    private AdapterView.OnItemClickListener onItemClickListener;
    @Nullable
    private final OnItemClickListener onItemClickListener;
    @Nullable
    private final OnItemLongClickListener onItemLongClickListener;

    @FunctionalInterface
    public interface OnItemClickListener
    {
        void onItemClick(String text);
    }

    @FunctionalInterface
    public interface OnItemLongClickListener
    {
        void onItemLongClick(String text);
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        private final View itemView;
        private final TextView suggestionTextView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.itemView = itemView;
            this.suggestionTextView = itemView.findViewById(R.id.suggestion_text_view);
            // Define click listener for the ViewHolder's View
//            this.suggestionTextView.setOnClickListener(v ->
//                    Log.d("ADAPTER", "onClick: " + suggestionTextView.getText().toString()));
        }

        public View getItemView()
        {
            return itemView;
        }

        public TextView getSuggestionTextView()
        {
            return suggestionTextView;
        }

        public void setOnClickListener(final String text, final OnItemClickListener listener)
        {
            itemView.setOnClickListener(v -> listener.onItemClick(text));
        }

        public void setOnLongClickListener(final String text, final OnItemLongClickListener listener)
        {
            itemView.setOnLongClickListener(v ->
            {
                listener.onItemLongClick(text);
                return true;
            });
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView
     */
    public SuggestionAdapter(List<String> dataSet,
                             @Nullable OnItemClickListener onItemClickListener,
                             @Nullable OnItemLongClickListener onItemLongClickListener)
    {
        localDataSet = new ArrayList<>(dataSet);
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.suggestion_row_item, viewGroup, false);
//        view.setOnClickListener(  onItemClickListener   );
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position)
    {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getSuggestionTextView().setText(localDataSet.get(position));
//        onItemClickListener.onItemClick(null, viewHolder.getTextView(), position, position);
        viewHolder.setOnClickListener(viewHolder.getSuggestionTextView().getText().toString(), onItemClickListener);
        viewHolder.setOnLongClickListener(viewHolder.getSuggestionTextView().getText().toString(), onItemLongClickListener);
    }

    @Override
    public int getItemCount()
    {
        return localDataSet.size();
    }

    public void clear()
    {
        localDataSet.clear();
        notifyDataSetChanged();
    }

    public void addItem(String item)
    {
        localDataSet.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(String item)
    {
        int index = localDataSet.indexOf(item);
        if (index != -1)
        {
            localDataSet.remove(item);
            notifyItemRemoved(index);
        }
    }

}
