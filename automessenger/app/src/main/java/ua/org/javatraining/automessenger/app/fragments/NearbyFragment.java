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
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Photo;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends Fragment {
    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;

    SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(getActivity());
    PhotoService photoService = new PhotoService(sqLiteAdapter);

    NearbyFragmentInterface activityCommands;

    public interface NearbyFragmentInterface{
        List<Post> getNearbyPosts();
        void update();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommands = (NearbyFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NearbyActivityInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setTitle(R.string.nearby);

        initRecyclerView(view);
        activityCommands.update();

    }

    private void initRecyclerView(View v) {
        myRV = (RecyclerView) v.findViewById(R.id.posts_recyclerview);
        myRV.setHasFixedSize(true);
        myLM = new LinearLayoutManager(getActivity().getApplicationContext());
        myRV.setLayoutManager(myLM);
        myAdapter = new PostsAdapter(activityCommands.getNearbyPosts(), photoService, getActivity().getApplicationContext());
        myRV.setAdapter(myAdapter);
    }

}
