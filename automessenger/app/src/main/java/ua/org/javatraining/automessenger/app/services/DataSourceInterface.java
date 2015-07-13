package ua.org.javatraining.automessenger.app.services;

import ua.org.javatraining.automessenger.app.entities.Comment;
import ua.org.javatraining.automessenger.app.entities.GradeComment;
import ua.org.javatraining.automessenger.app.entities.GradePost;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.vo.CommentGrades;
import ua.org.javatraining.automessenger.app.vo.FullPost;
import ua.org.javatraining.automessenger.app.vo.PostGrades;

import java.util.List;

public interface DataSourceInterface {

    /**
     * Get post by post ID
     *
     * @param postID post ID
     * @return post object
     */
    FullPost getPostByID(long postID);

    /**
     * Get last 10 posts from user subscribes
     *
     * @return List with Post objects
     */
    List<FullPost> getPostsFromSubscriptions();

    /**
     * Method required for pagination.
     * Get 10 posts from user subscribes older than specified date(represented as Timestamp)
     *
     * @param timestamp timestamp from last post we have for that moment
     * @return List with Post objects
     */
    List<FullPost> getPostsFromSubscriptions(long timestamp);

    /**
     * Get 10 posts from specified area
     *
     * @param country   target country
     * @param adminArea state (Oblast')
     * @param locality  city/district
     * @return List with Post objects
     */
    List<FullPost> getPostsByLocation(String country, String adminArea, String locality);

    /**
     * Method required for pagination.
     * Get 10 posts from specified area older than specified date(represented as Timestamp)
     *
     * @param country   target country
     * @param adminArea state (Oblast')
     * @param locality  city/district
     * @param timestamp timestamp from last post we have for that moment
     * @return List with Post objects
     */
    List<FullPost> getPostsByLocation(String country, String adminArea, String locality, long timestamp);

    /**
     * Get 10 posts by tag name
     *
     * @param tagName specified tag
     * @return List with Post objects
     */
    List<FullPost> getPostsByTagName(String tagName);

    /**
     * Method required for pagination.
     * Get 10 posts by tag name older than specified date(represented as Timestamp)
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
    List<Comment> getComments(long postID);

    /**
     * Method required for pagination.
     * Get 10 comments from post older than specified date(represented as Timestamp)
     *
     * @param postID    post id
     * @param timestamp timestamp from last post we have for that moment
     * @return List with comments objects
     */
    List<Comment> getComments(long postID, long timestamp);

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
    List<Subscription> getSubscribtions();

    /**
     * Add new subscription
     *
     * @param tag tag name
     * @return subscription id if successfully added
     */
    long addSubscription(String tag);

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
     * @param grade grade value, should be in range(-1,0,1)
     * @return grade id if successfully added
     */
    long setCurrentUserPostGrade(long postID, int grade);

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
     * @param grade grade value, should be in range(-1,0,1)
     * @return grade id if successfully added
     */
    long setCurrentUserCommentGrade(long commentID, int grade);

    //TODO methods for search feature
}
