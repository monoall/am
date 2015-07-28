package ua.org.javatraining.automessenger.backend.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.GradePost;
import ua.org.javatraining.automessenger.backend.repository.GradePostRepository;
import ua.org.javatraining.automessenger.backend.test.service.GradePostService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
@Service
public class GradePostServiceImpl implements GradePostService {
    @Autowired
    private GradePostRepository gradePostRepository;

    @Override
    public GradePost addGradePost(GradePost gradePost) {
        return gradePostRepository.saveAndFlush(gradePost);
    }

    @Override
    public boolean delete(Long id) {
        if (gradePostRepository.exists(id)) {
            gradePostRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public GradePost getById(Long id) {
        return gradePostRepository.findOne(id);
    }

    @Override
    public GradePost editGradePost(GradePost gradePost) {
        return gradePostRepository.saveAndFlush(gradePost);
    }

    @Override
    public List<GradePost> getAll() {
        return gradePostRepository.findAll();
    }
}
