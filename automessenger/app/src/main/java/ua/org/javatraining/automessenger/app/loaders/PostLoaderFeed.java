package ua.org.javatraining.automessenger.app.loaders;

import android.location.Address;
import android.content.Context;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.widget.SwipeRefreshLayout;
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

    public PostLoaderFeed(Context context) {
        super(context);

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        source = DataSourceManager.getSource(context);
    }

    public void registerRefreshLayout(SwipeRefreshLayout srl) {
        refreshLayout = srl;
    }

    @Override
    public void deliverResult(List<FullPost> data) {
        super.deliverResult(data);

        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public List<FullPost> loadInBackground() {
        List<FullPost> fps = source.getPostsFromSubscriptions();

        for (FullPost fp : fps) {
            float latitude, longitude;
            int separatorPosition;
            String coordinates = fp.getPostLocation();
            separatorPosition = coordinates.indexOf(" ");
            latitude = Float.valueOf(coordinates.substring(0, separatorPosition));
            longitude = Float.valueOf(coordinates.substring(separatorPosition, coordinates.length()));

            try {
                Address address = geocoder.getFromLocation(latitude, longitude, 1).get(0);
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
    }
}
