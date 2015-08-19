package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSource;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.fragments.SearchFragment;
import ua.org.javatraining.automessenger.app.user.Authentication;
import ua.org.javatraining.automessenger.app.vo.ExtTag;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private SearchFragment parent;
    private List<ExtTag> dataset;
    private DataSource source;
    private Context context;

    public TagAdapter(Context context, List<ExtTag> dataset, SearchFragment parent) {
        this.dataset = dataset;
        this.parent = parent;
        this.context = context;
        source = DataSourceManager.getInstance().getPreferedSource(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageButton star;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text1);
            star = (ImageButton) itemView.findViewById(R.id.is_subscribed);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;

        holder.textView.setText(dataset.get(position).getTagName());
        if(dataset.get(pos).isSubscribed()){
            holder.star.setImageResource(R.drawable.ic_star_white_24dp);
        }

        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataset.get(pos).isSubscribed()){
                    Subscription s = new Subscription();
                    s.setTagId(dataset.get(pos).getTagName());
                    s.setUserId(Authentication.getLastUser(context));
                    source.removeSubscription(s);
                    dataset.get(pos).setIsSubscribed(false);
                    holder.star.setImageResource(R.drawable.ic_star_outline_white_24dp);
                }else {
                    source.addSubscription(dataset.get(pos).getTagName());
                    dataset.get(pos).setIsSubscribed(true);
                    holder.star.setImageResource(R.drawable.ic_star_white_24dp);
                }
            }
        });

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
