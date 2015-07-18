package ua.org.javatraining.automessenger.app.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.TagService;
import ua.org.javatraining.automessenger.app.entities.Photo;
import ua.org.javatraining.automessenger.app.entities.Post;
import ua.org.javatraining.automessenger.app.entities.Tag;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.utils.ValidationUtils;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.io.IOException;

public class AddPostActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;

    private String username;
    private Toolbar toolbar;
    private ImageView imageView;
    private String photoURI;
    private EditText tagText;
    private EditText postText;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DataSource source;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (photoURI != null) {
            imageLoader.displayImage(photoURI, imageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        photoURI = getIntent().getStringExtra("photoPath");
        username = getIntent().getStringExtra("username");

        imageView = (ImageView) findViewById(R.id.photo);
        tagText = (EditText) findViewById(R.id.car_number);
        postText = (EditText) findViewById(R.id.post_description);

        source = DataSourceManager.getSource(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add_post);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        float testLoc[] = getLocation(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    photoURI = imageReturnedIntent.getData().toString();
                    Log.i("", photoURI);
                    imageLoader.displayImage(photoURI, imageView);
                }
        }
    }

    public void addPhotoPressed(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    public void donePressed(MenuItem item) {
        String tag = tagText.getText().toString().replaceAll(" ","").toUpperCase();
        String text = postText.getText().toString();

        if (ValidationUtils.checkTag(tag) && !text.equals("") && photoURI != null) {
            FullPost fPost = new FullPost();

            float[] loc = getLocation(Uri.parse(photoURI));
            boolean statusGEO = loc != null;

            fPost.setText(text);
            fPost.setTag(tag);
            fPost.setDate(System.currentTimeMillis());
            fPost.getPhotos().add(photoURI);
            if (statusGEO) fPost.setPostLocation(Float.toString(loc[0]) + " " + Float.toString(loc[1]));

            source.addPost(fPost);

            onBackPressed();
        }
    }

    //getting GPS coordinates from photo
    private float[] getLocation(Uri uri) {
        float[] result = new float[2];
        String filename = null;
        if (uri != null) {
            try {
                if (uri.getScheme().equals("file")) {
                    filename = uri.getHost() + uri.getPath();
                } else {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        filename = cursor.getString(column_index);
                    }
                    cursor.close();
                }
                ExifInterface exif = new ExifInterface(filename);
                boolean check = exif.getLatLong(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (result[0] == 0 && result[1] == 0) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            result[0] = (float) location.getLatitude();
            result[1] = (float) location.getLongitude();
        }

        return result;
    }

}
