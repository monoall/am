package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.PostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entities.Post;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@Ignore
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
        postService = new PostService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertTest(){
        Post post = new Post();
        post.setPostText("Post text1");
        post.setPostDate(120215);
        post.setPostLocation("ShortLocation");
        post.setNameUser("Yum");
        post.setNameTag("BE 120515");
        postService.insertPost(post);
        Cursor c = db.query(POST_TABLE, null,
                USER_NAME + " = ?", new String[]{"Yum"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("Yum", c.getString(c.getColumnIndex(USER_NAME)));
        assertEquals("Post text1", c.getString(c.getColumnIndex(POST_TEXT)));
        c.close();
    }


    @Test
    public void testGetAllPosts(){
        ArrayList<Post> al = postService.getAllPosts("User", "BE 0102");
        assertEquals(2, al.size());
    }


    @Test
    public void testGetPostsFromSubscribes(){
        ArrayList<Post> al = postService.getPostsByAuthor("User");
        assertEquals(4, al.size());
    }

    @Test
    public void testGetPostsFromNearby(){
        ArrayList<Post> al = postService.getPostsFromNearby("ShortLocation");
        assertEquals(4, al.size());
    }

    @Test
    public void testGetPostsByTag(){
        ArrayList<Post> al = postService.getPostsByTag("BE 0102");
        assertEquals(3, al.size());
    }

    @Test
    public void testDeletePost(){
        Post post = new Post();
        post.setPostText("Post text4");
        post.setPostDate(120817);
        post.setPostLocation("location");
        post.setNameUser("John");
        post.setNameTag("BE 0102");
        postService.deletePost(post);
        Cursor c = db.query(POST_TABLE, null,
                POST_TEXT + " = ?", new String[]{"Post text4"}, null, null, null);
        assertFalse(c.moveToFirst());
        c.close();
    }

}
