package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ua.org.javatraining.automessenger.app.entityes.Post;

import java.util.ArrayList;

public class PostService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public  PostService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет пост в таблицу Post
     * @param post объект Post
     * @return вставленный объект
     */
    public Post insertPost(Post post) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(POST_TEXT, post.getPostText());
            cv.put(POST_DATE, post.getPostDate());
            cv.put(POST_LOCATION, post.getPostLocation());
            cv.put(USER_NAME, post.getNameUser());
            cv.put(TAG_NAME, post.getNameTag());
            id = sqLiteDatabase.insert(POST_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        post.setId(id);
        return post;
    }

  /**
     * Возвращает все посты юзера по тегу
     * @param userName имя юзера
     * @param tagName имя тега
     * @return Список постов
     */
    public ArrayList<Post> getAllPosts(String userName, String tagName){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_POST_BY_USER_NAME_AND_TAG_NAME, new String[]{userName, tagName});
        ArrayList<Post> al = new ArrayList<Post>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Post post = buildPost(cursor);
            al.add(post);
        }
        return al;
    }

    /**
     * Возвращает последние 50 постов юзера
     * @param userName имя юзера
     * @return Список постов
     */
    public ArrayList<Post> getPostsFromSubscribes(String userName){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_POST_BY_USER_NAME, new String[]{userName});
        ArrayList<Post> al = new ArrayList<Post>();
        int count = 0;
        for (cursor.moveToLast(); !(cursor.isBeforeFirst()); cursor.moveToPrevious()) {
            if(count < 50){
                Post post = buildPost(cursor);
                al.add(post);
                count++;
            }else {
                break;
            }
        }
        return al;
    }

    /**
     * Возвращает последние 50 постов по локации
     * @param location локация
     * @return Список постов
     */
    public ArrayList <Post> getPostsFromNearby(String location){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_POST_BY_LOCATION, new String[]{location});
        ArrayList<Post> al = new ArrayList<Post>();
        int count = 0;
        for (cursor.moveToLast(); !(cursor.isBeforeFirst()); cursor.moveToPrevious()) {
            if(count < 50){
                Post post = buildPost(cursor);
                al.add(post);
                count++;
            }else {
                break;
            }
        }
        return al;
    }


    /**
     * Возвращает все посты по тегу
     * @param tagName имя тега
     * @return Список постов
     */
    public ArrayList <Post> getPostsByTag(String tagName){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_POST_BY_TAG_NAME, new String[]{tagName});
        ArrayList<Post> al = new ArrayList<Post>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Post post = buildPost(cursor);
            al.add(post);
        }
        return al;
    }



    /**
     * Удаляет пост
     * @param post объект Post
     */
    public void deletePost(Post post){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        try{
            sqLiteDatabase.delete(POST_TABLE, POST_TEXT + " = ?" + " and " + USER_NAME + " = ?", new String[]{post.getPostText(), post.getNameUser()});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private Post buildPost(Cursor c){
        Post p =  new Post();
        p.setId(c.getLong(c.getColumnIndex(ID)));
        p.setPostText(c.getString(c.getColumnIndex(POST_TEXT)));
        p.setPostDate(c.getLong(c.getColumnIndex(POST_DATE)));
        p.setPostLocation(c.getString(c.getColumnIndex(POST_LOCATION)));
        p.setNameUser(c.getString(c.getColumnIndex(USER_NAME)));
        p.setNameTag(c.getString(c.getColumnIndex(TAG_NAME)));
        return p;
    }

}
