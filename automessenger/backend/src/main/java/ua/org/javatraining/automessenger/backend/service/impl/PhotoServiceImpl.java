package ua.org.javatraining.automessenger.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.Photo;
import ua.org.javatraining.automessenger.backend.repository.PhotoRepository;
import ua.org.javatraining.automessenger.backend.service.PhotoService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
@Service
public class PhotoServiceImpl implements PhotoService {
 
    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public Photo addPhoto(Photo photo) {
        return photoRepository.saveAndFlush(photo);
    }

    @Override
    public boolean delete(Long id) {
        if (photoRepository.exists(id)) {
            photoRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public Photo getById(Long id) {
        return photoRepository.findOne(id);
    }

    @Override
    public Photo editPhoto(Photo photo) {
        return photoRepository.saveAndFlush(photo);
    }

    @Override
    public List<Photo> getAll() {
        return photoRepository.findAll();
    }
}
