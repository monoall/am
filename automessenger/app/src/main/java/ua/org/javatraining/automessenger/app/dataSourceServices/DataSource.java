package ua.org.javatraining.automessenger.app.dataSourceServices;

import ua.org.javatraining.automessenger.app.entities.*;
import ua.org.javatraining.automessenger.app.vo.*;

import java.util.List;

public interface DataSource {

    /**
     * Check if current user exists in DB and add new if it doesnt
     *
     * @param username user name
     */
    void setUser(String username);

    /**
     * Get post by post ID
     *
     * @param postID post ID
     * @return post object
     */
    FullPost getPostByID(long postID);

    /**
     * Get last posts by author
     *
     * @return List with Post objects
     */
    List<FullPost> getPostsByAuthor();


    /**
     * Get last 5 posts from user subscribes with user own posts
     *
     * @return List with Post objects
     */
    List<FullPost> getPostsFromSubscriptions();

    /**
     * Method required for pagination.
     * Get 5 posts from user subscribes with user own posts older than specified date(represented as Timestamp)
     *
     * @param timestamp timestamp from last post we have for that moment
     * @return List with Post objects
     */
    List<FullPost> getPostsFromSubscriptions(long timestamp);

    /**
     * Get 5 posts from specified area
     *
     * @param shortLocation Representation location in three fields
     * @return List with Post objects
     */
    List<FullPost> getPostsByLocation(ShortLocation shortLocation);

    /**
     * Method required for pagination.
     * Get 5 posts from specified area older than specified date(represented as Timestamp)
     *
     * @param shortLocation Representation location in three fields
     * @param timestamp     timestamp from last post we have for that moment
     * @return List with Post objects
     */
    List<FullPost> getPostsByLocation(ShortLocation shortLocation, long timestamp);

    /**
     * Get 5 posts by tag name
     *
     * @param tagName specified tag
     * @return List with Post objects
     */
    List<FullPost> getPostsByTagName(String tagName);

    /**
     * Method required for pagination.
     * Get 5 posts by tag name older than specified date(represented as Timestamp)
     *
     * @param tagName   specified tag
     * @param timestamp timestamp from last post we have for that moment
     * @return List with Post objects
     */
    List<FullPost> getPostsByTagName(String tagName, long timestamp);

    /**
     * Add post.
     *
     * @param fullPost FullPost object
     * @return Post ID if successfully added
     */
    long addPost(FullPost fullPost);

    /**
     * Get 10 comments from post
     *
     * @param postID post id
     * @return List with comments objects
     */
    List<SuperComment> getComments(long postID);

    /**
     * Method required for pagination.
     * Get 10 comments from post older than specified date(represented as Timestamp)
     *
     * @param postID    post id
     * @param timestamp timestamp from last post we have for that moment
     * @return List with comments objects
     */
    List<SuperComment> getComments(long postID, long timestamp);

    /**
     * Add comment
     *
     * @param comment new comment object
     * @return comment id if successfully added
     */
    long addComment(Comment comment);

    /**
     * Get list with all subscriptions of current user
     *
     * @return list with subscription objects
     */
    List<Subscription> getSubscriptions();

    /**
     * Add new subscription
     *
     * @param tag tag name
     * @return subscription object if successfully added
     */
    Subscription addSubscription(String tag);

    /**
     * delete subscription
     *
     * @param subscription subscription object
     */
    void removeSubscription(Subscription subscription);

    /**
     * Get object that contains sum of all grades of specified post in one int field,
     * except current user grade, current user grade contained in separate field of this object.
     *
     * @param postID post id
     * @return object with grades values
     */
    PostGrades getPostGrades(long postID);

    /**
     * Get object that represent current user grade in specified post
     *
     * @param postID post id
     * @return grade object
     */
    GradePost getCurrentUserPostGrade(long postID);

    /**
     * Set grade for post
     *
     * @param postID post id
     * @param grade  grade value, should be in range(-1,0,1)
     * @return grade id if successfully added
     */
    void setCurrentUserPostGrade(long postID, int grade);

    /**
     * Get object that contains sum of all grades of specified comment in one int field,
     * except current user grade, current user grade contained in separate field of this object.
     *
     * @param commentID comment id
     * @return object with grades values
     */
    CommentGrades getCommentGrades(long commentID);

    /**
     * Get object that represent current user grade in specified comment
     *
     * @param commentID comment id
     * @return grade object
     */
    GradeComment getCurrentUserCommentGrade(long commentID);

    /**
     * Set grade for comment
     *
     * @param commentID comment id
     * @param grade     grade value, should be in range(-1,0,1)
     * @return grade id if successfully added
     */
    void setCurrentUserCommentGrade(long commentID, int grade);

    /**
     * Search tags in database
     *
     * @param request String that contain our search request
     * @return List with search results
     */
    List<Tag> findSomeTags(String request);
}
