package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Comment;

import java.util.ArrayList;

/**
 * Created by berkut on 05.06.15.
 */
public class CommentService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public  CommentService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public Comment insertComment(Comment comment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COMMENT_DATE, comment.getCommentDate());
        cv.put(COMMENT_TEXT, comment.getCommentText());
        cv.put(USER_ID, comment.getIdUser());
        cv.put(ID_POST, comment.getIdPost());
        long id = sqLiteDatabase.insert(COMMENT_TABLE, null, cv);
        comment.setId(id);
        return comment;
    }

    public ArrayList<Comment> getAllComments(int postId){

        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_COMMENTS_BY_POST_ID , new String[]{String.valueOf(postId)});
        int indexCommentDate = cursor.getColumnIndex(COMMENT_DATE);
        int indexCommentText = cursor.getColumnIndex(COMMENT_TEXT);
        int indexIdUser = cursor.getColumnIndex(USER_ID);
        int indexIdPost = cursor.getColumnIndex(ID_POST);

        ArrayList<Comment> al = new ArrayList<Comment>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Comment comment = new Comment();
            comment.setCommentDate(cursor.getInt(indexCommentDate));
            comment.setCommentText(cursor.getString(indexCommentText));
            comment.setIdUser(cursor.getInt(indexIdUser));
            comment.setIdPost(cursor.getInt(indexIdPost));
            al.add(comment);
        }
        return al;
    };

    protected void deleteComment(Comment comment){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(COMMENT_TABLE, COMMENT_TEXT + " = " + comment.getCommentText(), null);
    }


}
