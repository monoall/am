package ua.org.javatraining.automessenger.app.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entities.Comment;

import java.util.ArrayList;

public class CommentService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public CommentService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет в таблицу Comment
     *
     * @param comment объект Comment
     * @return вставленный объект
     */
    public Comment insertComment(Comment comment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COMMENT_DATE, comment.getCommentDate());
            cv.put(COMMENT_TEXT, comment.getCommentText());
            cv.put(USER_NAME, comment.getUserId());
            cv.put(ID_POST, comment.getPostId());
            id = sqLiteDatabase.insert(COMMENT_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
        comment.setId(id);
        return comment;
    }

    /**
     * Возвращает все комментарии к посту
     *
     * @param postId id поста
     * @return Список комментариев
     */
    public ArrayList<Comment> getAllComments(int postId) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_COMMENTS_BY_POST_ID, new String[]{String.valueOf(postId)});
        ArrayList<Comment> al = new ArrayList<Comment>();
        int count = 0;
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (count < 10) {
                Comment comment = buildComment(cursor);
                al.add(comment);
                count++;
            } else {
                break;
            }
        }
        return al;
    }

    public ArrayList<Comment> getAllCommentsNextPage(int postId, long timestamp) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_COMMENTS_BY_POST_ID_NEXT_PAGE, new String[]{String.valueOf(postId), Long.toString(timestamp)});
        ArrayList<Comment> al = new ArrayList<Comment>();
        int count = 0;
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (count < 10) {
                Comment comment = buildComment(cursor);
                al.add(comment);
                count++;
            } else {
                break;
            }
        }
        return al;
    }

    public int getCommentCountByPostID(int postId) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_COMMENT_COUNT_BY_POST_ID, new String[]{String.valueOf(postId)});

        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    /**
     * Удаляет комментарии
     *
     * @param comment объект Comment
     */
    public void deleteComment(Comment comment) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete(COMMENT_TABLE, COMMENT_TEXT + " = ?", new String[]{comment.getCommentText()});
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private Comment buildComment(Cursor c) {
        Comment com = new Comment();
        com.setId(c.getLong(c.getColumnIndex(ID)));
        com.setCommentDate(c.getLong(c.getColumnIndex(COMMENT_DATE)));
        com.setCommentText(c.getString(c.getColumnIndex(COMMENT_TEXT)));
        com.setUserId(c.getString(c.getColumnIndex(USER_NAME)));
        com.setPostId(c.getLong(c.getColumnIndex(ID_POST)));
        return com;
    }


}
