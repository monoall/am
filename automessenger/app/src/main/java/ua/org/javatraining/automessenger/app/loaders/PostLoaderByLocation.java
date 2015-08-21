package ua.org.javatraining.automessenger.app.loaders;

import android.content.*;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import ua.org.javatraining.automessenger.app.fragments.NearbyFragment;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSource;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.services.GPSMonitor;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.ShortLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostLoaderByLocation extends AsyncTaskLoader<List<FullPost>> {

    private Geocoder geocoder;
    private DataSource source;
    private SwipeRefreshLayout refreshLayout;
    private boolean isNextPage = false;
    private long lastPostDate = 0;
    private NearbyFragment nf;
    private List<FullPost> data;
    private PostLoaderByLocationObserver postObserver;
    private GPSMonitor gpsMonitor;

    public void nextPage(boolean state, long date) {
        this.isNextPage = state;
        this.lastPostDate = date;
    }

    public PostLoaderByLocation(Context context, NearbyFragment nf, GPSMonitor gpsMonitor) {
        super(context);

        this.gpsMonitor = gpsMonitor;
        this.nf = nf;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        source = DataSourceManager.getInstance().getPreferedSource(context);
    }

    public void registerRefreshLayout(SwipeRefreshLayout srl) {
        refreshLayout = srl;
    }

    @Override
    public void deliverResult(List<FullPost> data) {

        if (isNextPage) {
            this.data.addAll(data);
            nf.setData(this.data);
        } else {
            this.data = data;
            super.deliverResult(data);
        }

        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }

        isNextPage = false;
        lastPostDate = 0;

    }

    @Override
    public List<FullPost> loadInBackground() {
        List<FullPost> fps = new ArrayList<FullPost>();
        ShortLocation shortLocation = gpsMonitor.getShortLocation();
        if (shortLocation != null) {
            if (isNextPage) {
                fps = source.getPostsByLocation(shortLocation, lastPostDate);
            } else {
                fps = source.getPostsByLocation(shortLocation);
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
            postObserver = new PostLoaderByLocationObserver(this);
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(postObserver, new IntentFilter(PostLoaderByLocationObserver.POST_UPDATED_INTENT));
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
