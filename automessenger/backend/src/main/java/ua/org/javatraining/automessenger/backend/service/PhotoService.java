package ua.org.javatraining.automessenger.backend.service;

import ua.org.javatraining.automessenger.backend.entity.Photo;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface PhotoService {
    Photo addPhoto(Photo photo);
    boolean delete(Long id);
    Photo getById(Long id);
    Photo editPhoto(Photo photo);
    List<Photo> getAll();
}
