package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.entities.Comment;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment> dataset;
    static View listItem;
    private Context context;
    private FullPost post;
    ImageLoader imageLoader = ImageLoader.getInstance();


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CommentsAdapter(Context context, FullPost post, List<Comment> dataset) {
        this.dataset = dataset;
        this.post = post;
        this.context = context;
    }

    public static class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView descriptionField;
        public TextView dateField;
        public TextView ratingField;
        public View curView;

        public CommentHolder(View itemView) {
            super(itemView);
            curView = itemView;
            descriptionField = (TextView) itemView.findViewById(R.id.description_field);
            dateField = (TextView) itemView.findViewById(R.id.date_field);
            ratingField = (TextView) itemView.findViewById(R.id.rating_field);
        }

        @Override
        public void onClick(View v) {
            Log.i("", v.toString());
            int id = v.getId();
            switch (id) {
                case R.id.comment_ll:
                    listItemPressed(v);
            }
        }
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView descriptionField;
        public TextView dateField;
        public ImageView photo;

        public HeaderHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            descriptionField = (TextView) itemView.findViewById(R.id.description_text);
            dateField = (TextView) itemView.findViewById(R.id.date_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_details_card, viewGroup, false);
            return new HeaderHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_layout, viewGroup, false);
            return new CommentHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderHolder) {
            HeaderHolder hh = (HeaderHolder) viewHolder;
            hh.dateField.setText(DateFormatUtil.toReadable(context, post.getDate()));
            hh.descriptionField.setText(post.getText());
            imageLoader.displayImage(post.getPhotos().get(0), hh.photo);
        } else if (viewHolder instanceof CommentHolder) {
            CommentHolder cm = (CommentHolder) viewHolder;
            cm.descriptionField.setText(dataset.get(i-1).getCommentText());
            cm.dateField.setText(DateFormatUtil.toReadable(context, dataset.get(i-1).getCommentDate()));
            //todo разобратся с обработкой рейтинга комментария
            cm.curView.setOnClickListener(cm);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size() + 1;
    }

    public static void listItemPressed(View view) {
        View tempListItem = view.findViewById(R.id.rate_panel);
        if (listItem == tempListItem)
            if (listItem.getVisibility() == View.VISIBLE)
                listItem.setVisibility(View.GONE);
            else
                listItem.setVisibility(View.VISIBLE);
        else {
            if (listItem != null)
                listItem.setVisibility(View.GONE);
            tempListItem.setVisibility(View.VISIBLE);
        }
        listItem = tempListItem;
    }
}