package ua.org.javatraining.automessenger.app.loaders;


import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.SuperComment;

import java.util.List;

public class CommentLoader extends AsyncTaskLoader<List<SuperComment>> {

    private long postID;
    DataSource source;
    CommentLoaderObserver commentObserver;

    public CommentLoader(Context context, long postID) {
        super(context);
        this.postID = postID;
        source = DataSourceManager.getSource(context);
    }

    @Override
    public List<SuperComment> loadInBackground() {
        return source.getComments(postID);
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
