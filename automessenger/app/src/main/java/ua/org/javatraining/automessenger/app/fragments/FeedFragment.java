package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.org.javatraining.automessenger.app.PostsAdapter;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.List;

public class FeedFragment extends Fragment {
    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;
    List<FullPost> data;

    FeedFragmentInterface activityCommands;

    public interface FeedFragmentInterface{
        List<FullPost> getFeedPosts();
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
        data = activityCommands.getFeedPosts();
        initRecyclerView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityCommands.getFeedPosts();
    }

    private void initRecyclerView(View v) {
        myRV = (RecyclerView) v.findViewById(R.id.posts_recyclerview);
        myLM = new LinearLayoutManager(getActivity().getApplicationContext());
        myRV.setLayoutManager(myLM);
        myAdapter = new PostsAdapter(data, getActivity().getApplicationContext());
        myRV.setAdapter(myAdapter);
    }

}
