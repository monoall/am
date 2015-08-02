package ua.org.javatraining.automessenger.app.loaders;

import android.content.IntentFilter;
import android.location.Address;
import android.content.Context;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import ua.org.javatraining.automessenger.app.fragments.FeedFragment;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PostLoaderFeed extends AsyncTaskLoader<List<FullPost>> {

    private Geocoder geocoder;
    private SwipeRefreshLayout refreshLayout;
    private DataSource source;
    private boolean isNextPage = false;
    private long lastPostDate = 0;
    private List<FullPost> data;
    private FeedPostLoaderObserver postObserver;
    private FeedFragment ff;

    public void nextPage(boolean state, long date) {
        this.isNextPage = state;
        this.lastPostDate = date;
    }

    public PostLoaderFeed(Context context, FeedFragment ff) {
        super(context);

        this.ff = ff;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        source = DataSourceManager.getSource(context);
    }

    public void registerRefreshLayout(SwipeRefreshLayout srl) {
        refreshLayout = srl;
    }

    @Override
    public void deliverResult(List<FullPost> data) {

        if (isNextPage) {
            this.data.addAll(data);
            ff.setData(this.data);
        } else {
            this.data = data;
            super.deliverResult(data);
        }

        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }

        Log.i("myTag", "PostLoaderFeed, deliverResult, data size = " + this.data.size());

        isNextPage = false;
        lastPostDate = 0;

    }

    @Override
    public List<FullPost> loadInBackground() {
        List<FullPost> fps;
        if (isNextPage) {
            fps = source.getPostsFromSubscriptions(lastPostDate);
            Log.i("myTag", "PostLoaderFeed, loadInBackground, isNextPage = true, data size = " + fps.size());
        } else {
            fps = source.getPostsFromSubscriptions();
            Log.i("myTag", "PostLoaderFeed, loadInBackground, isNextPage = false, data size = " + fps.size());
        }

        for (FullPost fp : fps) {
            float latitude, longitude;
            int separatorPosition;
            String coordinates = fp.getPostLocation();
            try {
                separatorPosition = coordinates.indexOf(" ");
                latitude = Float.valueOf(coordinates.substring(0, separatorPosition));
                longitude = Float.valueOf(coordinates.substring(separatorPosition, coordinates.length()));
                Address address = geocoder.getFromLocation(latitude, longitude, 1).get(0);
                fp.setPostLocation(address.getCountryName() + ", " + address.getAdminArea() + ", " + address.getLocality());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        return fps;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }

        if (postObserver == null) {
            postObserver = new FeedPostLoaderObserver(this);
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(postObserver, new IntentFilter(FeedPostLoaderObserver.POST_UPDATED_INTENT));
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
}
