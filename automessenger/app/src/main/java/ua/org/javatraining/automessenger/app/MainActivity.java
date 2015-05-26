package ua.org.javatraining.automessenger.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    RelativeLayout relativeLayout;
    ImageButton imageButton;
    int drawerWidth;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("drawerWidth", drawerWidth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white87));
        toolbar.setNavigationIcon(R.drawable.ic_action);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
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
        int result = (int) ((metrics.widthPixels < metrics.heightPixels ? metrics.widthPixels : metrics.heightPixels) - 56 * metrics.density);
        return result;
    }
}
