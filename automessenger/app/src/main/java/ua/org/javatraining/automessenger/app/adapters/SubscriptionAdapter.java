package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.entityes.Subscription;
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
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text1);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            rootView = itemView.findViewById(R.id.root_item);
        }
    }

    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubscriptionAdapter.ViewHolder holder, int position) {
        final int pos = position;

        holder.text.setText(dataset.get(position).getNameTag());

        //Define action for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.deleteItem(dataset.get(pos));
            }
        });

        //Define action if item pressed
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.showPosts(dataset.get(pos).getNameTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset != null ? dataset.size() : 0;
    }
}
