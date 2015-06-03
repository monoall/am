package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by berkut on 29.05.15.
 */
public class User implements DbConstants {

    private static SQLiteDatabase sqLiteDatabase;
    private String nameUser;
    private static SQLiteAdapter sqLiteAdapter;
    private Context context;

    public User(Context context, String nameUser){
        this.context = context;
        this.nameUser = nameUser;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        insertToUser(nameUser);
    }

    /**
     * Записывает в таблицу User нового юзера
     * @param userName имя юзера
     * @return позицию в таблице
     */
    private long insertToUser(String userName) {
        sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, userName);
        return sqLiteDatabase.insert(USER_TABLE, null, cv);
    }

    /**
     * Выбирает ID юзера по его имени
     * @param userName имя юзера
     * @return Id юзера или -1, если такого юзера не существует в таблице
     */
    protected static int querryIdFromUser(String userName) {
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        String sqlQuery = "SELECT ID from USER where USER_NAME = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{userName});
        int indexId = cursor.getColumnIndex(ID);
        return cursor.getInt(indexId);
    }

    /**
     * Собирает все посты этого юзера
     * @return список объектов Post от данного Юзера
     */
    public ArrayList<Post> queryPosts() {
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        String sqlQuery = "SELECT * from POST where ID_USER = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, new String[]{nameUser});
        ArrayList<Post> posts = new ArrayList<Post>();

        int indexPostText = cursor.getColumnIndex(POST_TEXT);
        int indexPostDate = cursor.getColumnIndex(POST_DATE);
        int indexPostLocation = cursor.getColumnIndex(POST_LOCATION);
        int indexUserId = cursor.getColumnIndex(USER_ID);
        int indexTagId = cursor.getColumnIndex(TAG_ID);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            posts.add(new Post(context, cursor.getString(indexPostText),
                    cursor.getInt(indexPostDate),cursor.getString(indexPostLocation),
                    cursor.getInt(indexUserId), cursor.getInt(indexTagId)
            ));
        }
        return posts;
    }

    /**
     * Удаляет юзера
     */
    protected void deleteUser(){
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        int id = querryIdFromUser(nameUser);
        sqLiteDatabase.delete(USER_TABLE, "ID = " + id, null);
    }


    /**
     * Выбирает и упаковывает юзера в объект
     * @param context контексн
     * @param userName имя Юзера
     * @return объект User
     */
    protected static User getUser(Context context, String userName){
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(USER_TABLE, null,
                null, null, null, null, null);
        int indexUserName = cursor.getColumnIndex(USER_NAME);
        cursor.move(querryIdFromUser(userName));
        return new User(context, cursor.getString(indexUserName));
    }


    public void insertTag(String nameTag){
        new Tag(context, nameTag);
    }

}
