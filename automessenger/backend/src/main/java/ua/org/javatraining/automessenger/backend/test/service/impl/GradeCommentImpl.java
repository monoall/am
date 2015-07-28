package ua.org.javatraining.automessenger.backend.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.GradeComment;
import ua.org.javatraining.automessenger.backend.repository.GradeCommentRepository;
import ua.org.javatraining.automessenger.backend.test.service.GradeCommentService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
@Service
public class GradeCommentImpl implements GradeCommentService {

    @Autowired
    private GradeCommentRepository gradeCommentRepository;

    @Override
    public GradeComment addGradeComment(GradeComment gradeComment) {
        return gradeCommentRepository.saveAndFlush(gradeComment);
    }

    @Override
    public boolean delete(Long id) {
        if (gradeCommentRepository.exists(id)) {
            gradeCommentRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public GradeComment getById(Long id) {
        return gradeCommentRepository.findOne(id);
    }

    @Override
    public GradeComment editGradeComment(GradeComment comment) {
        return gradeCommentRepository.saveAndFlush(comment);
    }

    @Override
    public List<GradeComment> getAll() {
        return gradeCommentRepository.findAll();
    }
}
