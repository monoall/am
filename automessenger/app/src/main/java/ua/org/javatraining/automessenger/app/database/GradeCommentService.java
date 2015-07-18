package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entities.GradeComment;
import ua.org.javatraining.automessenger.app.entities.GradePost;

import java.util.ArrayList;

public class GradeCommentService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public GradeCommentService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public ArrayList<GradeComment> getCommentGrades(long commentID) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_GRADE_COMMENT_BY_ID_COMMENT, new String[]{String.valueOf(commentID)});
        ArrayList<GradeComment> al = new ArrayList<GradeComment>();
        for (cursor.moveToLast(); !(cursor.isBeforeFirst()); cursor.moveToPrevious()) {
            GradeComment gradeComment = buildGradeComment(cursor);
            al.add(gradeComment);
        }
        return al;
    }

    public GradeComment getCommentGrade(long commentID, String username) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_GRADE_COMMENT_BY_ID_COMMENT_AND_USER, new String[]{String.valueOf(commentID), username});
        GradeComment gradeComment = null;

        if (cursor.moveToFirst()) {
            gradeComment = buildGradeComment(cursor);
        }

        return gradeComment;
    }

    /**
     * Вставляет оценку в таблицу GradeComment
     * @param gradeComment объект GradeComment
     * @return вставленный объект
     */
    public GradeComment insertGradeComment(GradeComment gradeComment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(USER_NAME, gradeComment.getNameUser());
            cv.put(ID_COMMENT, gradeComment.getIdComment());
            cv.put(GRADE, gradeComment.getGrade());
            id = sqLiteDatabase.insert(GRADE_COMMENT_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        } finally{
            sqLiteDatabase.endTransaction();
        }
        gradeComment.setId(id);
        return gradeComment;
    }

    /**
     * Обновляет существующий GradeComment
     * @param gradeComment объект GradeComment
     * @return обновленный объект GradeComment
     */
    public GradeComment updateGradeComment(GradeComment gradeComment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(GRADE, gradeComment.getGrade());
            id = sqLiteDatabase.update(GRADE_COMMENT_TABLE, cv, USER_NAME + " = ?" + " and " + ID_COMMENT  + " = ?", new String[] { gradeComment.getNameUser(), String.valueOf(gradeComment.getIdComment())});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        gradeComment.setId(id);
        return gradeComment;
    }

    /**
     * Возвращает оценку по id комментария
     * @param id id комментария
     * @return объект GradeComment
     */
    public GradeComment getGradeComment(long id){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(GRADE_COMMENT_TABLE, null, "ID = ?", new String[]{String.valueOf(id)}, null, null, null);
        GradeComment gradeComment = null;
        if(cursor.moveToFirst()){
            gradeComment = buildGradeComment(cursor);
        }
        return gradeComment;
    }


    private GradeComment buildGradeComment(Cursor c){
        GradeComment gradeComment = new GradeComment();
        gradeComment.setId(c.getInt(c.getColumnIndex(ID)));
        gradeComment.setNameUser(c.getString(c.getColumnIndex(USER_NAME)));
        gradeComment.setIdComment(c.getInt(c.getColumnIndex(ID_COMMENT)));
        gradeComment.setGrade(c.getInt(c.getColumnIndex(GRADE)));
        return gradeComment;
    }
}
