package ua.org.javatraining.automessenger.app.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Comment;

import java.util.ArrayList;

public class CommentService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public  CommentService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет в таблицу Comment
     * @param comment объект Comment
     * @return вставленный объект
     */
    public Comment insertComment(Comment comment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(COMMENT_DATE, comment.getCommentDate());
            cv.put(COMMENT_TEXT, comment.getCommentText());
            cv.put(USER_ID, comment.getIdUser());
            cv.put(ID_POST, comment.getIdPost());
            id = sqLiteDatabase.insert(COMMENT_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        comment.setId(id);
        return comment;
    }

    /**
     * Возвращает все комментарии к посту
     * @param postId id поста
     * @return Список комментариев
     */
    public ArrayList<Comment> getAllComments(int postId){

        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_COMMENTS_BY_POST_ID , new String[]{String.valueOf(postId)});
        ArrayList<Comment> al = new ArrayList<Comment>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Comment comment = buildComment(cursor);
            al.add(comment);
        }
        return al;
    }

    /**
     *
     * Удаляет комментарии
     * @param comment объект Comment
     */
    public void deleteComment(Comment comment){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        try{
            sqLiteDatabase.delete(COMMENT_TABLE, COMMENT_TEXT + " = ?", new String[]{comment.getCommentText()});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private Comment buildComment(Cursor c){
        Comment com = new Comment();
        com.setId(c.getLong(c.getColumnIndex(ID)));
        com.setCommentDate(c.getInt(c.getColumnIndex(COMMENT_DATE)));
        com.setCommentText(c.getString(c.getColumnIndex(COMMENT_TEXT)));
        com.setIdUser(c.getInt(c.getColumnIndex(USER_ID)));
        com.setIdPost(c.getInt(c.getColumnIndex(ID_POST)));
        return com;
    }


}
