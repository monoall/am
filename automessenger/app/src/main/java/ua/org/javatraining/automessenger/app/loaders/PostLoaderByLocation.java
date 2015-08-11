package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import ua.org.javatraining.automessenger.app.fragments.NearbyFragment;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.ShortLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostLoaderByLocation extends AsyncTaskLoader<List<FullPost>> {

    private Geocoder defaultGeocoder;
    private Geocoder engGeocoder;
    private SwipeRefreshLayout refreshLayout;
    private LocationManager locationManager;
    private boolean isNextPage = false;
    private long lastPostDate = 0;
    private NearbyFragment nf;
    private List<FullPost> data;
    private PostLoaderByLocationObserver postObserver;
    private Context context;

    public void nextPage(boolean state, long date) {
        this.isNextPage = state;
        this.lastPostDate = date;
    }

    public PostLoaderByLocation(Context context, NearbyFragment nf) {
        super(context);

        this.nf = nf;
        defaultGeocoder = new Geocoder(getContext(), Locale.getDefault());
        engGeocoder = new Geocoder(getContext(), Locale.ENGLISH);
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
        float[] loc = getCurrentLocation();
        Address adrs;

        DataSource source = DataSourceManager.getInstance().getPreferedSource(context);

        try {
            adrs = engGeocoder.getFromLocation(loc[0], loc[1], 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return fps;
        }

        ShortLocation shortLocation = new ShortLocation();
        shortLocation.setCountry(adrs.getCountryName());
        shortLocation.setAdminArea(adrs.getAdminArea());
        if (adrs.getLocality() == null) {
            shortLocation.setRegion(adrs.getSubAdminArea());
        } else {
            shortLocation.setRegion(adrs.getLocality());
        }

        if (isNextPage) {
            fps = source.getPostsByLocation(shortLocation, lastPostDate);
        } else {
            fps = source.getPostsByLocation(shortLocation);
        }

        for (FullPost fp : fps) {
            float latitude, longitude;
            int separatorPosition;
            String coordinates = fp.getPostLocation();
            separatorPosition = coordinates.indexOf(" ");
            latitude = Float.valueOf(coordinates.substring(0, separatorPosition));
            longitude = Float.valueOf(coordinates.substring(separatorPosition, coordinates.length()));

            try {
                Address address = defaultGeocoder.getFromLocation(latitude, longitude, 1).get(0);
                fp.setPostLocation(address.getCountryName() + ", " + address.getAdminArea() + ", " + address.getLocality());
            } catch (IOException e) {
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

    private float[] getCurrentLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        float[] result = new float[2];
        result[0] = (float) location.getLatitude();
        result[1] = (float) location.getLongitude();
        return result;
    }
}
