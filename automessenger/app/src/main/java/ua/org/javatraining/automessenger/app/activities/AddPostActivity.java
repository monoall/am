package ua.org.javatraining.automessenger.app.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Geocoder;
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
import android.location.Address;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.TagService;
import ua.org.javatraining.automessenger.app.entityes.Photo;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.entityes.Tag;
import ua.org.javatraining.automessenger.app.utils.ValidationUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;

    private String username;
    Toolbar toolbar;
    ImageView imageView;
    String photoURI;
    EditText tagText;
    EditText postText;
    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Выводим уменьшеный вариант фотографии в ImageView
        if (photoURI != null) {
            imageLoader.displayImage(photoURI, imageView);
            Log.i("myTag", "Photo path (AddPost...): " + photoURI);
        } else {
            Log.i("myTag", "photoURI = null");
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

        if (ValidationUtils.checkTag(tag) && !text.equals("") && photoURI != null) { // Validating car number
            Toast.makeText(this, "Ok", Toast.LENGTH_LONG).show();
            SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(this);
            PostService postService = new PostService(sqLiteAdapter);
            PhotoService photoService = new PhotoService(sqLiteAdapter);
            TagService tagService = new TagService(sqLiteAdapter);

            Post post = new Post();
            Photo photo = new Photo();

            if (tagService.getTag(tag) != null) {
                Tag ctag = new Tag();
                ctag.setTagName(tag);
                tagService.insertTag(ctag);
            }

            float[] loc = getLocation(Uri.parse(photoURI));

            boolean statusGEO = loc != null;

            post.setPostText(text);
            post.setNameTag(tag);
            post.setNameUser(username);
            post.setPostDate(System.currentTimeMillis());
            if (statusGEO) post.setPostLocation(Float.toString(loc[0]) + " " + Float.toString(loc[1]));

            //Inserting Post object to DB and getting id
            long postId = postService.insertPost(post).getId();

            photo.setPhotoLink(photoURI);
            photo.setIdPost((int) postId);//todo must be long

            //Inserting Photo object to DB and getting id
            long photoId = photoService.insertPhoto(photo).getId();

            Log.i("myTag", "User: " + username);
            Log.i("myTag", "Post ID: " + Long.toString(postId));
            Log.i("myTag", "Photo ID: " + Long.toString(photoId));
            Log.i("myTag", "Photo GEO: " + Float.toString(loc[0]) + " " + Float.toString(loc[1]));

            onBackPressed();
        } else {
            Toast.makeText(this, R.string.validation_error, Toast.LENGTH_LONG).show();
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
