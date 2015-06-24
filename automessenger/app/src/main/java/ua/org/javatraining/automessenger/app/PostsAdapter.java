package ua.org.javatraining.automessenger.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    List<Post> dataset;
    PhotoService photoService;
    ImageLoader imageLoader = ImageLoader.getInstance();
    Context context;

    public PostsAdapter(List<Post> dataset, PhotoService photoService, Context context) {
        this.dataset = dataset;
        this.photoService = photoService;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView photo;
        private TextView tag;
        private TextView description;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.card_image_view);
            tag = (TextView) itemView.findViewById(R.id.text_tag);
            description = (TextView) itemView.findViewById(R.id.text_description);
            date = (TextView) itemView.findViewById(R.id.text_date);
        }

    }

    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(PostsAdapter.ViewHolder holder, int position) {
        Post post = dataset.get(position);

        holder.description.setText(post.getPostText());
        holder.date.setText(DateFormatUtil.toReadable(context, post.getPostDate()));
        holder.tag.setText(post.getNameTag());
        imageLoader.displayImage(photoService.getPhoto((int) post.getId()).getPhotoLink(), holder.photo);

    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }
}