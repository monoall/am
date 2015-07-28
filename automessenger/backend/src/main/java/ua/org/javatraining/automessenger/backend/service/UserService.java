package ua.org.javatraining.automessenger.backend.service;

import ua.org.javatraining.automessenger.backend.entity.User;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */
public interface UserService {
    User addUser(User user);
    boolean delete(Long id);
    User getById(Long id);
    User editUser(User user);
    List<User> getAll();
}
