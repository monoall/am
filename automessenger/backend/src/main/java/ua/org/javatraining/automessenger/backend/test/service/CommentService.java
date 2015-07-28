package ua.org.javatraining.automessenger.backend.test.service;

import ua.org.javatraining.automessenger.backend.entity.Comment;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface CommentService {
    Comment addComment(Comment comment);
    boolean delete(Long id);
    Comment getById(Long id);
    Comment editComment(Comment comment);
    List<Comment> getAll();
}
