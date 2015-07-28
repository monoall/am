package ua.org.javatraining.automessenger.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.Comment;
import ua.org.javatraining.automessenger.backend.repository.CommentRepository;
import ua.org.javatraining.automessenger.backend.service.CommentService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public boolean delete(Long id) {
        if (commentRepository.exists(id)) {
            commentRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public Comment editComment(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }
}
