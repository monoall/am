package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.adapters.PostsAdapter;
import ua.org.javatraining.automessenger.app.loaders.PostLoader;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.ArrayList;
import java.util.List;

public class PostByTagFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<FullPost>> {

    private static final int POST_LOADER_ID = 2;

    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;
    SwipeRefreshLayout refreshLayout;
    List<FullPost> data = new ArrayList<FullPost>();
    private Loader<List<FullPost>> mLoader;
    private CallbackInterface activity;

    public interface CallbackInterface {
        String getTag();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (CallbackInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callback interface!");
        }
    }

    @Override
    public Loader<List<FullPost>> onCreateLoader(int id, Bundle args) {
        return new PostLoader(getActivity().getApplicationContext(), activity.getTag());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) activity).toolbar.setTitle(activity.getTag());
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoader.onContentChanged();
            }
        });
        mLoader = getActivity().getSupportLoaderManager().initLoader(POST_LOADER_ID, null, this);
        initRecyclerView(view);
        ((PostLoader) mLoader).registerRefreshLayout(refreshLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getSupportLoaderManager().destroyLoader(POST_LOADER_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((PostLoader)mLoader).setTag(activity.getTag());
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
