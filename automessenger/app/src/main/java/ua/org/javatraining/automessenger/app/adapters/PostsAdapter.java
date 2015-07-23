package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;

    List<FullPost> dataset;
    ImageLoader imageLoader = ImageLoader.getInstance();
    Context context;

    public PostsAdapter(List<FullPost> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position))
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == dataset.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView tag;
        private TextView description;
        private TextView date;
        private TextView location;
        private TextView commentNumber;
        private View frame;

        public ItemViewHolder(View itemView) {
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

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);

            return new FooterViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

            return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            final FullPost fullPost = dataset.get(position);
            itemHolder.location.setText(fullPost.getPostLocation());
            itemHolder.description.setText(fullPost.getText());
            itemHolder.date.setText(DateFormatUtil.toReadable(context, fullPost.getDate()));
            itemHolder.tag.setText(fullPost.getTag());
            itemHolder.commentNumber.setText(Integer.toString(fullPost.getCommentCount()));
            Log.i("mytag", "onBindViewHolder, loc1 = " + fullPost.getLocCountry());
            Log.i("mytag", "onBindViewHolder, loc2 = " + fullPost.getLocAdminArea());
            Log.i("mytag", "onBindViewHolder, loc3 = " + fullPost.getLocRegion());
            imageLoader.displayImage(fullPost.getPhotos().get(0), itemHolder.photo);

            itemHolder.frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostDetails.class);
                    intent.putExtra("POST_ID", fullPost.getPostID());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size() + 1;
    }
}
