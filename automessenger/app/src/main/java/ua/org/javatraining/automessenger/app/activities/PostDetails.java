package ua.org.javatraining.automessenger.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.CommentsAdapter;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.database.PhotoService;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Comment;
import ua.org.javatraining.automessenger.app.entityes.Photo;
import ua.org.javatraining.automessenger.app.entityes.Post;
import ua.org.javatraining.automessenger.app.utils.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

public class PostDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView descriptionTextView;
    ImageView photo;
    TextView dateTextView;
    View postDetails;
    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;
    ImageLoader imageLoader = ImageLoader.getInstance();
    int postDetailsHeight;
    List<Comment> comments;
    long postId;
    SQLiteAdapter sqLiteAdapter;
    PostService postService;
    PhotoService photoService;
    Post postObj;
    Photo photoObj;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        postDetailsHeight = postDetails.getHeight();
        myRV.setPadding(0, postDetailsHeight, 0, 0);
        myLM.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
        photo = (ImageView) findViewById(R.id.photo);
        postDetails = findViewById(R.id.post_details);
        descriptionTextView = (TextView) findViewById(R.id.description_text);
        dateTextView = (TextView) findViewById(R.id.date_text);

        initToolbar();
        initTestData();
        initCommentsList();
        loadPost();
    }

    private void loadPost() {
        postId = getIntent().getLongExtra("POST_ID", 0);
        if (postId != 0) {
            sqLiteAdapter = SQLiteAdapter.initInstance(this);
            postService = new PostService(sqLiteAdapter);
            photoService = new PhotoService(sqLiteAdapter);

            List<Post> posts = postService.getPostsFromSubscribes("user_test");  //Здесь все очень не правильно,
            for(int i = 0; i<posts.size(); i++){                                 //но работает.
                if (posts.get(i).getId() == postId)                              //Это временная мера,
                    postObj = posts.get(i);                                      //пока не напишется метод
            }                                                                    //получения поста по ID поста.

            photoObj = photoService.getPhoto((int) postId);

            Log.i("myTag", "postObf is: " + postObj.toString() + " | " + "photoObj is: " + photoObj.toString());

            toolbar.setTitle(postObj.getNameTag());
            imageLoader.displayImage(photoObj.getPhotoLink(), photo);
            descriptionTextView.setText(postObj.getPostText());
            dateTextView.setText(DateFormatUtil.toReadable(this, postObj.getPostDate()));
        }
    }

    private void initCommentsList() {
        myRV = (RecyclerView) findViewById(R.id.comment_rv);
        myRV.setHasFixedSize(true);
        myLM = new LinearLayoutManager(this);
        myRV.setLayoutManager(myLM);
        myAdapter = new CommentsAdapter(comments);
        myRV.setAdapter(myAdapter);
        myRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mOffset = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                clipOffset();
                onMoved(mOffset);
                if ((mOffset < postDetailsHeight && dy > 0) || (mOffset > 0 && dy < 0)) {
                    mOffset += dy;
                }
            }

            private void clipOffset() {
                if (mOffset > postDetailsHeight) {
                    mOffset = postDetailsHeight;
                } else if (mOffset < 0) {
                    mOffset = 0;
                }
            }

            public void onMoved(int distance) {
                postDetails.setTranslationY(-distance);
            }
        });
    }

    //Нажата кнопка "share"
    public void actionShare(View view) {
        String extraText = descriptionTextView.getText().toString();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        // В качестве примера используем картинку из ресурсов приложения
        // todo В конечном варианте c рабочай базой данных должны брать изображение из поста
        Uri uri = Uri.parse("android.resource://ua.org.javatraining.automessenger.app/drawable/myimg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_post_details);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //нажата кнопка оценки
    public void actionSetRate(View view) {
    }

    //initialize dataset with test data
    private void initTestData() {
        comments = new ArrayList<Comment>();
        for (int i = 0; i <= 10; i++) {
            Comment c = new Comment();
            c.setCommentText("Comment " + Integer.toString(i));
            comments.add(c);
        }
    }

    public void actionSubscribe(MenuItem item) {
    }
}
