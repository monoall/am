package ua.org.javatraining.automessenger.app.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PostLoaderByLocationObserver extends BroadcastReceiver {

    public static final String POST_UPDATED_INTENT = "ua.org.javatraining.automessenger.app.loaders.POST_BY_LOCATION_UPDATED";
    public static final String LAST_POST_DATE = "ua.org.javatraining.automessenger.app.loaders.LAST_POST_DATE";

    PostLoaderByLocation loader;

    PostLoaderByLocationObserver(PostLoaderByLocation loader){
        this.loader = loader;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long lastPostDate =  intent.getLongExtra(LAST_POST_DATE,0);

        if(lastPostDate != 0){
            loader.nextPage(true, lastPostDate);
        }

        loader.onContentChanged();
    }
}
