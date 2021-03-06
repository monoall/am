package ua.org.javatraining.automessenger.app.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.location.*;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.services.GPSMonitor;
import ua.org.javatraining.automessenger.app.services.GoogleDriveAuth;
import ua.org.javatraining.automessenger.app.utils.PhotoUtils;
import ua.org.javatraining.automessenger.app.utils.ValidationUtils;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.ShortLocation;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddPostActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;
    private static final java.lang.String PHOTO_URI_VALUE = "photoURI";

    private GPSMonitor gpsMonitor;
    private boolean isBound = false;

    private ProgressBar pBar;
    private ImageView imageView;
    private View addPhotoButton;
    private EditText tagText;
    private EditText postText;

    private ImageLoader imageLoader;
    private Geocoder geocoder;
    private AddressLoader addressLoader;
    private InsertTask insertTask;
    private ShortLocation sl = null;

    private float[] loc;
    private String photoURI;
    private String tag;
    private String text;
    private String photoURL;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PHOTO_URI_VALUE, photoURI);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (photoURI != null) {
            //imageView.setImageURI(Uri.parse("content://" + photoURI));
            imageLoader.displayImage(photoURI, imageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        imageView = (ImageView) findViewById(R.id.photo);
        tagText = (EditText) findViewById(R.id.car_number);
        postText = (EditText) findViewById(R.id.post_description);
        pBar = (ProgressBar) findViewById(R.id.marker_progress);
        addPhotoButton = findViewById(R.id.add_photo_button);

        imageLoader = ImageLoader.getInstance();
        geocoder = new Geocoder(this, Locale.ENGLISH);

        if (savedInstanceState == null) {
            photoURI = getIntent().getStringExtra("photoPath");
        } else {
            photoURI = savedInstanceState.getString(PHOTO_URI_VALUE);
        }

        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, GPSMonitor.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_add_post);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (insertTask != null && !addressLoader.isCancelled()) {
            insertTask.cancel(true);
        }

        if (addressLoader != null && !addressLoader.isCancelled()) {
            addressLoader.cancel(true);
        }

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    photoURI = imageReturnedIntent.getData().toString();
                    Log.i("myTag", photoURI);

                    //imageView.setImageURI(Uri.parse(photoURI));
                    imageLoader.displayImage(photoURI, imageView);
                }
        }
    }

    /**
     * Called when "add photo" button pressed.
     * Send implicit intent to pick photo from gallery or similar app.
     */
    public void addPhotoPressed(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    /**
     * Firs step of adding post, validating data and upload photo to Drive.
     * Called when "Done" button pressed.
     */
    public void prepare(MenuItem item) {

        tag = tagText.getText().toString().replaceAll(" ", "").toUpperCase();
        text = postText.getText().toString();

        if (ValidationUtils.checkTag(tag) && !text.equals("") && photoURI != null) {
            //Disable "Done" button
            item.setEnabled(false);

            //Get altitude and longitude
            loc = getLocation(Uri.parse(photoURI));
            Log.i("mytag", "AddPostActivity, prepare, loc = " + loc[0] + " " + loc[1]);

            //Get actual address from altitude and longitude
            if (loc[0] != 0 && loc[1] != 0) {
                addressLoader = new AddressLoader();
                addressLoader.execute(loc);

                try {
                    sl = addressLoader.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (!addressLoader.isCancelled()) {
                    addressLoader.cancel(true);
                }
            }

            //Upload photo to Google Drive
            uploadPhotoToDrive(getFilePathFromUri(Uri.parse(photoURI)));
            Log.i("mytag", "AddPostActivity, prepare, photoURI = " + photoURI);
        } else {
            Toast.makeText(this, R.string.validation_error, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Getting coordinates for post.
     * First checks exif data in photo, if its empty takes coordinates from LocationManager.
     *
     * @param uri photo
     * @return array with altitude and longitude
     */
    private float[] getLocation(Uri uri) {
        float[] result = new float[2];

        //Trying to get location from photo
        if (uri != null) {
            try {
                String filename = getFilePathFromUri(uri);
                Log.i("myTag", "AddPostActivity, getLocation, filename = " + filename);
                ExifInterface exif = new ExifInterface(filename);
                exif.getLatLong(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        //If photo didn't contain coordinates, trying to take it from current device
        if (result[0] == 0 && result[1] == 0) {
            if (gpsMonitor != null) {
                Location location = gpsMonitor.getLocation();
                if (location != null) {
                    result[0] = (float) location.getLatitude();
                    result[1] = (float) location.getLongitude();

                    Log.i("myTag", "AddPostActivity, getLocation, getting location from GPSMonitor(lat:" + result[0] + ", lon:" + result[1]);
                }
            }
        }

        return result;
    }

    /**
     * Write post to DB
     */
    public void makePost() {
        FullPost fPost = new FullPost();

        boolean statusGEO = (loc[0] != 0 && loc[1] != 0);

        fPost.setText(text);
        fPost.setTag(tag);
        fPost.setDate(System.currentTimeMillis());
        fPost.getPhotos().add(photoURL);

        if (statusGEO) {
            fPost.setPostLocation(Float.toString(loc[0]) + " " + Float.toString(loc[1]));
            if (sl != null) {
                fPost.setLocCountry(sl.getCountry());
                fPost.setLocAdminArea(sl.getAdminArea());
                fPost.setLocRegion(sl.getRegion());
            }
        }

        DataSourceManager.getInstance().getPreferedSource(this).addPost(fPost);
        DataSourceManager.getInstance().getPreferedSource(this).addSubscription(tag);
        onBackPressed();
    }

    private class AddressLoader extends AsyncTask<float[], Void, ShortLocation> {
        @Override
        protected ShortLocation doInBackground(float[]... params) {
            ShortLocation loc = new ShortLocation();
            try {
                Address address = geocoder.getFromLocation(params[0][0], params[0][1], 1).get(0);
                loc.setCountry(address.getCountryName());
                Log.i("mytag", "AddPostActivity, prepare, LocCountry = " + loc.getCountry());
                loc.setAdminArea(address.getAdminArea());
                Log.i("mytag", "AddPostActivity, prepare, LocAdminArea = " + loc.getAdminArea());
                if (address.getSubAdminArea() != null) {
                    loc.setRegion(address.getSubAdminArea());
                    Log.i("mytag", "AddPostActivity, prepare, LocRegion = " + loc.getRegion());
                } else {
                    loc.setRegion(address.getLocality());
                    Log.i("mytag", "AddPostActivity, prepare, LocRegion = " + loc.getRegion());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                return loc;
            }
            return loc;
        }

        @Override
        protected void onPostExecute(ShortLocation shortLocation) {
            super.onPostExecute(shortLocation);
        }
    }

    private class InsertTask extends AsyncTask<Void, Void, String> {

        com.google.api.services.drive.Drive mService;
        private String filePath;

        public InsertTask(com.google.api.services.drive.Drive mService, String path) {
            this.mService = mService;
            this.filePath = path;
        }

        @Override
        protected void onPreExecute() {
            Log.i("mytag", "AddPostActivity, InsertTask, onPreExecute");

            super.onPreExecute();

            addPhotoButton.setClickable(false);
            tagText.setEnabled(false);
            postText.setEnabled(false);

            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("mytag", "AddPostActivity, InsertTask, onPostExecute");

            photoURL = s;

            makePost();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.i("mytag", "AddPostActivity, InsertTask, doInBackground");


            return insertPhoto(filePath, mService);
        }

        /**
         * inserts photo to google drive
         *
         * @param filePath path of the photo
         * @param mService service to upload
         * @return url of the inserted file
         */
        private String insertPhoto(String filePath, Drive mService) {
            com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
            body.setTitle("Title");
            body.setDescription("Photo");

            Log.i("myTag", "insertTask, filePath: " + filePath);

            java.io.File fileContent = new java.io.File(PhotoUtils.getResizedPhotoFile(getApplicationContext(), filePath));
            FileContent mediaContent = new FileContent(null, fileContent);
            com.google.api.services.drive.model.File file;

            try {
                file = mService.files().insert(body, mediaContent).execute();

                Log.i("myTag", "insertTask, downloadURL: " + file.getWebContentLink());

                Permission newPermission = new Permission();
                newPermission.setType("anyone");
                newPermission.setRole("reader");
                mService.permissions().insert(file.getId(), newPermission).execute();
                String[] res = file.getWebContentLink().split("&");
                return res[0];
            } catch (IOException e) {
                e.printStackTrace();
                return filePath;
            }
        }

    }

    /**
     * Convert Uri to actual file path.
     * Can work with "content://" and "file://" Uri types.
     *
     * @param uri file Uri
     * @return file path
     */
    private String getFilePathFromUri(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("file")) {
            result = uri.getHost() + uri.getPath();

        } else {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);

            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(column_index);
            }

            cursor.close();
        }

        return result;
    }

    /**
     * Upload photo to google Drive
     *
     * @param filePath of the photo to upload
     */
    private void uploadPhotoToDrive(String filePath) {
        GoogleDriveAuth gauth = new GoogleDriveAuth();
        Drive drive = gauth.init(this);
        insertTask = new InsertTask(drive, filePath);
        insertTask.execute();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            GPSMonitor.LocalBinder binder = (GPSMonitor.LocalBinder) service;
            gpsMonitor = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
}
