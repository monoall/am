package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entities.Post;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostLoaderByTag extends AsyncTaskLoader<List<FullPost>> {
    private Geocoder geocoder;
    private SwipeRefreshLayout refreshLayout;
    private String tagname = null;
    private DataSource source;

    public PostLoaderByTag(Context context, String tag) {
        super(context);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        tagname = tag;
        source = DataSourceManager.getSource(context);
    }

    public void setTag(String tag) {
        this.tagname = tag;
    }

    @Override
    public List<FullPost> loadInBackground() {
        List<FullPost> fps = source.getPostsByTagName(tagname);

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
}
