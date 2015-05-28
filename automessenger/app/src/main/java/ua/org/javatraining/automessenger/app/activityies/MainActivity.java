package ua.org.javatraining.automessenger.app.activityies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.fragments.FeedFragment;
import ua.org.javatraining.automessenger.app.fragments.NearbyFragment;
import ua.org.javatraining.automessenger.app.fragments.SearchFragment;
import ua.org.javatraining.automessenger.app.fragments.SubscriptionsFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    DrawerLayout drawerLayout;
    RelativeLayout relativeLayout;
    ImageButton imageButton;
    int drawerWidth;
    String photoPath;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("drawerWidth", drawerWidth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarInit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.fab_pressed);
        imageButton = (ImageButton) findViewById(R.id.fab_add);

        if(savedInstanceState == null){
            drawerWidth = getNavDrawWidth();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
        }else {
            drawerWidth = savedInstanceState.getInt("drawerWidth");
        }

        findViewById(R.id.drawer).getLayoutParams().width = drawerWidth;

    }

    private void toolbarInit(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white87));
        toolbar.setNavigationIcon(R.drawable.ic_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    //Floating Action Button
    public void FABController(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.fab_add:
                imageButton.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                //Work here
                break;
            case R.id.fab_cancel:
                imageButton.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                //Work here
                break;
            case R.id.fab_camera:
                imageButton.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                //Work here
                goCamera();
                break;
            case R.id.fab_pen:
                imageButton.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                //Work here
                Intent intent = new Intent(this, AddPostActivity.class);
                startActivity(intent);
                break;
        }

    }

    //Navigation Drawer
    public void NDController(View view) {
        int id = view.getId();
        switch (id){
            case R.id.item_feed:
                //Work here
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
                break;
            case R.id.item_nearby:
                //Work here
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NearbyFragment()).commit();
                break;
            case R.id.item_subscriptions:
                //Work here
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SubscriptionsFragment()).commit();
                break;
            case R.id.item_search:
                //Work here
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                break;
        }
    }

    //Calculating required Navigation Drawer panel width
    private int getNavDrawWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) ((metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels) - 56 * metrics.density);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(timeStamp, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }

    //Starting camera app to take photo
    public void goCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, 1);
            }
        }
    }

    //If photo taken successfully open AddPostActivity() and pass photo path to it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, AddPostActivity.class);
            intent.putExtra("photoPath", photoPath);
            startActivity(intent);


            /*
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            Bitmap bm = BitmapFactory.decodeFile(photoPath, options);

            if (bm != null)
                imageView.setImageBitmap(bm);
                */
        }
    }

}
