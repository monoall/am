package ua.org.javatraining.automessenger.app.activities;


import android.annotation.SuppressLint;
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
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.TagService;
import ua.org.javatraining.automessenger.app.database.UserService;
import ua.org.javatraining.automessenger.app.entityes.Tag;
import ua.org.javatraining.automessenger.app.entityes.User;
import ua.org.javatraining.automessenger.app.fragments.FeedFragment;
import ua.org.javatraining.automessenger.app.fragments.NearbyFragment;
import ua.org.javatraining.automessenger.app.fragments.SearchFragment;
import ua.org.javatraining.automessenger.app.fragments.SubscriptionsFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        //////////////////////////////////////////////////////
        //dbTest();
        test();
        //////////////////////////////////////////////////////

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
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
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
        }
    }

    //Временный метод для проверки активности
    public void spda(View view) {
        startActivity(new Intent(this, PostDetails.class));
    }


   /*private void dbTest(){
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(this);
        UserService userService = new UserService(sqLiteAdapter);
        TagService tagService = new TagService(sqLiteAdapter);
        SubscriptionService subscriptionService = new SubscriptionService(sqLiteAdapter);
        PostService postService = new PostService(sqLiteAdapter);
        CommentService commentService = new CommentService(sqLiteAdapter);
        GradeCommentService gradeCommentService = new GradeCommentService(sqLiteAdapter);
        GradePostService gradePostService = new GradePostService(sqLiteAdapter);
        PhotoService photoService = new PhotoService(sqLiteAdapter);
        User user = new User();
        user.setName("Tom");
        Log.d("myTags", "User insert " + userService.insertUser(user).getName());
        Tag tag = new Tag();
        tag.setTagName("BE1");
        tagService.insertTag(tag);
        Log.d("myTags", "Tag insert " + tagService.insertTag(tag).getTagName());
        Post post1 = new Post();
        post1.setPostText("Some Text1");
        post1.setPostLocation("location1");
        post1.setPostDate(120215);
        User chekUser = userService.getUser("Tom");
        Tag checkTag = tagService.getTag("BE1");
        post1.setNameUser(chekUser.getName());
        post1.setNameTag(checkTag.getTagName());
        Log.d("myTags", "Post insert " + postService.insertPost(post1).getId());
        ArrayList<Post> p = postService.getAllPosts( chekUser.getName(), checkTag.getTagName());
        Post checkPost = p.get(0);
        Log.d("myTags", "Post text " + checkPost.getPostText());
    }*/

   void test(){
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(this);
        UserService userService = new UserService(sqLiteAdapter);
        User user = new User();
        user.setName("Tom");
        userService.insertUser(user);
        User check = userService.getUser("Tom");
        Log.d("myTags", "User " + check.getName());
        TagService tagService = new TagService(sqLiteAdapter);
        Tag tag = new Tag();
        tag.setTagName("BE");
        tagService.insertTag(tag);
        Tag checkTag = tagService.getTag("BE");
        Log.d("myTags", "Tag " + checkTag.getTagName());
    }

}
