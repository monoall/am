package ua.org.javatraining.automessenger.app.services;

import ua.org.javatraining.automessenger.app.vo.FullPost;
import java.util.List;

public interface IService {

    /**
     * Get post by post ID
     * @param postID post ID
     * @return post object
     */
    FullPost getPostByID(long postID);

    /**
     * Get last 10 posts from user subscribes
     * @return ArrayList with Post objects
     */
    List<FullPost> getPostsFromSubscriptions();

    /**
     * Method required for pagination.
     * Get 10 posts from user subscribes older than specified date(represented as Timestamp)
     * @param timestamp timestamp from last post we have for that moment
     * @return ArrayList with Post objects
     */
    List<FullPost> getPostsFromSubscriptions(long timestamp);

    List<FullPost> getPostsByLocation(String country, String adminArea, String city);

    List<FullPost> getPostsByLocation(String country, String adminArea, String city, long timestamp);

    List<FullPost> getPostsByTagName(String tagName);

    List<FullPost> getPostsByTagName(String tagName, long timestamp);


}
