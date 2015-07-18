package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.adapters.PostsAdapter;
import ua.org.javatraining.automessenger.app.loaders.PostLoaderByAuthor;
import ua.org.javatraining.automessenger.app.loaders.PostLoaderFeed;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<FullPost>> {

    private static final int POST_LOADER_ID = 14;
    public static final int NEARBY_FRAGMENT = 3366;

    private RecyclerView myRV;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLM;
    private SwipeRefreshLayout refreshLayout;
    private List<FullPost> data = new ArrayList<FullPost>();
    private Loader<List<FullPost>> mLoader;
    private CallBacks activity;

    public interface CallBacks {
        void setDrawerItemState(boolean isHighlighted, int title);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (CallBacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CallBacks interface!");
        }
    }

    @Override
    public Loader<List<FullPost>> onCreateLoader(int id, Bundle args) {
        return new PostLoaderByAuthor(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<FullPost>> loader, List<FullPost> data) {
        this.data.clear();
        this.data.addAll(data);
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FullPost>> loader) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, android.os.Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setTitle(R.string.feed);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoader.onContentChanged();
            }
        });

        mLoader = getActivity().getSupportLoaderManager().initLoader(POST_LOADER_ID, null, this);
        initRecyclerView(view);
        ((PostLoaderByAuthor) mLoader).registerRefreshLayout(refreshLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getSupportLoaderManager().destroyLoader(POST_LOADER_ID);
    }

    @Override
    public void onStop() {
        super.onStop();

        activity.setDrawerItemState(false, NEARBY_FRAGMENT);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setDrawerItemState(true, NEARBY_FRAGMENT);
        mLoader.onContentChanged();
    }

    private void initRecyclerView(View v) {
        myRV = (RecyclerView) v.findViewById(R.id.posts_recyclerview);
        myLM = new LinearLayoutManager(getActivity().getApplicationContext());
        myRV.setLayoutManager(myLM);
        myAdapter = new PostsAdapter(data, getActivity().getApplicationContext());
        myRV.setAdapter(myAdapter);
    }

}
