package databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by berkut on 18.06.15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class PostTableTest extends DBITest implements DbConstants {

    private PostService postService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        postService = new PostService(sqLiteAdapter);
    }

    @Test
    public void testInsertTest(){
        Post post = new Post();
        post.setPostText("Post text1");
        post.setPostDate(120215);
        post.setPostLocation("location");
        post.setNameUser("Yum");
        post.setNameTag("BE 120515");
        postService.insertPost(post);
        Cursor c = db.query(POST_TABLE, null,
                USER_NAME + " = ?", new String[]{"Yum"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("Yum", c.getString(c.getColumnIndex(USER_NAME)));
        assertEquals("Post text1", c.getString(c.getColumnIndex(POST_TEXT)));
    }


    @Test
    public void testGetAllPosts(){
        ContentValues cv = new ContentValues();
        cv.put(POST_TEXT, "Post text");
        cv.put(POST_DATE, 120515);
        cv.put(POST_LOCATION, "location");
        cv.put(USER_NAME, "Andr");
        cv.put(TAG_NAME, "WE 021");
        db.insert(POST_TABLE, null, cv);
        ContentValues cv2 = new ContentValues();
        cv2.put(POST_TEXT, "Post text2");
        cv2.put(POST_DATE, 120815);
        cv2.put(POST_LOCATION, "location");
        cv2.put(USER_NAME, "Andr");
        cv2.put(TAG_NAME, "WE 021");
        db.insert(POST_TABLE, null, cv2);
        ContentValues cv3 = new ContentValues();
        cv3.put(POST_TEXT, "Post text3");
        cv3.put(POST_DATE, 120915);
        cv3.put(POST_LOCATION, "location");
        cv3.put(USER_NAME, "Whell");
        cv3.put(TAG_NAME, "WE 021");
        db.insert(POST_TABLE, null, cv3);
        ArrayList<Post> al = postService.getAllPosts("Andr", "WE 021");
        assertEquals(2, al.size());
    }

    @Test
    public void testDeletePost(){
        Post post = new Post();
        post.setPostText("Post text1");
        post.setPostDate(120215);
        post.setPostLocation("location");
        post.setNameUser("Yum");
        post.setNameTag("BE 120515");
        ContentValues cv = new ContentValues();
        cv.put(POST_TEXT, "Post text1");
        cv.put(POST_DATE, 120215);
        cv.put(POST_LOCATION, "location");
        cv.put(USER_NAME, "Yum");
        cv.put(TAG_NAME, "BE 120515");
        long id = db.insert(POST_TABLE, null, cv);
        post.setId(id);
        postService.deletePost(post);
    }

}
