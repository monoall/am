package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.org.javatraining.automessenger.app.adapters.PostsAdapter;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.loaders.PostLoader;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<FullPost>> {

    private static final int POST_LOADER_ID = 1;

    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;
    List<FullPost> data = new ArrayList<FullPost>();
    private Loader<List<FullPost>> mLoader;

    FeedFragmentInterface activityCommands;

    @Override
    public Loader<List<FullPost>> onCreateLoader(int id, Bundle args) {
        return new PostLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<FullPost>> loader, List<FullPost> data) {
        this.data.clear();
        this.data.addAll(data);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<FullPost>> loader) {
    }

    public interface FeedFragmentInterface {
        String getUsername();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommands = (FeedFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FeedActivityInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setTitle(R.string.feed);
        mLoader = getActivity().getSupportLoaderManager().initLoader(POST_LOADER_ID, null, this);
        initRecyclerView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getSupportLoaderManager().destroyLoader(POST_LOADER_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
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
