package ua.org.javatraining.automessenger.app.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;


public class SearchFragment extends Fragment {
    public static final int SEARCH_FRAGMENT = 4646468;
    private CallBacks activity;

    public interface CallBacks{
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
        ((MainActivity)activity).toolbar.setTitle(R.string.search);
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

}
