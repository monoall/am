package ua.org.javatraining.automessenger.app.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import ua.org.javatraining.automessenger.app.R;

public class AddPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    String photoPath;
    int reqWidth;
    int reqHeight;
    EditText tagText;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imageView = (ImageView) findViewById(R.id.photo);
        tagText = (EditText) findViewById(R.id.car_number);

        reqHeight = imageView.getHeight();
        reqWidth = imageView.getWidth();
        //Выводим уменьшеный вариант фотографии в ImageView
        if (photoPath != null) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(photoPath, options);
            if (bm != null) {
                imageView.setImageBitmap(bm);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        photoPath = getIntent().getStringExtra("photoPath");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add_post);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void addPhotoPressed(View view) {

    }

    //Щитаем в сколько раз нужно уменьшить фотографию перед тем как вывести ее в ImageView
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void donePressed(MenuItem item) {



    }
}
