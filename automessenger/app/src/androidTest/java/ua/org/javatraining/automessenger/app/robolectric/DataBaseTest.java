package ua.org.javatraining.automessenger.app.robolectric;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.activities.MainActivity;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entityes.User;

import static junit.framework.Assert.assertEquals;

/**
 * Created by berkut on 04.06.15.
 */
@SuppressWarnings("DefaultFileTemplate")
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DataBaseTest {

    MainActivity mainActivity;
    SQLiteAdapter sqLiteAdapter;
    UserService userService;
    TagService tagService;
    SubscriptionService subscriptionService;
    PostService postService;
    CommentService commentService;

    @Before
    public void initialize(){
        mainActivity = new MainActivity();
        sqLiteAdapter = SQLiteAdapter.initInstance(mainActivity);
        userService = new UserService(sqLiteAdapter);
        tagService = new TagService(sqLiteAdapter);
        subscriptionService = new SubscriptionService(sqLiteAdapter);
        postService = new PostService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setName("Tom");
        userService.insertUser(user);
        User checkUser = userService.getUserById(1);
        assertEquals("Tom", checkUser.getName());
    }


}
