package ua.org.javatraining.automessenger.backend.test.util;

import ua.org.javatraining.automessenger.backend.entity.User;

/**
 * Created by fisher on 28.07.15.
 */
public class UserUtil {
    public static User createUser() {
        User user = new User ();
        user.setName("Fisher");

        return user;
    }
}
