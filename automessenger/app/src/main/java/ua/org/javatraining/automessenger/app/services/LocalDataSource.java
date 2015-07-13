package ua.org.javatraining.automessenger.app.services;

import android.content.Context;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entities.Comment;
import ua.org.javatraining.automessenger.app.entities.GradeComment;
import ua.org.javatraining.automessenger.app.entities.GradePost;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.vo.CommentGrades;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.PostGrades;

import java.util.List;

public class LocalDataSource implements DataSourceInterface {

    private CommentService commentService;
    private PostService postService;
    private PhotoService photoService;
    private SubscriptionService subscriptionService;
    private TagService tagService;
    private UserService userService;
    private GradeCommentService gradeCommentService;
    private GradePostService gradePostService;

    public LocalDataSource(Context context) {
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(context);
        commentService = new CommentService(sqLiteAdapter);
        postService = new PostService(sqLiteAdapter);
        photoService = new PhotoService(sqLiteAdapter);
        subscriptionService = new SubscriptionService(sqLiteAdapter);
        tagService = new TagService(sqLiteAdapter);
        userService = new UserService(sqLiteAdapter);
        gradeCommentService = new GradeCommentService(sqLiteAdapter);
        gradePostService = new GradePostService(sqLiteAdapter);
    }

    @Override
    public FullPost getPostByID(long postID) {
        return null;
    }

    @Override
    public List<FullPost> getPostsFromSubscriptions() {
        return null;
    }

    @Override
    public List<FullPost> getPostsFromSubscriptions(long timestamp) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByLocation(String country, String adminArea, String city) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByLocation(String country, String adminArea, String city, long timestamp) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByTagName(String tagName) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByTagName(String tagName, long timestamp) {
        return null;
    }

    @Override
    public long addPost(FullPost fullPost) {
        return 0;
    }

    @Override
    public List<Comment> getComments(long postID) {
        return null;
    }

    @Override
    public List<Comment> getComments(long postID, long timestamp) {
        return null;
    }

    @Override
    public long addComment(Comment comment) {
        return 0;
    }

    @Override
    public List<Subscription> getSubscribtions() {
        return null;
    }

    @Override
    public long addSubscription(String tag) {
        return 0;
    }

    @Override
    public void removeSubscription(Subscription subscription) {

    }

    @Override
    public PostGrades getPostGrades(long postID) {
        return null;
    }

    @Override
    public GradePost getCurrentUserPostGrade(long postID) {
        return null;
    }

    @Override
    public long setCurrentUserPostGrade(long postID, int grade) {
        return 0;
    }

    @Override
    public CommentGrades getCommentGrades(long commentID) {
        return null;
    }

    @Override
    public GradeComment getCurrentUserCommentGrade(long commentID) {
        return null;
    }

    @Override
    public long setCurrentUserCommentGrade(long commentID, int grade) {
        return 0;
    }
}

