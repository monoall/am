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

    public GradePost insertGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(USER_ID, gradePost.getIdUser());
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

    public GradePost updateGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        try{
            ContentValues cv = new ContentValues();
            cv.put(GRADE, gradePost.getGrade());
            id = sqLiteDatabase.update(GRADE_POST_TABLE, cv, "ID = ?", new String[] { String.valueOf(gradePost.getId()) });
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        gradePost.setId(id);
        return gradePost;
    }


    public GradePost getGradePost(int idPost){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(GRADE_POST_TABLE, null, "ID = ?", new String[]{String.valueOf(idPost)}, null, null, null);
        GradePost gradePost = null;
        if(cursor.moveToFirst()){
           gradePost = buildGradePost(cursor);
        }
        return gradePost;
    }

    private GradePost buildGradePost(Cursor c){
        GradePost gradePost = new GradePost();
        gradePost.setId(c.getInt(c.getColumnIndex(ID)));
        gradePost.setIdUser(c.getInt(c.getColumnIndex(USER_ID)));
        gradePost.setIdPost(c.getInt(c.getColumnIndex(ID_POST)));
        gradePost.setGrade(c.getInt(c.getColumnIndex(GRADE)));
        return gradePost;
    }

}
