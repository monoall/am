package ua.org.javatraining.automessenger.backend.service;

import ua.org.javatraining.automessenger.backend.entity.GradePost;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface GradePostService {
    GradePost addGradePost(GradePost gradePost);
    boolean delete(Long id);
    GradePost getById(Long id);
    GradePost editGradePost(GradePost gradePost);
    List<GradePost> getAll();
}
