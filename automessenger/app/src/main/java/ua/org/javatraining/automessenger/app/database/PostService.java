package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;

public class PostService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public  PostService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public Post insertPost(Post post) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(POST_TEXT, post.getPostText());
            cv.put(POST_DATE, post.getPostDate());
            cv.put(POST_LOCATION, post.getPostLocation());
            cv.put(USER_ID, post.getIdUser());
            cv.put(TAG_ID, post.getIdTag());
            id = sqLiteDatabase.insert(POST_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        post.setId(id);
        return post;
    }

    public Post getPostById(long id){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(POST_TABLE, null,
                "ID = ?", new String[]{String.valueOf(id)}, null, null, null);
        Post post = null;
        if(cursor.moveToFirst()){
            post = buildPost(cursor);
        }
        return post;
    }


    public ArrayList<Post> getAllPosts(long userId, long tagId){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_POST_BY_USER_ID_AND_TAG_ID, new String[]{String.valueOf(userId), String.valueOf(tagId)});
        ArrayList<Post> al = new ArrayList<Post>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Post post = buildPost(cursor);
            al.add(post);
        }
        return al;
    }

    public void deletePost(Post post){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(POST_TABLE, POST_TEXT + " = ?", new String[]{post.getPostText()});
    }

    private Post buildPost(Cursor c){
        Post p =  new Post();
        p.setId(c.getLong(c.getColumnIndex(ID)));
        p.setPostText(c.getString(c.getColumnIndex(POST_TEXT)));
        p.setPostDate(c.getInt(c.getColumnIndex(POST_DATE)));
        p.setPostLocation(c.getString(c.getColumnIndex(POST_LOCATION)));
        p.setIdUser(c.getInt(c.getColumnIndex(USER_ID)));
        p.setIdTag(c.getInt(c.getColumnIndex(TAG_ID)));
        return p;
    }

}
