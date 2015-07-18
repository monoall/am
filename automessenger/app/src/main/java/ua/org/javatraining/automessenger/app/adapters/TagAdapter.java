package ua.org.javatraining.automessenger.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.entities.Tag;
import ua.org.javatraining.automessenger.app.fragments.SearchFragment;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    SearchFragment parent;
    List<Tag> dataset;

    public TagAdapter(List<Tag> dataset, SearchFragment parent) {
        this.dataset = dataset;
        this.parent = parent;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        holder.textView.setText(dataset.get(position).getTagName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.showPosts(dataset.get(pos).getTagName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataset == null) ? 0 : dataset.size();
    }
}
