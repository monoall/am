package ua.org.javatraining.automessenger.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;

public class PostDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView descriptionTextView;
    View listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_post_details);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        justForTest();
    }

    //Временный метод. Заполняем случайными данными для проверки разметки
    void justForTest() {

        ImageView imageView = (ImageView) findViewById(R.id.photo);
        descriptionTextView = (TextView) findViewById(R.id.description_text);
        TextView dateTextView = (TextView) findViewById(R.id.date_text);

        toolbar.setTitle("BE0000AA");

        imageView.setImageResource(R.drawable.myimg);
        descriptionTextView.setText("Cowards die many times before their deaths; the valiant never taste of death but once.");
        dateTextView.setText("10 march 2015");

    }

    //Обрабатываем нажатие кнопки "subscribe"
    public void actionSubscribe(MenuItem item) {

    }

    //Нажата кнопка "share"
    public void actionShare(View view) {

        String extraText = descriptionTextView.getText().toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        //  !!!!  В качестве примера используем картинку из ресурсов приложения,
        // в конечном варианте c рабочай базой данных должны брать изображение из поста !!!!!
        Uri uri = Uri.parse("android.resource://ua.org.javatraining.automessenger.app/drawable/myimg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share"));

    }

    //нажата кнопка оценки
    public void actionSetRate(View view) {
    }

    public void listItemPressed(View view) {
        View tempListItem = view.findViewById(R.id.rate_panel);
        if (listItem == tempListItem)
            if (listItem.getVisibility() == View.VISIBLE)
                listItem.setVisibility(View.GONE);
            else
                listItem.setVisibility(View.VISIBLE);
        else{
            if(listItem!=null)
                listItem.setVisibility(View.GONE);
            tempListItem.setVisibility(View.VISIBLE);
        }
        listItem = tempListItem;
    }
}
