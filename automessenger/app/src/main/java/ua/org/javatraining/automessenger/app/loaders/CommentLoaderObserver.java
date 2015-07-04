package ua.org.javatraining.automessenger.app.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ua.org.javatraining.automessenger.app.entityes.Post;

public class CommentLoaderObserver extends BroadcastReceiver {

    public static final String COMMENTS_UPDATED_INTENT = "ua.org.javatraining.automessenger.app.loaders.COMMENTS_UPDATED";

    CommentLoader loader;

    public CommentLoaderObserver(CommentLoader loader) {
        this.loader = loader;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int intentPostID = intent.getIntExtra(Post.POST_ID, -1);
        if(intentPostID == loader.getPostID()) {
            Log.i("myTag", "Message received " + this.toString());
            loader.onContentChanged();
        }
    }
}
