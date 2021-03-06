package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import ua.org.javatraining.automessenger.app.fragments.PostByTagFragment;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSource;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.List;
import java.util.Locale;

public class PostLoaderByTag extends AsyncTaskLoader<List<FullPost>> {
    private Geocoder geocoder;
    private SwipeRefreshLayout refreshLayout;
    private String tagname = null;
    private boolean isNextPage = false;
    private long lastPostDate = 0;
    private List<FullPost> data;
    private PostByTagLoaderObserver postObserver;
    private PostByTagFragment postByTagFragment;
    private Context context;

    public PostLoaderByTag(Context context, String tag, PostByTagFragment f) {
        super(context);
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        tagname = tag;

        this.context = context;
        postByTagFragment = f;
    }

    public void nextPage(boolean state, long date) {
        this.isNextPage = state;
        this.lastPostDate = date;
    }

    public void registerRefreshLayout(SwipeRefreshLayout srl) {
        refreshLayout = srl;
    }

    public void setTag(String tag) {
        this.tagname = tag;
    }

    @Override
    public void deliverResult(List<FullPost> data) {
        if (isNextPage) {
            this.data.addAll(data);
            postByTagFragment.setData(this.data);
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
    protected void onStartLoading() {
        super.onStartLoading();

        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }

        if (postObserver == null) {
            postObserver = new PostByTagLoaderObserver(this);
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(postObserver, new IntentFilter(PostByTagLoaderObserver.POST_UPDATED_INTENT));
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        if (postObserver != null) {
            LocalBroadcastManager
                    .getInstance(getContext())
                    .unregisterReceiver(postObserver);
            postObserver = null;
        }
    }

    @Override
    public List<FullPost> loadInBackground() {
        DataSource source = DataSourceManager.getInstance().getPreferedSource(context);

        List<FullPost> fps;

        if (isNextPage) {
            fps = source.getPostsByTagName(tagname, lastPostDate);
        } else {
            fps = source.getPostsByTagName(tagname);
        }

        return fps;
    }
}
