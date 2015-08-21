package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.adapters.TagAdapter;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.entities.Tag;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSource;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.ExtTag;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final int SEARCH_FRAGMENT = 4646468;
    public static final String QUERY = "ua.org.javatraining.automessenger.app.fragments.SearchFragment.query";

    private CallBacks activity;
    private RecyclerView.Adapter myAdapter;
    private DataSource source;
    private List<ExtTag> dataset;
    private SearchAsyncTask searchTask;

    public interface CallBacks {
        void showPostsByTag(String tag);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        source = DataSourceManager.getInstance().getPreferedSource(getActivity());
        dataset = new ArrayList<ExtTag>();

        initRecyclerView(view);
        searchTask = new SearchAsyncTask();

        String request = getArguments().getString(QUERY);

        if (request != null) {
            searchTask.execute(request);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!searchTask.isCancelled()) {
            searchTask.cancel(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        activity.setDrawerItemState(false, SEARCH_FRAGMENT);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setDrawerItemState(true, SEARCH_FRAGMENT);
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, List<ExtTag>> {

        @Override
        protected List<ExtTag> doInBackground(String... params) {
            Log.i("mytag", "SearchAsyncTask,  doInBackground(), param: " + params[0]);

            List<Tag> tags = source.findSomeTags(params[0]);
            List<Subscription> subs = source.getSubscriptions();

            List<ExtTag> result = new ArrayList<ExtTag>();

            for (Tag t : tags) {
                boolean isSubscribed = false;

                for (Subscription s : subs) {
                    if (s.getTagId().equals(t.getTagName())) {
                        isSubscribed = true;
                        break;
                    }
                }

                result.add(new ExtTag(t.getTagName(), isSubscribed));
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<ExtTag> tags) {
            super.onPostExecute(tags);
            Log.i("mytag", "SearchAsyncTask,  onPostExecute(), tags size: " + tags.size());

            dataset.clear();
            dataset.addAll(tags);
            myAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView(View v) {
        RecyclerView myRV = (RecyclerView) v.findViewById(R.id.list);
        RecyclerView.LayoutManager myLM = new LinearLayoutManager(getActivity().getApplicationContext());
        myRV.setLayoutManager(myLM);
        myAdapter = new TagAdapter(getActivity(), dataset, this);
        myRV.setAdapter(myAdapter);
    }

    public void showPosts(String tag) {
        activity.showPostsByTag(tag);
    }
}
