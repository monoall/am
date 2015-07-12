package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class NearbyFragment extends Fragment {

    public static final int NEARBY_FRAGMENT = 3214444;
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
    public void onStop() {
        super.onStop();

        activity.setDrawerItemState(false, NEARBY_FRAGMENT);

    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setDrawerItemState(true, NEARBY_FRAGMENT);
    }
}
