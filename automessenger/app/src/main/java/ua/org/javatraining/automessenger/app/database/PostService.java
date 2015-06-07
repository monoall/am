package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;

/**
 * Created by berkut on 05.06.15.
 */
public class PostService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public  PostService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public Post insertPost(Post post) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(POST_TEXT, post.getPostText());
        cv.put(POST_DATE, post.getPostDate());
        cv.put(POST_LOCATION, post.getPostLocation());
        cv.put(USER_ID, post.getIdUser());
        cv.put(TAG_ID, post.getIdTag());
        long id = sqLiteDatabase.insert(POST_TABLE, null, cv);
        post.setId(id);
        return post;
    }

    public Post getPostById(long id){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(POST_TABLE, null,
                null, null, null, null, null);
        int indexPostText = cursor.getColumnIndex(POST_TEXT);
        int indexPostDate = cursor.getColumnIndex(POST_DATE);
        int indexPostLocation = cursor.getColumnIndex(POST_LOCATION);
        int indexIdUser = cursor.getColumnIndex(USER_ID);
        int indexTagId = cursor.getColumnIndex(TAG_ID);
        cursor.move((int) id);
        Post post = new Post();
        post.setId(id);
        post.setPostText(cursor.getString(indexPostText));
        post.setPostDate(cursor.getInt(indexPostDate));
        post.setPostLocation(cursor.getString(indexPostLocation));
        post.setIdUser(cursor.getInt(indexIdUser));
        post.setIdTag(cursor.getInt(indexTagId));
        return post;
    };


    public ArrayList<Post> getAllPosts(long userId, long tagId){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_POST_BY_USER_ID_AND_TAG_ID, new String[]{String.valueOf(userId), String.valueOf(tagId)});
        int indexPostText = cursor.getColumnIndex(POST_TEXT);
        int indexPostDate = cursor.getColumnIndex(POST_DATE);
        int indexPostLocation = cursor.getColumnIndex(POST_LOCATION);
        int indexIdUser = cursor.getColumnIndex(USER_ID);
        int indexTagId = cursor.getColumnIndex(TAG_ID);

        ArrayList<Post> al = new ArrayList<Post>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Post post = new Post();
            post.setPostText(cursor.getString(indexPostText));
            post.setPostDate(cursor.getInt(indexPostDate));
            post.setPostLocation(cursor.getString(indexPostLocation));
            post.setIdUser(cursor.getInt(indexIdUser));
            post.setIdTag(cursor.getInt(indexTagId));
            al.add(post);
        }
        return al;
    };

    public void deletePost(Post post){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(POST_TABLE, POST_TEXT + " = " + post.getPostText(), null);
    }


}
