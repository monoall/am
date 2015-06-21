package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.CommentService;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.Comment;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by berkut on 20.06.15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CommentTableTest extends DBITest implements DbConstants {

    private CommentService commentService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        commentService = new CommentService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertComment(){
        Comment comment = new Comment();
        comment.setCommentDate(120315);
        comment.setCommentText("Comment Text");
        comment.setNameUser("User");
        comment.setIdPost(1);
        commentService.insertComment(comment);
        Cursor c = db.query(COMMENT_TABLE, null,
                COMMENT_TEXT + " = ?", new String[]{"Comment Text"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("Comment Text", c.getString(c.getColumnIndex(COMMENT_TEXT)));
        c.close();
    }

    @Test
    public void testGetAllComments(){
        ArrayList<Comment> al = commentService.getAllComments(1);
        assertEquals(2, al.size());
    }

    @Test
    public void testDeleteComment(){
        Comment comment = new Comment();
        comment.setCommentDate(130817);
        comment.setCommentText("text3");
        comment.setNameUser("User");
        comment.setIdPost(2);
    }


}
