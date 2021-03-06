package ua.org.javatraining.automessenger.app.activities;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSource;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.fragments.*;
import ua.org.javatraining.automessenger.app.gcm.RegistrationIntentService;
import ua.org.javatraining.automessenger.app.loaders.FeedPostLoaderObserver;
import ua.org.javatraining.automessenger.app.services.*;
import ua.org.javatraining.automessenger.app.App;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity
        extends
        AppCompatActivity
        implements
        SubscriptionsFragment.CallBacks,
        PostByTagFragment.CallbackInterface,
        FeedFragment.CallBacks,
        SearchFragment.CallBacks,
        NearbyFragment.CallBacks,
        AboutFragment.CallBacks {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 314159;

    private static final int TAKE_PHOTO_REQUEST = 100;

    private GPSMonitor gpsMonitor;
    private boolean isBound = false;
    public Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RelativeLayout relativeLayout;
    private ImageButton imageButton;
    private int drawerWidth;
    private String photoPath;
    private String username;
    private LocalBroadcastManager localBroadcastManager;
    private String tag;
    private TextView usernameField;
    private SearchView searchView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("drawerWidth", drawerWidth);
        outState.putString("tag", tag);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, ConnectionMonitor.class));
        startService(new Intent(this, Uploader.class));
        startService(new Intent(this, GPSMonitor.class));

        toolbarInit();

        usernameField = (TextView) findViewById(R.id.username_field);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        relativeLayout = (RelativeLayout) findViewById(R.id.fab_pressed);
        imageButton = (ImageButton) findViewById(R.id.fab_add);
        searchView =  (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchFragment sf = new SearchFragment();

                Bundle args = new Bundle();
                args.putString(SearchFragment.QUERY, s);

                sf.setArguments(args);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, sf)
                        .addToBackStack(null)
                        .commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        searchView.setVisibility(View.GONE);
        DataSource source = DataSourceManager.getInstance().getPreferedSource(this);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        setUsername(App.getLastUser(this));

        source.setUser(username);

        if (savedInstanceState == null) {
            initUIL();
            drawerWidth = getNavDrawWidth();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
        } else {
            if (gpsMonitor == null){
                gpsMonitor = App.getGpsMonitor();
            }
            tag = savedInstanceState.getString("tag");
            drawerWidth = savedInstanceState.getInt("drawerWidth");
        }

        findViewById(R.id.drawer).getLayoutParams().width = drawerWidth;

        String lastUser = App.getLastUser(this);
        if (lastUser.equals("")) {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                    false, null, null, null, null);
            startActivityForResult(intent, App.ACCOUNT_REQUEST_CODE);
        }
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        Intent intent = new Intent(this, GPSMonitor.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        App.setGpsMonitor(gpsMonitor);
        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        usernameField.setText(App.getLastUser(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ConnectionMonitor.class));
        stopService(new Intent(this, Uploader.class));
        stopService(new Intent(this, GPSMonitor.class));
    }

    private void setUsername(String username) {
        this.username = username;
        localBroadcastManager.sendBroadcast(new Intent(FeedPostLoaderObserver.POST_UPDATED_INTENT));
    }

    private void toolbarInit() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
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
                intent.putExtra("username", username);
                startActivity(intent);
                break;
        }

    }

    //Navigation Drawer
    public void NDController(View view) {
        getSupportFragmentManager().popBackStack();
        int id = view.getId();
        searchView.setIconified(true);
        switch (id) {
            case R.id.feed_item:
                searchView.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
                break;
            case R.id.nearby_item:
                searchView.setVisibility(View.GONE);
                imageButton.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NearbyFragment()).commit();
                break;
            case R.id.subscriptions_item:
                searchView.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SubscriptionsFragment()).commit();
                break;
            case R.id.chose_acc_button:
                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                        false, null, null, null, null);
                startActivityForResult(intent, App.ACCOUNT_REQUEST_CODE);
                break;
            case R.id.item_send_feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("*/*");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"liketosleeplong@live.ru"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Automessenger feedback");
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
                break;
            case R.id.item_send_about:
                searchView.setVisibility(View.GONE);
                imageButton.setVisibility(View.GONE);
                drawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        }
    }

    //Calculating required Navigation Drawer panel width
    private int getNavDrawWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int w = (int) ((metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels) - 56 * metrics.density);
        int max = (int) (336 * metrics.density);
        return w > max ? max : w;
    }

    //Создаем файл для последующей передачи в приложение камеры, для записи в него новой фотографии
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(timeStamp, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("---", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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
            Uri photoUri = Uri.fromFile(photoFile);
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    private void initUIL() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.error)
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(displayImageOptions)
                .threadPoolSize(6)
                .memoryCacheSize(10 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
    }

    //If photo taken successfully open AddPostActivity() and pass photo path to it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, AddPostActivity.class);
            intent.putExtra("photoPath", "file:/" + photoPath);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        if (requestCode == App.ACCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            App app = ((App) getApplicationContext());
            app.setUser(accountName);
            SharedPreferences userSettings = getSharedPreferences(App.USERNAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = userSettings.edit();
            editor.putString(App.USERNAME, accountName);
            editor.apply();
            app.userAuth(this);
            setUsername(App.getLastUser(this));
        }
    }

    @Override
    public void showPostsByTag(String tag) {
        this.tag = tag;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PostByTagFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public String getTag() {
        return tag == null ? "" : tag;
    }

    @Override
    public void setDrawerItemState(boolean isHighlighted, int title) {
        View rootView = null;
        ImageView iv = null;

        switch (title) {
            case FeedFragment.FEED_FRAGMENT:
                rootView = findViewById(R.id.feed_item);
                iv = (ImageView) findViewById(R.id.feed_icon);
                toolbar.setTitle(R.string.feed);
                break;
            case NearbyFragment.NEARBY_FRAGMENT:
                rootView = findViewById(R.id.nearby_item);
                iv = (ImageView) findViewById(R.id.nearby_icon);
                toolbar.setTitle(R.string.nearby);
                break;
            case SubscriptionsFragment.SUBSCRIPTIONS_FRAGMENT:
                rootView = findViewById(R.id.subscriptions_item);
                iv = (ImageView) findViewById(R.id.subscriptions_icon);
                toolbar.setTitle(R.string.subscriptions);
                break;
            case SearchFragment.SEARCH_FRAGMENT:
                toolbar.setTitle(R.string.search);
                break;
            case AboutFragment.ABOUT_FRAGMENT:
                rootView = findViewById(R.id.item_send_about);
                iv = (ImageView) findViewById(R.id.about_icon);
                toolbar.setTitle(R.string.about);
                break;
        }

        if (rootView != null && iv != null) {
            rootView.setSelected(isHighlighted);
            if (isHighlighted) {
                iv.setColorFilter(getResources().getColor(R.color.app_blue));
            } else {
                iv.clearColorFilter();
            }
        }
    }

    @Override
    public GPSMonitor getGPSMonitor() {
        return gpsMonitor;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i("myTag", "MainActivity, GPS, onServiceConnected");

            GPSMonitor.LocalBinder binder = (GPSMonitor.LocalBinder) service;
            gpsMonitor = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i("myTag", "MainActivity, GPS, onServiceDisconnected");

            isBound = false;
        }
    };


}
