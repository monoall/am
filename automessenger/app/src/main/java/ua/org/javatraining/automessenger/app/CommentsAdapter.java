package ua.org.javatraining.automessenger.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.entityes.Comment;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> dataset;
    static View listItem;
    Context context;

    public CommentsAdapter(Context context, List<Comment> dataset){
        this.dataset = dataset;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView descriptionField;
        public TextView dateField;
        public TextView ratingField;
        public View curView;

        public ViewHolder(View itemView){
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
            switch (id){
                case R.id.comment_ll: listItemPressed(v);
            }
        }
    }

    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.descriptionField.setText(dataset.get(i).getCommentText());
        viewHolder.dateField.setText(DateFormatUtil.toReadable(context, dataset.get(i).getCommentDate()));
        //todo разобратся с обработкой рейтинга комментария
        viewHolder.ratingField.setText("666");// <- случайные данные для проверки разметки

        viewHolder.curView.setOnClickListener(viewHolder);
    }

    @Override
    public int getItemCount() {
        return (dataset == null) ? 0 : dataset.size() ;
    }

    public static void listItemPressed(View view) {
        View tempListItem = view.findViewById(R.id.rate_panel);
        if (listItem == tempListItem)
            if (listItem.getVisibility() == View.VISIBLE)
                listItem.setVisibility(View.GONE);
            else
                listItem.setVisibility(View.VISIBLE);
        else{
            if(listItem!=null)
                listItem.setVisibility(View.GONE);
            tempListItem.setVisibility(View.VISIBLE);
        }
        listItem = tempListItem;
    }
}
