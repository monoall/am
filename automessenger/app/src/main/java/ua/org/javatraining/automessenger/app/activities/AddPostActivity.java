package ua.org.javatraining.automessenger.app.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.api.services.drive.Drive;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.services.GoogleDriveAuth;
import ua.org.javatraining.automessenger.app.services.InsertTask;
import ua.org.javatraining.automessenger.app.utils.ValidationUtils;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.ShortLocation;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddPostActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;

    private ImageView imageView;
    private String photoURI;
    private EditText tagText;
    private EditText postText;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DataSource source;
    private Geocoder geocoder;
    private ShortLocation sl = null;
    private AddressLoader addressLoader;
    private float[] loc;

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
        String username = getIntent().getStringExtra("username");

        imageView = (ImageView) findViewById(R.id.photo);
        tagText = (EditText) findViewById(R.id.car_number);
        postText = (EditText) findViewById(R.id.post_description);

        source = DataSourceManager.getSource(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add_post);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        geocoder = new Geocoder(this, Locale.ENGLISH);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (addressLoader != null && !addressLoader.isCancelled()) {
            addressLoader.cancel(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    photoURI = imageReturnedIntent.getData().toString();
                    Log.i("", photoURI);
                    loc = getLocation(Uri.parse(photoURI));

                    if (loc != null) {
                        Log.i("mytag", "task executed");
                        addressLoader = new AddressLoader();
                        addressLoader.execute(loc);
                    }

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
        String tag = tagText.getText().toString().replaceAll(" ", "").toUpperCase();
        String text = postText.getText().toString();

        if (ValidationUtils.checkTag(tag) && !text.equals("") && photoURI != null) {
            FullPost fPost = new FullPost();

            boolean statusGEO = loc != null;

            //todo problem here
            uploadPhotoToDrive(Uri.parse(photoURI));

            fPost.setText(text);
            fPost.setTag(tag);
            fPost.setDate(System.currentTimeMillis());
            fPost.getPhotos().add(photoURI);
            if (statusGEO) {
                fPost.setPostLocation(Float.toString(loc[0]) + " " + Float.toString(loc[1]));
                if (sl != null) {
                    fPost.setLocCountry(sl.getCountry());
                    fPost.setLocAdminArea(sl.getAdminArea());
                    fPost.setLocRegion(sl.getRegion());
                }
            }
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

    private class AddressLoader extends AsyncTask<float[], Void, ShortLocation> {
        @Override
        protected ShortLocation doInBackground(float[]... params) {
            ShortLocation loc = null;
            try {
                loc = new ShortLocation();
                Address address = geocoder.getFromLocation(params[0][0], params[0][1], 1).get(0);
                loc.setCountry(address.getCountryName());
                Log.i("mytag", "AddPostActivity, donePressed, LocCountry = " + loc.getCountry());
                loc.setAdminArea(address.getAdminArea());
                Log.i("mytag", "AddPostActivity, donePressed, LocAdminArea = " + loc.getAdminArea());
                if (address.getSubAdminArea() != null) {
                    loc.setRegion(address.getSubAdminArea());
                    Log.i("mytag", "AddPostActivity, donePressed, LocRegion = " + loc.getRegion());
                } else {
                    loc.setRegion(address.getLocality());
                    Log.i("mytag", "AddPostActivity, donePressed, LocRegion = " + loc.getRegion());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return loc;
        }

        @Override
        protected void onPostExecute(ShortLocation shortLocation) {
            super.onPostExecute(shortLocation);
            sl = shortLocation;
        }
    }

    /**
     * Upload photo to google Drive
     * @param uri uri of the photo to upload
     * @return global uri of the uploaded photo
     */
    private String uploadPhotoToDrive(Uri uri){
        GoogleDriveAuth gauth = new GoogleDriveAuth();
        Drive drive = gauth.init(this);
        InsertTask it = new InsertTask(drive, uri);
        it.execute();

        String photoUrl = "url";
        try {
            photoUrl = it.get();
        } catch (InterruptedException e) {
            System.out.println("Error " + e);
        } catch (ExecutionException e) {
            System.out.println("Error " + e);
        }
        Log.i("mytag", "photoUrl " + photoUrl);

        return photoUrl;
    }

}
