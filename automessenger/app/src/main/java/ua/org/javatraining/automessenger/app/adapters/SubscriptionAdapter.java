package ua.org.javatraining.automessenger.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.fragments.SubscriptionsFragment;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    private List<Subscription> dataset;
    private SubscriptionsFragment parent;


    public SubscriptionAdapter(SubscriptionsFragment fragment, List<Subscription> dataset) {
        this.dataset = dataset;
        parent = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        ImageButton deleteButton;
        TextView deleteText;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text1);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            deleteText = (TextView) itemView.findViewById(R.id.delete_text);
            rootView = itemView.findViewById(R.id.root_item);
        }
    }

    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SubscriptionAdapter.ViewHolder holder, int position) {
        final int pos = position;

        holder.text.setText(dataset.get(position).getTagId());

        //Define action for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deleteButton.setVisibility(View.GONE);
                holder.deleteText.setVisibility(View.VISIBLE);
            }
        });

        holder.deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.deleteItem(dataset.get(pos));
            }
        });

        //Define action if item pressed
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.deleteText.getVisibility() == View.VISIBLE) {
                    holder.deleteText.setVisibility(View.GONE);
                    holder.deleteButton.setVisibility(View.VISIBLE);
                } else {
                    parent.showPosts(dataset.get(pos).getTagId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset != null ? dataset.size() : 0;
    }
}
