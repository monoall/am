package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.PostDetails;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    List<FullPost> dataset;
    ImageLoader imageLoader = ImageLoader.getInstance();
    Context context;

    public PostsAdapter(List<FullPost> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView tag;
        private TextView description;
        private TextView date;
        private TextView location;
        private TextView commentNumber;
        private View frame;

        public ViewHolder(View itemView) {
            super(itemView);

            location = (TextView) itemView.findViewById(R.id.location_filed);
            frame = itemView.findViewById(R.id.cardview);
            photo = (ImageView) itemView.findViewById(R.id.card_image_view);
            tag = (TextView) itemView.findViewById(R.id.text_tag);
            description = (TextView) itemView.findViewById(R.id.text_description);
            date = (TextView) itemView.findViewById(R.id.text_date);
            commentNumber = (TextView) itemView.findViewById(R.id.comment_count);
        }
    }

    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PostsAdapter.ViewHolder holder, int position) {
        final FullPost fullPost = dataset.get(position);

        holder.location.setText(fullPost.getPostLocation());
        holder.description.setText(fullPost.getText());
        holder.date.setText(DateFormatUtil.toReadable(context, fullPost.getDate()));
        holder.tag.setText(fullPost.getTag());
        holder.commentNumber.setText(Integer.toString(fullPost.getCommentCount()));
        imageLoader.displayImage(fullPost.getPhotos().get(0), holder.photo);

        holder.frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostDetails.class);
                intent.putExtra("POST_ID", fullPost.getPostID());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }
}