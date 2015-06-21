package ua.org.javatraining.automessenger.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    List<Post> dataset;

    public PostsAdapter(List<Post> dataset) {
        this.dataset = dataset;
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
        //todo Реализовать загрузку фото на форму
        holder.description.setText(dataset.get(position).getPostText());
        holder.date.setText(Integer.toString(dataset.get(position).getPostDate()));//todo доделать форматирование даты
        //holder.tag.setText(Integer.toString(dataset.get(position).getIdTag()));
        holder.photo.setImageResource(R.drawable.myimg);
    }

    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }
}
