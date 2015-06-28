package ua.org.javatraining.automessenger.app.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import ua.org.javatraining.automessenger.app.CommentsAdapter;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.database.CommentService;
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
    EditText addCommentField;
    ImageView photo;
    TextView dateTextView;
    ImageButton submit;
    View postDetails;
    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;
    ImageLoader imageLoader = ImageLoader.getInstance();
    int postDetailsHeight;
    List<Comment> comments = new ArrayList<Comment>();
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
        addCommentField = (EditText) findViewById(R.id.add_comment_field);
        postId = getIntent().getLongExtra("POST_ID", 0);
        sqLiteAdapter = SQLiteAdapter.initInstance(this);
        submit = (ImageButton) findViewById(R.id.submit);

        addCommentField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });

        initToolbar();
        updateCommentsDataset();
        initCommentsList();
        loadPost();
    }

    private void loadPost() {
        if (postId != 0) {
            postService = new PostService(sqLiteAdapter);
            photoService = new PhotoService(sqLiteAdapter);

            List<Post> posts = postService.getPostsFromSubscribes("user_test");  //Здесь все очень не правильно,
            for (Post post : posts) {                                            //но работает.
                if (post.getId() == postId)                                      //Это временная мера,
                    postObj = post;                                              //пока не напишется метод
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
        myLM = new LinearLayoutManager(this);
        myRV.setLayoutManager(myLM);
        myAdapter = new CommentsAdapter(this.getApplicationContext(), comments);
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
        Uri uri = Uri.parse(photoObj.getPhotoLink());
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
    private void updateCommentsDataset() {
        if (postId != 0) {
            CommentService commentService = new CommentService(sqLiteAdapter);
            comments.clear();
            comments.addAll(commentService.getAllComments((int) postId)); //todo remove cast to int when DB will be OK
        }
    }

    public void actionSubscribe(MenuItem item) {

    }

    public void actionCommentSend(View view) {
        String comment = addCommentField.getText().toString();
        if (!comment.equals("")) {
            CommentService commentService = new CommentService(sqLiteAdapter);
            Comment commentObj = new Comment();
            commentObj.setCommentDate((int) System.currentTimeMillis()); //todo remove cast to int when DB will be OK
            commentObj.setCommentText(comment);
            commentObj.setIdPost((int) postId); //todo remove cast to int when DB will be OK
            commentObj.setNameUser("user_test");
            long id = commentService.insertComment(commentObj).getId();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(addCommentField.getWindowToken(), 0);
            Log.i("myTag", "comment added. id: " + Long.toString(id));
            addCommentField.setText("");
            updateCommentsDataset();
        }
    }
}
