package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.PostDetails;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.PostGrades;
import ua.org.javatraining.automessenger.app.vo.SuperComment;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SuperComment> dataset;
    static View listItem;
    private Context context;
    private FullPost post;
    private PostGrades postGrades;
    private PostDetails outerActivity;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String location = "";
    private String textToShare;
    private String photoToShare;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CommentsAdapter(Context context, FullPost post, List<SuperComment> dataset, PostGrades grades, PostDetails outerActivity) {
        this.dataset = dataset;
        this.post = post;
        this.context = context;
        this.postGrades = grades;
        this.outerActivity = outerActivity;
    }

    public static class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView descriptionField;
        public TextView dateField;
        public TextView ratingField;
        public ImageButton thumbUp;
        public ImageButton thumbDown;
        public View curView;

        public CommentHolder(View itemView) {
            super(itemView);
            curView = itemView;
            descriptionField = (TextView) itemView.findViewById(R.id.description_field);
            dateField = (TextView) itemView.findViewById(R.id.date_field);
            ratingField = (TextView) itemView.findViewById(R.id.rating_field);
            thumbDown = (ImageButton) itemView.findViewById(R.id.comment_thumb_down);
            thumbUp = (ImageButton) itemView.findViewById(R.id.comment_thumb_up);
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
        public TextView locationField;
        public TextView gradeNumber;
        public ImageButton thumbsUp;
        public ImageButton thumbsDown;

        public HeaderHolder(View itemView) {
            super(itemView);

            locationField = (TextView) itemView.findViewById(R.id.location_filed);
            gradeNumber = (TextView) itemView.findViewById(R.id.grade_number);
            thumbsDown = (ImageButton) itemView.findViewById(R.id.down_button);
            thumbsUp = (ImageButton) itemView.findViewById(R.id.up_button);
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
            final HeaderHolder hh = (HeaderHolder) viewHolder;
            hh.dateField.setText(DateFormatUtil.toReadable(context, post.getDate()));
            hh.descriptionField.setText(post.getText());
            hh.gradeNumber.setText(String.valueOf(postGrades.getSumGrade() + postGrades.getUserGrade()));

            switch (postGrades.getUserGrade()) {
                case -1:
                    hh.thumbsDown.setImageResource(R.drawable.ic_thumb_down_black_18dp);
                    break;
                case 1:
                    hh.thumbsUp.setImageResource(R.drawable.ic_thumb_up_black_18dp);
                    break;
            }

            hh.thumbsDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postGrades.getUserGrade() == -1) {
                        outerActivity.setPostRate(0);
                        postGrades = outerActivity.getPostGrades();
                        hh.gradeNumber.setText(String.valueOf(postGrades.getSumGrade()));
                        hh.thumbsDown.setImageResource(R.drawable.ic_thumb_down_grey600_18dp);
                    } else {
                        outerActivity.setPostRate(-1);
                        postGrades = outerActivity.getPostGrades();
                        hh.gradeNumber.setText(String.valueOf(postGrades.getSumGrade() - 1));
                        hh.thumbsUp.setImageResource(R.drawable.ic_thumb_up_grey600_18dp);
                        hh.thumbsDown.setImageResource(R.drawable.ic_thumb_down_black_18dp);
                    }
                }
            });

            hh.thumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postGrades.getUserGrade() == 1) {
                        outerActivity.setPostRate(0);
                        postGrades = outerActivity.getPostGrades();
                        hh.gradeNumber.setText(String.valueOf(postGrades.getSumGrade()));
                        hh.thumbsUp.setImageResource(R.drawable.ic_thumb_up_grey600_18dp);
                    } else {
                        outerActivity.setPostRate(+1);
                        postGrades = outerActivity.getPostGrades();
                        hh.gradeNumber.setText(String.valueOf(postGrades.getSumGrade() + 1));
                        hh.thumbsUp.setImageResource(R.drawable.ic_thumb_up_black_18dp);
                        hh.thumbsDown.setImageResource(R.drawable.ic_thumb_down_grey600_18dp);
                    }
                }
            });

            if (!location.equals("")) {
                hh.locationField.setText(location);
            } else {
                AddressLoader al = new AddressLoader(hh.locationField, post.getPostID());
                al.execute(post.getPostLocation());
            }

            imageLoader.displayImage(post.getPhotos().get(0), hh.photo);
            try {
                photoToShare = imageLoader.getDiskCache().get(post.getPhotos().get(0)).getAbsoluteFile().getAbsolutePath();
            }catch (NullPointerException e){
                photoToShare = "";
            }
            textToShare = post.getText();

        } else if (viewHolder instanceof CommentHolder) {
            final CommentHolder cm = (CommentHolder) viewHolder;
            final SuperComment superComment = dataset.get(i - 1);
            cm.descriptionField.setText(dataset.get(i - 1).getCommentText());
            cm.dateField.setText(DateFormatUtil.toReadable(context, dataset.get(i - 1).getCommentDate()));
            cm.curView.setOnClickListener(cm);
            cm.ratingField.setText(String.valueOf(superComment.getGradeNumber() + superComment.getUserGrade()));

            switch (superComment.getUserGrade()) {
                case 1:
                    cm.thumbUp.setImageResource(R.drawable.ic_thumb_up_black_18dp);
                    break;
                case -1:
                    cm.thumbDown.setImageResource(R.drawable.ic_thumb_down_black_18dp);
                    break;
            }

            cm.thumbUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (superComment.getUserGrade() == 1) {
                        outerActivity.setCommentRate(superComment.getId(), 0);
                        superComment.setUserGrade(outerActivity.getCommentGrades((int) superComment.getId()).getUserGrade());
                        cm.thumbUp.setImageResource(R.drawable.ic_thumb_up_grey600_18dp);
                        cm.ratingField.setText(String.valueOf(superComment.getGradeNumber() + superComment.getUserGrade()));
                    } else {
                        outerActivity.setCommentRate(superComment.getId(), 1);
                        superComment.setUserGrade(outerActivity.getCommentGrades((int) superComment.getId()).getUserGrade());
                        cm.thumbUp.setImageResource(R.drawable.ic_thumb_up_black_18dp);
                        cm.thumbDown.setImageResource(R.drawable.ic_thumb_down_grey600_18dp);
                        cm.ratingField.setText(String.valueOf(superComment.getGradeNumber() + superComment.getUserGrade()));
                    }
                }
            });

            cm.thumbDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (superComment.getUserGrade() == -1) {
                        outerActivity.setCommentRate(superComment.getId(), 0);
                        superComment.setUserGrade(outerActivity.getCommentGrades((int) superComment.getId()).getUserGrade());
                        cm.thumbDown.setImageResource(R.drawable.ic_thumb_down_grey600_18dp);
                        cm.ratingField.setText(String.valueOf(superComment.getGradeNumber() + superComment.getUserGrade()));
                    } else {
                        outerActivity.setCommentRate(superComment.getId(), -1);
                        superComment.setUserGrade(outerActivity.getCommentGrades((int) superComment.getId()).getUserGrade());
                        cm.thumbUp.setImageResource(R.drawable.ic_thumb_up_grey600_18dp);
                        cm.thumbDown.setImageResource(R.drawable.ic_thumb_down_black_18dp);
                        cm.ratingField.setText(String.valueOf(superComment.getGradeNumber() + superComment.getUserGrade()));
                    }
                }
            });

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

    public String[] getShareStuff() {
        return new String[]{textToShare, photoToShare};
    }

    private class AddressLoader extends AsyncTask<String, Void, String> {

        private TextView textView;
        private long postId;
        private boolean status = false;

        public AddressLoader(TextView textView, long postId) {
            this.textView = textView;
            this.postId = postId;
        }

        @Override
        protected String doInBackground(String... strings) {
            float latitude, longitude;
            int separatorPosition;

            String txt = strings[0];
            Log.i("myTag", "PostAdapter, AddressLoader, doInBackground, str = " + txt);

            String result = "";

            try {
                separatorPosition = txt.indexOf(" ");
                latitude = Float.valueOf(txt.substring(0, separatorPosition));
                longitude = Float.valueOf(txt.substring(separatorPosition, txt.length()));

                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    result = address.getCountryName() + ", " + address.getAdminArea() + ", " + address.getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();

                return strings[0];
            } catch (NullPointerException e) {
                e.printStackTrace();

                return strings[0];
            }

            status = true;

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status) {
                location = s;
            }

            textView.setText(s);
        }
    }
}
