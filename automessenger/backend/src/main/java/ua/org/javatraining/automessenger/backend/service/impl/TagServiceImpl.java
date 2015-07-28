package ua.org.javatraining.automessenger.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.Tag;
import ua.org.javatraining.automessenger.backend.repository.TagRepository;
import ua.org.javatraining.automessenger.backend.service.TagService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag addTag(Tag tag) {
        return tagRepository.saveAndFlush(tag);
    }

    @Override
    public boolean delete(Long id) {
        if (tagRepository.exists(id)) {
            tagRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public Tag getById(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public Tag editTag(Tag tag) {
        return tagRepository.saveAndFlush(tag);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
}
