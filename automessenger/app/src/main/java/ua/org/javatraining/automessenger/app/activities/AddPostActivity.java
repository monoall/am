package ua.org.javatraining.automessenger.app.activities;

import android.content.Intent;
import android.os.Bundle;
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
import ua.org.javatraining.automessenger.app.entityes.Photo;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.entityes.Tag;
import ua.org.javatraining.automessenger.app.utils.ValidationUtils;

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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        photoURI = getIntent().getStringExtra("photoURI");
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
        String tag = tagText.getText().toString();
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

            post.setPostText(text);
            post.setNameTag(tag);
            post.setNameUser(username);
            post.setPostDate((int)System.currentTimeMillis());
            post.setPostLocation("mk.ua");

            //Inserting Post object to DB and getting id
            long postId = postService.insertPost(post).getId();

            photo.setPhotoLink(photoURI);
            photo.setIdPost((int)postId);//todo must be long

            //Inserting Photo object to DB and getting id
            long photoId = photoService.insertPhoto(photo).getId();

            Log.i("myTag", "User: " + username.toString());
            Log.i("myTag", "Post ID: " + Long.toString(postId));
            Log.i("myTag", "Photo ID: " + Long.toString(photoId));

            onBackPressed();
        } else {
            Toast.makeText(this, R.string.validation_error, Toast.LENGTH_LONG).show();
        }
    }
}
