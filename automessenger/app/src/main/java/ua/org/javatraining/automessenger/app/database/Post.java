package ua.org.javatraining.automessenger.app.database;

// не завершено

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Post implements DbConstants {

    private static SQLiteDatabase sqLiteDatabase;
    private String postText;
    private int postDate;
    private String postLocation;
    private int idUser;
    private int idTag;

    private static SQLiteAdapter sqLiteAdapter;

    public Post(Context context, String postText, int postDate, String postLocation, int idUser, int idTag){
        this.postText = postText;
        this.postDate = postDate;
        this.postLocation = postLocation;
        this.idUser = idUser;
        this.idTag = idTag;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        sqLiteAdapter.openToWrite();
        insertToPost(idUser, postText, postDate, postLocation, idTag);
    }



    /**
     * Записывает в таблицу Post информацию о поcтах пользователя
     * @param idUser Id юзера
     * @param postText текст поста
     * @param postDate дата поста
     * @param postLocation место расположения, где был сделал пост
     * @param idTag Id тега
     * @return позицию в таблице
     */
    private long insertToPost(int idUser, String postText, int postDate, String postLocation, int idTag) {
        sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, idUser);
        cv.put(POST_TEXT, postText);
        cv.put(POST_DATE, postDate);
        cv.put(POST_LOCATION, postLocation);
        cv.put(TAG_ID, idTag);
        return sqLiteDatabase.insert(POST_TABLE, null, cv);
    }


    /**
     * Выбирает ID поста по его имени
     * @param textPost текст поста
     * @return Id поста или -1, если такого поста не существует в таблице
     */
    protected static int querryIdFromPost(String textPost) {
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        String sqlQuery = "SELECT ID from POST where POST_TEXT = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{textPost});
        int indexId = cursor.getColumnIndex(ID);
        return cursor.getInt(indexId);
    }


    /**
     * Выбирает и упаковывает пост в объект
     * @param context контекст
     * @param id порядковый номер поста
     * @return объект Post
     */
    public static Post getPost(Context context, int id){
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(POST_TABLE, null,
                null, null, null, null, null);
        int indexPostText = cursor.getColumnIndex(POST_TEXT);
        int indexPostDate = cursor.getColumnIndex(POST_DATE);
        int indexPostLocation = cursor.getColumnIndex(POST_LOCATION);
        int indexUserId = cursor.getColumnIndex(USER_ID);
        int indexTagId = cursor.getColumnIndex(TAG_ID);
        cursor.move(id);
        return new Post(context, cursor.getString(indexPostText),
                cursor.getInt(indexPostDate),cursor.getString(indexPostLocation),
                cursor.getInt(indexUserId), cursor.getInt(indexTagId)
                );
    }

    /**
     * Удаляет Post
     */
    protected void deletePost(){
        int idPost = querryIdFromPost(postText);
        sqLiteDatabase.delete(POST_TABLE, "ID = " + idPost, null);
    }

}