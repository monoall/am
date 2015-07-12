package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entities.GradeComment;

public class GradeCommentService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public GradeCommentService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
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
