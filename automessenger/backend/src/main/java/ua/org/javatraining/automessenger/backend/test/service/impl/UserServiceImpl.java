package ua.org.javatraining.automessenger.backend.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.org.javatraining.automessenger.backend.entity.User;
import ua.org.javatraining.automessenger.backend.repository.UserRepository;
import ua.org.javatraining.automessenger.backend.test.service.UserService;

import java.util.List;

/**
 * Created by fisher on 28.07.15.
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public boolean delete(Long id) {
        if (userRepository.exists(id)) {
            userRepository.delete(id);
            return true;
        } else
            return false;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User editUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
