package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.GradePostService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entityes.GradePost;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by berkut on 21.06.15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GrandPostTableTest extends DBITest implements DbConstants {

    private GradePostService gradePostService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        gradePostService = new GradePostService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertGrade(){
        GradePost gradePost = new GradePost();
        gradePost.setNameUser("User");
        gradePost.setIdPost(1);
        gradePost.setGrade(1);
        gradePostService.insertGradePost(gradePost);
        Cursor c = db.query(GRADE_POST_TABLE, null,
                USER_NAME + " = ?", new String[]{"User"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("User", c.getString(c.getColumnIndex(USER_NAME)));
        c.close();
    }

    @Test
    public void testUpdateIncreasingGradePost(){
        GradePost gradePost = new GradePost();
        gradePost.setNameUser("John");
        gradePost.setIdPost(1);
        gradePost.setGrade(1);
        gradePost.increaseGrade();
        gradePostService.updateGradePost(gradePost);
        Cursor c = db.query(GRADE_POST_TABLE, null,
                USER_NAME + " = ?", new String[]{"John"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals(2, c.getInt(c.getColumnIndex(GRADE)));
        c.close();
    }

    @Test
    public void testUpdateDecreasingGradePost(){
        GradePost gradePost = new GradePost();
        gradePost.setNameUser("John");
        gradePost.setIdPost(1);
        gradePost.setGrade(1);
        gradePost.decreaseGrade();
        gradePostService.updateGradePost(gradePost);
        Cursor c = db.query(GRADE_POST_TABLE, null,
                USER_NAME + " = ?", new String[]{"John"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals(0, c.getInt(c.getColumnIndex(GRADE)));
        c.close();
    }



}
