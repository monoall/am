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
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.ArrayList;
import java.util.List;

public class PostDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView descriptionTextView;
    EditText addCommentField;
    ImageButton submit;
    RecyclerView myRV;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager myLM;
    List<Comment> comments = new ArrayList<Comment>();
    long postId;
    SQLiteAdapter sqLiteAdapter;
    PostService postService;
    PhotoService photoService;
    Photo photoObj;
    FullPost fullPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
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
        loadPostForList();
        initCommentsList();
    }


    private void loadPostForList(){
        if (postId != 0) {
            postService = new PostService(sqLiteAdapter);
            photoService = new PhotoService(sqLiteAdapter);

            List<Post> posts = postService.getPostsFromSubscribes("user_test");  //Здесь все очень не правильно,
            for (Post post : posts) {                                            //но работает.
                if (post.getId() == postId)                                      //Это временная мера,
                    fullPost = new FullPost(post);                               //пока не напишется метод
            }                                                                    //получения поста по ID поста.

            photoObj = photoService.getPhoto((int) postId);
            fullPost.getPhotos().add(photoObj.getPhotoLink());
            toolbar.setTitle(fullPost.getTag());
        }
    }

    private void initCommentsList() {
        myRV = (RecyclerView) findViewById(R.id.comment_rv);
        myLM = new LinearLayoutManager(this);
        myRV.setLayoutManager(myLM);
        myAdapter = new CommentsAdapter(this.getApplicationContext(), fullPost, comments);
        myRV.setAdapter(myAdapter);
    }

    //Нажата кнопка "share"
    public void actionShare(View view) {
        String extraText = fullPost.getText();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        Uri uri = Uri.parse(fullPost.getPhotos().get(0));
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
