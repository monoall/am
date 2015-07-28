package ua.org.javatraining.automessenger.backend.test.service;

import ua.org.javatraining.automessenger.backend.entity.Tag;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface TagService {
    Tag addTag(Tag tag);
    boolean delete(Long id);
    Tag getById(Long id);
    Tag editTag(Tag tag);
    List<Tag> getAll();
}
