package ua.org.javatraining.automessenger.app.dataSourceServices;

import android.content.Context;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ua.org.javatraining.automessenger.app.entities.*;
import ua.org.javatraining.automessenger.app.vo.*;

import java.util.List;

public class RemoteDataSource implements DataSource {

    private Context context;

    public RemoteDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void setUser(String username) {

        User user = new User();
        user.setName(username);

        final String url = "http://backend-automessenger.rhcloud.com/user";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForObject(url, user, User.class);


    }

    @Override
    public FullPost getPostByID(long postID) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByAuthor() {
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
    public List<FullPost> getPostsByLocation(ShortLocation shortLocation) {
        return null;
    }

    @Override
    public List<FullPost> getPostsByLocation(ShortLocation shortLocation, long timestamp) {
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
    public List<SuperComment> getComments(long postID) {
        return null;
    }

    @Override
    public List<SuperComment> getComments(long postID, long timestamp) {
        return null;
    }

    @Override
    public long addComment(Comment comment) {
        return 0;
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return null;
    }

    @Override
    public Subscription addSubscription(String tag) {
        return null;
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
    public void setCurrentUserPostGrade(long postID, int grade) {

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
    public void setCurrentUserCommentGrade(long commentID, int grade) {

    }

    @Override
    public List<Tag> findSomeTags(String request) {
        return null;
    }
}
