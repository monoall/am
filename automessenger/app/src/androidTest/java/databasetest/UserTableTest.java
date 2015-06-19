package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.UserService;
import ua.org.javatraining.automessenger.app.entityes.User;

import static junit.framework.TestCase.*;

/**
 * Created by berkut on 17.06.15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class UserTableTest extends DBITest implements DbConstants {

    private UserService userService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        userService = new UserService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setName("Jack");
        userService.insertUser(user);
        Cursor c = db.query(USER_TABLE, null,
                USER_NAME + " = ?", new String[]{"Jack"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("Jack", c.getString(c.getColumnIndex(USER_NAME)));
        c.close();
    }

    @Test
    public void testGetUser(){
        User checkUser = userService.getUser("Tom");
        assertNotNull(checkUser);
        assertEquals("Tom", checkUser.getName());
    }

    @Test
    public void testDeleteUser(){
        User checkUser = new User();
        checkUser.setName("Tom");
        userService.deleteUser(checkUser);
        Cursor c = db.query(USER_TABLE, null,
                USER_NAME + " = ?", new String[]{"Tom"}, null, null, null);
        assertFalse(c.moveToFirst());
        c.close();
    }
}
