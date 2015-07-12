package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entities.Post;
import ua.org.javatraining.automessenger.app.user.Authentication;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostLoaderByTag extends AsyncTaskLoader<List<FullPost>> {
    private SQLiteAdapter sqLiteAdapter;
    private PostService postService;
    private PhotoService photoService;
    private CommentService commentService;
    private FeedPostLoaderObserver postObserver;
    private Geocoder geocoder;
    private SwipeRefreshLayout refreshLayout;
    private String tagname = null;

    public PostLoaderByTag(Context context, String tag) {
        super(context);
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        postService = new PostService(sqLiteAdapter);
        photoService = new PhotoService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        tagname = tag;
    }

    public void setTag(String tag) {
        this.tagname = tag;
    }

    @Override
    public List<FullPost> loadInBackground() {
        return updateData();
    }

    private List<FullPost> updateData() {
        List<Post> posts = postService.getPostsByTag(tagname);
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
