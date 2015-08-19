package ua.org.javatraining.automessenger.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.PostDetails;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;

    private List<FullPost> dataset;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context context;
    private Map<Long, String> addresses = new ArrayMap<Long, String>();

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

            if (addresses.containsKey(fullPost.getPostID())) {
                itemHolder.location.setText(addresses.get(fullPost.getPostID()));
            } else {
                //itemHolder.location.setText(fullPost.getPostLocation());
                AddressLoader al = new AddressLoader(itemHolder.location, fullPost.getPostID());
                al.execute(fullPost.getPostLocation());
            }

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
                addresses.put(postId, s);
            }

            textView.setText(s);
        }
    }
}
