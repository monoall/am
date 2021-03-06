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
import ua.org.javatraining.automessenger.app.database.SubscriptionService;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.entities.User;

import java.util.ArrayList;

import static junit.framework.Assert.*;

//@Ignore
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SubscriptionDataBaseTest extends DBITest implements DbConstants {

    private SubscriptionService subscriptionService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        subscriptionService = new SubscriptionService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertSubscription(){
        Subscription subscription = new Subscription();
        subscription.setTagId("BE 123");
        subscription.setUserId("User");
        subscriptionService.insertSubscription(subscription);
        Cursor c = db.query(SUBSCRIPTION_TABLE, null,
                USER_NAME + " = ?", new String[]{"User"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("User", c.getString(c.getColumnIndex(USER_NAME)));
        assertEquals("BE 123", c.getString(c.getColumnIndex(TAG_NAME)));
        c.close();
    }


    @Test
    public void testGetSubscriptionList(){
        User user = new User();
        user.setName("Tom");
        ArrayList<Subscription> al = subscriptionService.getSubscriptionsList(user);
        assertEquals(2, al.size());
    }


    @Test
    public void testDeleteSubscription(){
        Subscription subscription = new Subscription();
        subscription.setTagId("BE 0102");
        subscription.setUserId("John");
        subscriptionService.deleteSubscription(subscription);
        Cursor c = db.query(SUBSCRIPTION_TABLE, null,
                USER_NAME + " = ? AND " + TAG_NAME + " = ?", new String[]{"John", "BE 0102"}, null, null, null);
        assertFalse(c.moveToFirst());
        c.close();
    }
}
