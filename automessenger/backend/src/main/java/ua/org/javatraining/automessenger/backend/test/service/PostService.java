package ua.org.javatraining.automessenger.backend.test.service;

import ua.org.javatraining.automessenger.backend.entity.Post;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface PostService {
    Post addPost(Post post);
    boolean delete(Long id);
    Post getById(Long id);
    Post editPost(Post post);
    List<Post> getAll();
}
