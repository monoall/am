package databasetest;

import android.database.Cursor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import ua.org.javatraining.automessenger.app.database.DbConstants;
import ua.org.javatraining.automessenger.app.database.GradeCommentService;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.entities.GradeComment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by berkut on 21.06.15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GradeCommentTableTest extends DBITest implements DbConstants {

    private GradeCommentService gradeCommentService;

    @Override
    String getDataSetFileName() {
        return "/db.sql";
    }

    @Before
    public void init(){
        gradeCommentService = new GradeCommentService(SQLiteAdapter.initInstance(Robolectric.application));
    }

    @Test
    public void testInsertGradeComment(){
        GradeComment gradeComment = new GradeComment();
        gradeComment.setUserId("User");
        gradeComment.setCommentId(1);
        gradeComment.setGrade(1);
        gradeCommentService.insertGradeComment(gradeComment);
        Cursor c = db.query(GRADE_COMMENT_TABLE, null,
                USER_NAME + " = ?", new String[]{"User"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals("User", c.getString(c.getColumnIndex(USER_NAME)));
        c.close();
    }

    @Test
    public void testUpdateIncreasingGradeComment(){
        GradeComment gradeComment = new GradeComment();
        gradeComment.setUserId("John");
        gradeComment.setCommentId(1);
        gradeComment.setGrade(1);
        gradeComment.increaseGrade();
        gradeCommentService.updateGradeComment(gradeComment);
        Cursor c = db.query(GRADE_COMMENT_TABLE, null,
                USER_NAME + " = ?", new String[]{"John"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals(2, c.getInt(c.getColumnIndex(GRADE)));
        c.close();
    }

    @Test
    public void testUpdateDecreasingGradeComment(){
        GradeComment gradeComment = new GradeComment();
        gradeComment.setUserId("John");
        gradeComment.setCommentId(1);
        gradeComment.setGrade(1);
        gradeComment.decreaseGrade();
        gradeCommentService.updateGradeComment(gradeComment);
        Cursor c = db.query(GRADE_COMMENT_TABLE, null,
                USER_NAME + " = ?", new String[]{"John"}, null, null, null);
        assertTrue(c.moveToFirst());
        assertEquals(0, c.getInt(c.getColumnIndex(GRADE)));
        c.close();
    }
}
