package ua.org.javatraining.automessenger.backend.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.Post;
import ua.org.javatraining.automessenger.backend.repository.PostRepository;
import ua.org.javatraining.automessenger.backend.test.service.PostService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Post addPost(Post post) {
        return postRepository.saveAndFlush(post);
    }

    @Override
    public boolean delete(Long id) {
        if (postRepository.exists(id)) {
            postRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findOne(id);
    }

    @Override
    public Post editPost(Post post) {
        return postRepository.saveAndFlush(post);
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }
}
