package ua.org.javatraining.automessenger.app.services;

import ua.org.javatraining.automessenger.app.entities.Post;
import ua.org.javatraining.automessenger.app.vo.FullPost;

import java.util.List;

public class RemoteService implements IService {
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
