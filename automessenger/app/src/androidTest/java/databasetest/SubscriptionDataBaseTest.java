package databasetest;

import android.content.ContentValues;
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
import ua.org.javatraining.automessenger.app.entityes.Subscription;
import ua.org.javatraining.automessenger.app.entityes.User;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by berkut on 18.06.15.
 */
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
        subscription.setNameTag("BE 123");
        subscription.setNameUser("User");
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
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, "Tom");
        cv.put(TAG_NAME, "BA 12");
        db.insert(SUBSCRIPTION_TABLE, null, cv);
        ContentValues cv1 = new ContentValues();
        cv1.put(USER_NAME, "Tom");
        cv1.put(TAG_NAME, "BW 12");
        db.insert(SUBSCRIPTION_TABLE, null, cv1);
        ContentValues cv2 = new ContentValues();
        cv2.put(USER_NAME, "Jack");
        cv2.put(TAG_NAME, "BS 12");
        db.insert(SUBSCRIPTION_TABLE, null, cv2);
        ArrayList<Subscription> al = subscriptionService.getSubscriptionsList(user);
        assertEquals(2, al.size());
    }


    @Test
    public void testDeleteSubscription(){
        Subscription subscription = new Subscription();
        subscription.setNameTag("BA 12");
        subscription.setNameUser("Tom");
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, "Tom");
        cv.put(TAG_NAME, "BA 12");
        long id = db.insert(SUBSCRIPTION_TABLE, null, cv);
        subscription.setId(id);
        subscriptionService.deleteSubscription(subscription);
    }
}
