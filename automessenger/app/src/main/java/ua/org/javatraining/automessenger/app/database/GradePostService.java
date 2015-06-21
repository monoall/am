package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.GradePost;

public class GradePostService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public GradePostService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет оценку в таблицу GradePost
     * @param gradePost объект GradePost
     * @return вставленный объект
     */
    public GradePost insertGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(USER_NAME, gradePost.getNameUser());
            cv.put(ID_POST, gradePost.getIdPost());
            cv.put(GRADE, gradePost.getGrade());
            id = sqLiteDatabase.insert(GRADE_POST_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        } finally{
            sqLiteDatabase.endTransaction();
        }
        gradePost.setId(id);
        return gradePost;
    }

    /**
     * Обновляет существующий GradePost
     * @param gradePost объект GradePost
     * @return обновленный объект GradePost
     */
    public GradePost updateGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(GRADE, gradePost.getGrade());
            id = sqLiteDatabase.update(GRADE_POST_TABLE, cv,  USER_NAME + " = ?" + " and " + ID_POST  + " = ?", new String[] { gradePost.getNameUser(), String.valueOf(gradePost.getIdPost())});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        gradePost.setId(id);
        return gradePost;
    }

    private GradePost buildGradePost(Cursor c){
        GradePost gradePost = new GradePost();
        gradePost.setId(c.getInt(c.getColumnIndex(ID)));
        gradePost.setNameUser(c.getString(c.getColumnIndex(USER_NAME)));
        gradePost.setIdPost(c.getInt(c.getColumnIndex(ID_POST)));
        gradePost.setGrade(c.getInt(c.getColumnIndex(GRADE)));
        return gradePost;
    }

}
