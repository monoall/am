package ua.org.javatraining.automessenger.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import java.util.Date;

public class AddPostActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;

    private String username;
    Toolbar toolbar;
    ImageView imageView;
    String photoURI;
    String selectedImageURI;
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

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    selectedImageURI = imageReturnedIntent.getData().toString();
                    Log.i("",selectedImageURI);
                    imageLoader.displayImage(selectedImageURI, imageView);
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

        System.currentTimeMillis();

        if(ValidationUtils.checkTag(tag) && !text.equals("")) { // Validating car number
            Toast.makeText(this, "Ok", Toast.LENGTH_LONG).show();
            SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(this);
            PostService postService = new PostService(sqLiteAdapter);
            PhotoService photoService = new PhotoService(sqLiteAdapter);
            TagService tagService = new TagService(sqLiteAdapter);

            Post post = new Post();
            Photo photo = new Photo();
            Tag ctag = new Tag();

            ctag.setTagName(tag);
            tagService.insertTag(ctag);

            post.setPostText(text);
            post.setNameTag(tag);
            post.setNameUser(username);
            post.setPostDate(11111);//todo Change type of Date field to long in Post() class and replace "11111" with real timestamp
            post.setPostLocation("mk.ua");

            postService.insertPost(post);

            onBackPressed();
        }else{
            Toast.makeText(this, R.string.validation_error, Toast.LENGTH_LONG).show();
        }
    }
}
