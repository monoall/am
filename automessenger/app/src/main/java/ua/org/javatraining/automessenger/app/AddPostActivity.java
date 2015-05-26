package ua.org.javatraining.automessenger.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;


public class AddPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white87));

        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.car_number);

    }

}
