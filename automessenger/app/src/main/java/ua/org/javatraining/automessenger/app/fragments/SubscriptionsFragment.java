package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.adapters.PostsAdapter;
import ua.org.javatraining.automessenger.app.adapters.SubscriptionAdapter;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.SubscriptionService;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.entityes.Subscription;
import ua.org.javatraining.automessenger.app.loaders.SubscriptionLoader;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionsFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Subscription>>{

    private static final int SUBSCRIPTION_LOADER_ID = 123;

    private List<Subscription> data;
    private Loader mLoader;
    private RecyclerView myRV;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLM;
    private SQLiteAdapter sqLiteAdapter;
    private SubscriptionService subscriptionService;
    private CallbackInterface activity;

    public interface CallbackInterface{
        public void showPostsByTag(String tag);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (CallbackInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CallBack interface!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.toolbar.setTitle(R.string.subscriptions);
        sqLiteAdapter = SQLiteAdapter.initInstance(getActivity());
        subscriptionService = new SubscriptionService(sqLiteAdapter);
        data = new ArrayList<Subscription>();
        mLoader = getLoaderManager().initLoader(SUBSCRIPTION_LOADER_ID, null, this);
        initRV(view);
    }

    private void initRV(View v) {
        myRV = (RecyclerView) v.findViewById(R.id.subscription_recyclerview);
        myLM = new LinearLayoutManager(getActivity().getApplicationContext());
        myRV.setLayoutManager(myLM);
        myAdapter = new SubscriptionAdapter(this, data);
        myRV.setAdapter(myAdapter);
    }

    public void deleteItem(Subscription item) {
        subscriptionService.deleteSubscription(item);
        mLoader.onContentChanged();
    }


    public void showPosts(String tag) {
        activity.showPostsByTag(tag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getSupportLoaderManager().destroyLoader(SUBSCRIPTION_LOADER_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLoader.onContentChanged();
    }

    @Override
    public android.support.v4.content.Loader<List<Subscription>> onCreateLoader(int id, Bundle args) {
        return new SubscriptionLoader(getActivity());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Subscription>> loader, List<Subscription> data) {
        this.data.clear();
        this.data.addAll(data);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Subscription>> loader) {
    }
}
