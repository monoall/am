package ua.org.javatraining.automessenger.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.activities.MainActivity;

public class FeedFragment extends Fragment {

    ImageView imageView;
    TextView textViewTag;
    TextView textViewDscrb;
    TextView textViewDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        imageView = (ImageView) view.findViewById(R.id.card_image_view);
        textViewTag = (TextView) view.findViewById(R.id.on_picture_text);
        textViewDscrb = (TextView) view.findViewById(R.id.description_lable);
        textViewDate = (TextView) view.findViewById(R.id.text_date);

        imageView.setImageResource(R.drawable.myimg);
        textViewTag.setText("AA0000BB");
        textViewDscrb.setText("Cowards die many times before their deaths; the valiant never taste of death but once.");
        textViewDate.setText("10 march 2015");

    }

}
