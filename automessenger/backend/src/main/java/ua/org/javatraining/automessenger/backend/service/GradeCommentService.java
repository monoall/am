package ua.org.javatraining.automessenger.backend.service;

import ua.org.javatraining.automessenger.backend.entity.GradeComment;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */

public interface GradeCommentService {
    GradeComment addGradeComment(GradeComment gradeComment);
    boolean delete(Long id);
    GradeComment getById(Long id);
    GradeComment editGradeComment(GradeComment gradeComment);
    List<GradeComment> getAll();
}
