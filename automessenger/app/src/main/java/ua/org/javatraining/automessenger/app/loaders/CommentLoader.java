package ua.org.javatraining.automessenger.app.loaders;


import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entities.Comment;

import java.util.List;

public class CommentLoader extends AsyncTaskLoader<List<Comment>> {

    private long postID;
    SQLiteAdapter sqLiteAdapter;
    CommentService commentService;
    CommentLoaderObserver commentObserver;

    public CommentLoader(Context context, long postID) {
        super(context);
        this.postID = postID;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        commentService = new CommentService(sqLiteAdapter);
    }

    @Override
    public List<Comment> loadInBackground() {
        return commentService.getAllComments((int) postID);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (commentObserver == null) {
            commentObserver = new CommentLoaderObserver(this);
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(commentObserver, new IntentFilter(CommentLoaderObserver.COMMENTS_UPDATED_INTENT));
            Log.i("myTag", "CommentLoaderObserver registered: " + commentObserver.toString());

        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (commentObserver != null){
            LocalBroadcastManager
                    .getInstance(getContext())
                    .unregisterReceiver(commentObserver);
            Log.i("myTag", "CommentLoaderObserver unregistered: " + commentObserver.toString());
            commentObserver = null;
        }
    }

    public long getPostID() {
        return postID;
    }
}
