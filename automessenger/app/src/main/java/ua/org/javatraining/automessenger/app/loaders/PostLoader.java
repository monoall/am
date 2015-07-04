package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.user.Authentication;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.ArrayList;
import java.util.List;

public class PostLoader extends AsyncTaskLoader<List<FullPost>> {


    SQLiteAdapter sqLiteAdapter;
    PostService postService;
    PhotoService photoService;
    CommentService commentService;
    PostLoaderObserver postObserver;

    public PostLoader(Context context) {
        super(context);
        Log.i("myTag", "PostLoader constructor");
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        postService = new PostService(sqLiteAdapter);
        photoService = new PhotoService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
    }

    @Override
    public List<FullPost> loadInBackground() {
        return updateData();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (postObserver == null) {
            postObserver = new PostLoaderObserver(this);
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(postObserver, new IntentFilter(PostLoaderObserver.POST_UPDATED_INTENT));
            Log.i("myTag", "PostLoaderObserver registered: " + postObserver.toString());
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (postObserver != null) {
            LocalBroadcastManager
                    .getInstance(getContext())
                    .unregisterReceiver(postObserver);
            Log.i("myTag", "PostLoaderObserver unregistered: " + postObserver.toString());
            postObserver = null;
        }
    }

    private List<FullPost> updateData() {
        Log.i("myTag", "updateData " + Authentication.getLastUser(getContext()));
        List<Post> posts = postService.getPostsFromSubscribes(Authentication.getLastUser(getContext()));
        List<FullPost> data = new ArrayList<FullPost>();
        if (posts != null) {
            for (Post p : posts) {
                FullPost fp = new FullPost(p);
                fp.getPhotos().add(photoService.getPhoto((int) p.getId()).getPhotoLink());//todo remove cast to int after DB fix
                fp.setCommentCount(commentService.getAllComments((int) p.getId()).size());
                data.add(fp);
            }
        }
        Log.i("myTag", "data from loader: " + Integer.toString(data.size()));
        return data;
    }
}
