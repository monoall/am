package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.GradeComment;

public class GradeCommentService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public GradeCommentService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }


    public GradeComment insertGradeComment(GradeComment gradeComment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, gradeComment.getIdUser());
        cv.put(ID_COMMENT, gradeComment.getIdComment());
        cv.put(GRADE, gradeComment.getGrade());
        long id = sqLiteDatabase.insert(GRADE_COMMENT_TABLE, null, cv);
        gradeComment.setId(id);
        return gradeComment;
    }

    public GradeComment updateGradePost(GradeComment gradeComment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(GRADE, gradeComment.getGrade());
        long id = sqLiteDatabase.update(GRADE_COMMENT_TABLE, cv, "ID = ?", new String[] { String.valueOf(gradeComment.getId()) });
        gradeComment.setId(id);
        return gradeComment;
    }


    public GradeComment getGradeComment(int idComment){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_GRADE_COMMENT_BY_ID_COMMENT, new String[]{String.valueOf(idComment)});
        //Cursor cursor = sqLiteDatabase.query(GRADE_COMMENT_TABLE, null, null, null, null, null, null);
        //cursor.move(idComment);
        int indexId = cursor.getColumnIndex(ID);
        int indexIdUser = cursor.getColumnIndex(USER_ID);
        int indexIdComment = cursor.getColumnIndex(ID_COMMENT);
        int indexGrade = cursor.getColumnIndex(GRADE);
        GradeComment gradeComment = new GradeComment();
        gradeComment.setId(cursor.getInt(indexId));
        gradeComment.setIdUser(cursor.getInt(indexIdUser));
        gradeComment.setIdComment(cursor.getInt(indexIdComment));
        gradeComment.setGrade(cursor.getInt(indexGrade));
        return gradeComment;
    }
}
