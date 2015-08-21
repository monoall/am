package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebView;
import ua.org.javatraining.automessenger.app.R;

public class AboutFragment extends Fragment {

    public static final int ABOUT_FRAGMENT = 33672;

    private CallBacks activity;

    public interface CallBacks {
        void setDrawerItemState(boolean isHighlighted, int title);
    }

    @Override
    public void onStop() {
        super.onStop();

        activity.setDrawerItemState(false, ABOUT_FRAGMENT);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setDrawerItemState(true, ABOUT_FRAGMENT);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView wv = (WebView) view.findViewById(R.id.web_view);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.loadUrl("file:///android_asset/cat.gif");
    }
}
