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
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, gradePost.getIdUser());
        cv.put(ID_POST, gradePost.getIdPost());
        cv.put(GRADE, gradePost.getGrade());
        long id = sqLiteDatabase.insert(GRADE_POST_TABLE, null, cv);
        gradePost.setId(id);
        return gradePost;
    }

    public GradePost updateGradePost(GradePost gradePost) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GRADE, gradePost.getGrade());
        long id = sqLiteDatabase.update(GRADE_POST_TABLE, cv, "ID = ?", new String[] { String.valueOf(gradePost.getId()) });
        gradePost.setId(id);
        return gradePost;
    }


    public GradePost getGradePost(int idPost){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_GRADE_POST_BY_ID_POST, new String[]{String.valueOf(idPost)});
        int indexId = cursor.getColumnIndex(ID);
        int indexIdUser = cursor.getColumnIndex(USER_ID);
        int indexIdPost = cursor.getColumnIndex(ID_POST);
        int indexGrade = cursor.getColumnIndex(GRADE);
        GradePost gradePost = new GradePost();
        gradePost.setId(cursor.getInt(indexId));
        gradePost.setIdUser(cursor.getInt(indexIdUser));
        gradePost.setIdPost(cursor.getInt(indexIdPost));
        gradePost.setGrade(cursor.getInt(indexGrade));
        return gradePost;
    }

}
