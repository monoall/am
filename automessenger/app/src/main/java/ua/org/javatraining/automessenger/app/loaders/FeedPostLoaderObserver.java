package ua.org.javatraining.automessenger.app.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FeedPostLoaderObserver extends BroadcastReceiver {

    public static final String POST_UPDATED_INTENT = "ua.org.javatraining.automessenger.app.loaders.POST_UPDATED";

    PostLoader loader;

    FeedPostLoaderObserver(PostLoader loader){
        this.loader = loader;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        loader.onContentChanged();
    }
}
