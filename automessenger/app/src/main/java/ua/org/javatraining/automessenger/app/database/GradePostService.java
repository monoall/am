package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entities.GradePost;
import ua.org.javatraining.automessenger.app.user.Authentication;

import java.util.ArrayList;

public class GradePostService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public GradePostService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public ArrayList<GradePost> getPostGrades(long postID) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_GRADE_POST_BY_ID_POST, new String[]{String.valueOf(postID)});
        ArrayList<GradePost> al = new ArrayList<GradePost>();
        for (cursor.moveToLast(); !(cursor.isBeforeFirst()); cursor.moveToPrevious()) {
            GradePost gradePost = buildGradePost(cursor);
            al.add(gradePost);
        }
        return al;
    }

    public GradePost getPostGrade(long postID, String username) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_GRADE_POST_BY_ID_POST_AND_USER, new String[]{String.valueOf(postID), username});
        GradePost gradePost = null;

        if (cursor.moveToFirst()) {
            gradePost = buildGradePost(cursor);
        }

        return gradePost;
    }

    /**
     * Вставляет оценку в таблицу GradePost
     *
     * @param gradePost объект GradePost
     * @return вставленный объект
     */
    public GradePost insertGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(USER_NAME, gradePost.getNameUser());
            cv.put(ID_POST, gradePost.getIdPost());
            cv.put(GRADE, gradePost.getGrade());
            id = sqLiteDatabase.insert(GRADE_POST_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        gradePost.setId(id);
        return gradePost;
    }

    /**
     * Обновляет существующий GradePost
     *
     * @param gradePost объект GradePost
     * @return обновленный объект GradePost
     */
    public GradePost updateGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(GRADE, gradePost.getGrade());
            id = sqLiteDatabase.update(GRADE_POST_TABLE, cv, USER_NAME + " = ?" + " and " + ID_POST + " = ?", new String[]{gradePost.getNameUser(), String.valueOf(gradePost.getIdPost())});
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        gradePost.setId(id);
        return gradePost;
    }

    private GradePost buildGradePost(Cursor c) {
        GradePost gradePost = new GradePost();
        gradePost.setId(c.getInt(c.getColumnIndex(ID)));
        gradePost.setNameUser(c.getString(c.getColumnIndex(USER_NAME)));
        gradePost.setIdPost(c.getInt(c.getColumnIndex(ID_POST)));
        gradePost.setGrade(c.getInt(c.getColumnIndex(GRADE)));
        return gradePost;
    }

}
