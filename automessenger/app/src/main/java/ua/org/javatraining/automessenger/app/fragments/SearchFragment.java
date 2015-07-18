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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.adapters.PostsAdapter;
import ua.org.javatraining.automessenger.app.adapters.TagAdapter;
import ua.org.javatraining.automessenger.app.entities.Tag;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final int SEARCH_FRAGMENT = 4646468;

    private CallBacks activity;
    private ImageButton searchButton;
    private EditText searchField;
    private RecyclerView myRV;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLM;
    private DataSource source;
    private List<Tag> dataset;
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

        source = DataSourceManager.getSource(getActivity());
        dataset = new ArrayList<Tag>();

        initRecyclerView(view);

        searchField = (EditText) view.findViewById(R.id.search_field);
        searchButton = (ImageButton) view.findViewById(R.id.go_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTask = new SearchAsyncTask();
                searchTask.execute(searchField.getText().toString());
            }
        });

        ((MainActivity) activity).toolbar.setTitle(R.string.search);
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

    private class SearchAsyncTask extends AsyncTask<String, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(String... params) {
            Log.i("mytag", "SearchAsyncTask,  doInBackground(), param: " + params[0]);
            return source.findSomeTags(params[0]);
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            super.onPostExecute(tags);
            Log.i("mytag", "SearchAsyncTask,  onPostExecute(), tags size: " + tags.size());
            dataset.clear();
            dataset.addAll(tags);
            myAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView(View v) {
        myRV = (RecyclerView) v.findViewById(R.id.list);
        myLM = new LinearLayoutManager(getActivity().getApplicationContext());
        myRV.setLayoutManager(myLM);
        myAdapter = new TagAdapter(dataset, this);
        myRV.setAdapter(myAdapter);
    }

    public void showPosts(String tag) {
        activity.showPostsByTag(tag);
    }
}
