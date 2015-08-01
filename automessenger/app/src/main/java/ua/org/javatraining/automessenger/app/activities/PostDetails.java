package ua.org.javatraining.automessenger.app.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import ua.org.javatraining.automessenger.app.R;
import ua.org.javatraining.automessenger.app.adapters.CommentsAdapter;
import ua.org.javatraining.automessenger.app.entities.Comment;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.loaders.CommentLoader;
import ua.org.javatraining.automessenger.app.services.DataSource;
import ua.org.javatraining.automessenger.app.services.DataSourceManager;
import ua.org.javatraining.automessenger.app.vo.CommentGrades;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.PostGrades;
import ua.org.javatraining.automessenger.app.vo.SuperComment;

import java.util.ArrayList;
import java.util.List;

public class PostDetails
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<SuperComment>> {

    private static final int COMMENT_LOADER_ID = 2;

    private EditText addCommentField;
    private ImageButton submit;
    private RecyclerView.Adapter myAdapter;
    private List<SuperComment> comments = new ArrayList<SuperComment>();
    private long postId;
    private DataSource source;
    private Subscription subscription;
    private FullPost fullPost;
    private PostGrades postGrades;

    private Loader<List<SuperComment>> mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
        addCommentField = (EditText) findViewById(R.id.add_comment_field);
        postId = getIntent().getLongExtra("POST_ID", 0);
        submit = (ImageButton) findViewById(R.id.submit);

        source = DataSourceManager.getSource(this);

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

        loadPostForList();
        initSubscription();
        initToolbar();
        mLoader = getSupportLoaderManager().initLoader(COMMENT_LOADER_ID, null, this);
        initCommentsList();

    }

    private boolean initSubscription() {
        boolean status = false;

        List<Subscription> subs = source.getSubscriptions();

        for (Subscription s : subs) {
            if (s.getTagId().equals(fullPost.getTag())) {
                subscription = s;
                status = true;
                break;
            }
        }
        return status;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(COMMENT_LOADER_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoader.onContentChanged();
    }

    private void loadPostForList() {
        if (postId != 0) {
            fullPost = source.getPostByID(postId);
            postGrades = source.getPostGrades(postId);
        }
    }

    private void initCommentsList() {
        RecyclerView myRV = (RecyclerView) findViewById(R.id.comment_rv);
        RecyclerView.LayoutManager myLM = new LinearLayoutManager(this);
        myRV.setLayoutManager(myLM);
        myAdapter = new CommentsAdapter(this.getApplicationContext(), fullPost, comments, postGrades, this);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_post_details);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setTitle(fullPost.getTag());
        if (subscription != null) {
            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_star_rate_white_24dp);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setPostRate(int rate) {
        source.setCurrentUserPostGrade(postId, rate);
    }


    public void setCommentRate(long commentID, int grade) {
        source.setCurrentUserCommentGrade(commentID, grade);
    }

    public CommentGrades getCommentGrades(int id) {
        return source.getCommentGrades(id);
    }

    public PostGrades getPostGrades() {
        return source.getPostGrades(postId);
    }

    public void actionSubscribe(MenuItem item) {
        if (subscription == null) {
            subscription = source.addSubscription(fullPost.getTag());
            if (!(subscription.getId() >= 1)) {
                subscription = null;
            } else {
                item.setIcon(R.drawable.ic_star_rate_white_24dp);
            }


        } else {
            source.removeSubscription(subscription);
            subscription = null;
            item.setIcon(R.drawable.ic_star_outline_white_24dp);
        }
    }

    public void actionCommentSend(View view) {
        String comment = addCommentField.getText().toString();
        if (!comment.equals("")) {
            Comment commentObj = new Comment();
            commentObj.setCommentDate(System.currentTimeMillis());
            commentObj.setCommentText(comment);
            commentObj.setPostId(postId);
            long id = source.addComment(commentObj);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(addCommentField.getWindowToken(), 0);
            Log.i("myTag", "comment added. id: " + Long.toString(id));
            addCommentField.setText("");
            mLoader.onContentChanged();
        }
    }

    @Override
    public Loader<List<SuperComment>> onCreateLoader(int id, Bundle args) {
        return new CommentLoader(this, postId);
    }

    @Override
    public void onLoadFinished(Loader<List<SuperComment>> loader, List<SuperComment> data) {
        comments.clear();
        comments.addAll(data);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<SuperComment>> loader) {

    }

}
