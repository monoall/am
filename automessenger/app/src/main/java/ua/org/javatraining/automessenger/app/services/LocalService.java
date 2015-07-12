package ua.org.javatraining.automessenger.app.services;

import android.content.Context;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entities.Post;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.List;

public class LocalService implements IService {

    private CommentService commentService;
    private PostService postService;
    private PhotoService photoService;
    private SubscriptionService subscriptionService;
    private TagService tagService;
    private UserService userService;
    private GradeCommentService gradeCommentService;
    private GradePostService gradePostService;

    public LocalService(Context context) {
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
}

