package ua.org.javatraining.automessenger.app.activityies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import ua.org.javatraining.automessenger.app.R;


public class AddPostActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editText;
    ImageView imageView;
    String photoPath;
    int reqWidth;
    int reqHeight;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imageView = (ImageView) findViewById(R.id.photo);
        reqHeight = imageView.getHeight();
        reqWidth = imageView.getWidth();
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
    }

    public void donePressed(View view) {

    }

    public void addPhotoPressed(View view) {

    }


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
}
