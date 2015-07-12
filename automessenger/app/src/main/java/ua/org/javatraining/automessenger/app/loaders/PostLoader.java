package ua.org.javatraining.automessenger.app.loaders;

import android.location.Address;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Switch;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.fragments.PostByTagFragment;
import ua.org.javatraining.automessenger.app.user.Authentication;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostLoader extends AsyncTaskLoader<List<FullPost>> {

    //Loader can be reused for different needs.
    private static final int FEED_POSTS = 10;    //Load post from feed
    public static final int POSTS_BY_TAG = 11;  //Load posts only with specified tagname
    public static final int NEARBY_POSTS = 12;  //Load posts from nearby

    private SQLiteAdapter sqLiteAdapter;
    private PostService postService;
    private PhotoService photoService;
    private CommentService commentService;
    private FeedPostLoaderObserver postObserver;
    private Geocoder geocoder;
    private SwipeRefreshLayout refreshLayout;
    private int mode = FEED_POSTS; //Mode we will work (default value)
    private String tagname = null; //Will use it only in POST_BY_TAG mode

    //Use this constructor if we need feed posts
    public PostLoader(Context context) {
        super(context);
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        mode = FEED_POSTS;
    }

    //Use this constructor if we need posts by tagname
    public PostLoader(Context context, String tag) {
        super(context);
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        tagname = tag;
        mode = POSTS_BY_TAG;
    }

    public void setTag(String tag) {
        this.tagname = tag;
    }

    public void registerRefreshLayout(SwipeRefreshLayout srl) {
        refreshLayout = srl;
    }

    @Override
    public void deliverResult(List<FullPost> data) {
        super.deliverResult(data);
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public List<FullPost> loadInBackground() {
        return updateData();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        postService = new PostService(sqLiteAdapter);
        photoService = new PhotoService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        //Define what observer we must use depends on mode
        if (postObserver == null) {
            switch (mode) {
                case FEED_POSTS:
                    postObserver = new FeedPostLoaderObserver(this);
                    break;
            }

            //Register observer
            if (postObserver != null) {
                LocalBroadcastManager
                        .getInstance(getContext())
                        .registerReceiver(postObserver, new IntentFilter(FeedPostLoaderObserver.POST_UPDATED_INTENT));
            }
        }
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (postObserver != null) {
            LocalBroadcastManager
                    .getInstance(getContext())
                    .unregisterReceiver(postObserver);
            postObserver = null;
        }
    }

    private List<FullPost> updateData() {
        List<Post> posts = null;

        switch (mode) {
            case FEED_POSTS:
                posts = postService.getPostsFromSubscribes(Authentication.getLastUser(getContext()));
                break;
            case POSTS_BY_TAG:
                posts = postService.getPostsByTag(tagname);
                break;
        }

        List<FullPost> data = new ArrayList<FullPost>();
        if (posts != null) {
            for (Post p : posts) {
                FullPost fp = new FullPost(p);

                float latitude, longitude;
                int separatorPosition;
                String coordinates = p.getPostLocation();
                separatorPosition = coordinates.indexOf(" ");
                latitude = Float.valueOf(coordinates.substring(0, separatorPosition));
                longitude = Float.valueOf(coordinates.substring(separatorPosition, coordinates.length()));
                try {
                    Address address = geocoder.getFromLocation(latitude, longitude, 1).get(0);
                    fp.setPostLocation(address.getCountryName() + ", " + address.getAdminArea() + ", " + address.getLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fp.getPhotos().add(photoService.getPhoto((int) p.getId()).getPhotoLink());//todo remove cast to int after DB fix
                fp.setCommentCount(commentService.getAllComments((int) p.getId()).size());
                data.add(fp);
            }
        }
        return data;
    }
}
