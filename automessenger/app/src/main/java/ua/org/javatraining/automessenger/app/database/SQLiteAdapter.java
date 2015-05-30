package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by berkut on 29.05.15.
 */
public class SQLiteAdapter extends SQLiteOpenHelper implements DbConstants {

    private static SQLiteAdapter sQLiteAdapter;
    private static SQLiteDatabase sqLiteDatabase;

    private SQLiteAdapter(Context context){
        super(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_CREATE);
        db.execSQL(TAG_CREATE);
        db.execSQL(SUBSCRIPTION_CREATE);
        db.execSQL(POST_CREATE);
        db.execSQL(COMMENT_CREATE);
        db.execSQL(GRADE_POST_CREATE);
        db.execSQL(GRADE_COMMENT_CREATE);
        db.execSQL(PHOTO_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static synchronized SQLiteAdapter initInstance(Context context) {
        if (sQLiteAdapter == null) {
            sQLiteAdapter = new SQLiteAdapter(context);
        }
        return sQLiteAdapter;
    }

    /**
     * Подключает к БД и разрешает чтение
     *
     * @return SQLiteAdapter
     * @throws android.database.SQLException
     */
    protected SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteDatabase = sQLiteAdapter.getReadableDatabase();
        Log.d("myLogs", "read");
        return this;
    }

    /**
     * Подключает к БД и разрешает запись
     *
     * @return SQLiteAdapter
     * @throws android.database.SQLException
     */
    protected SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteDatabase = sQLiteAdapter.getWritableDatabase();
        Log.d("myLogs", "write");
        return this;
    }

    /**
     * Записывает в таблицу User нового юзера
     * @param userName имя юзера
     * @return позицию в таблице
     */
    protected long insertToUser(String userName) {
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, userName);
        return sqLiteDatabase.insert(USER_TABLE, null, cv);
    }


    /**
     * Выбирает ID юзера по его имени
     * @param userName имя юзера
     * @return Id юзера или -1, если такого юзера не существует в таблице
     */
    protected String querryIdFromUser(String userName) {
        String sqlQuery = "SELECT ID from USER where USER_NAME = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{userName});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }

    /**
     * Удаляет юзера
     * @param userName имя юзера
     */
    protected void deleteUser(String userName){
        int id = Integer.parseInt(querryIdFromUser(userName));
        sqLiteDatabase.delete(USER_TABLE, "ID = " + id, null);
    }

    /**
     * Записывает в таблицу Tag новый тег
     * @param tagName имя тега
     * @return позицию в таблице
     */
    protected long insertToTag(String tagName) {
        ContentValues cv = new ContentValues();
        cv.put(TAG_NAME, tagName);
        return sqLiteDatabase.insert(TAG_TABLE, null, cv);
    }

    /**
     * Выбирает ID тега по его имени
     * @param tagName имя юзера
     * @return Id тега или -1, если такого тега не существует в таблице
     */
    protected String querryIdFromTag(String tagName) {
        String sqlQuery = "SELECT ID from TAG where TAG_NAME = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{tagName});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }

    /**
     * Удаляет тег
     * @param tagName имя тега
     */
    protected void deleteTag(String tagName){
        int id = Integer.parseInt(querryIdFromTag(tagName));
        sqLiteDatabase.delete(TAG_TABLE, "ID = " + id, null);
    }

    /**
     * Записывает в таблицу Subscription информацию о подписках пользователя
     * @param idUser Id юзера
     * @param idTag  Id тега
     * @return позицию в таблице
     */
    protected long insertToSubscription(int idUser, int idTag) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, idUser);
        cv.put(TAG_ID, idTag);
        return sqLiteDatabase.insert(SUBSCRIPTION_TABLE, null, cv);
    }

    protected String querryIdFromSubscription(int idUser) {
        String id = String.valueOf(idUser);
        String sqlQuery = "SELECT ID from SUBSCRIPTION where ID_USER = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{id});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }

    /**
     * Удаляет подписку из Subscription
     * @param idSubscription Id Subscription
     */
    protected void deleteSubscription(int idSubscription){
        sqLiteDatabase.delete(SUBSCRIPTION_TABLE, "ID = " + idSubscription, null);
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
    protected long insertToPost(int idUser, String postText, int postDate, String postLocation, int idTag) {
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
    protected String querryIdFromPost(String textPost) {
        String sqlQuery = "SELECT ID from POST where POST_TEXT = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{textPost});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }


    public ArrayList<String[]> queryPosts() {
        Cursor cursor = sqLiteDatabase.query(POST_TABLE, null,
                null, null, null, null, null);
        ArrayList<String[]> al = new ArrayList<String[]>();

        int indexPostText = cursor.getColumnIndex(POST_TEXT);
        int indexPostDate = cursor.getColumnIndex(POST_DATE);
        int indexPostLocation = cursor.getColumnIndex(POST_LOCATION);
        int indexUserId = cursor.getColumnIndex(USER_ID);
        int indexTagId = cursor.getColumnIndex(TAG_ID);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] ar = new String[5];
            ar[0] = cursor.getString(indexPostText);
            ar[1] = cursor.getString(indexPostDate);
            ar[2] = cursor.getString(indexPostLocation);
            ar[3] = cursor.getString(indexUserId);
            ar[4] = cursor.getString(indexTagId);
            al.add(ar);
        }
        return al;
    }

    /**
     * Удаляет Post
     * @param idPost Id поста
     */
    protected void deletePost(int idPost){
        sqLiteDatabase.delete(POST_TABLE, "ID = " + idPost, null);
    }

    /**
     * Записывает в таблицу Comment информацию о комментариях
     * @param idUser Id юзера
     * @param commentDate дата комментария
     * @param commentText текст комментария
     * @param idPost Id поста
     * @return позицию в таблице
     */
    protected long insertToComment(int idUser, int commentDate, String commentText, int idPost) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, idUser);
        cv.put(COMMENT_DATE, commentDate);
        cv.put(COMMENT_TEXT, commentText);
        cv.put(ID_POST, idPost);
        return sqLiteDatabase.insert(COMMENT_TABLE, null, cv);
    }

    protected String querryIdFromComment(String textComment, int idPost) {
        String id = String.valueOf(idPost);
        String sqlQuery = "SELECT ID from COMMENT where COMMENT_TEXT = ? and ID_POST = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{textComment, id});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }

    /**
     * Удаляет Комментарий
     * @param idComment Id комментария
     */
    protected void deleteComment(int idComment){
        sqLiteDatabase.delete(COMMENT_TABLE, "ID = " + idComment, null);
    }

    /**
     * Записывает в таблицу GrandePost информацию
     * @param idUser Id юзера
     * @param idPost Id поста
     * @return позицию в таблице
     */
    protected long insertToGrandePost(int idUser, int idPost, int grade) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, idUser);
        cv.put(ID_POST, idPost);
        cv.put(GRADE, grade);
        return sqLiteDatabase.insert(GRADE_POST_TABLE, null, cv);
    }

    protected String querryIdFromGrandePost(int idUser) {
        String id = String.valueOf(idUser);
        String sqlQuery = "SELECT ID from GRADE_POST where ID_USER = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{id});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }

    /**
     * Удаляет GrandePost
     * @param idGrandePost Id GrandePost
     */
    protected void deleteGrandePost(int idGrandePost){
        sqLiteDatabase.delete(GRADE_POST_TABLE, "ID = " + idGrandePost, null);
    }

    /**
     * Записывает в таблицу GrandeComment информацию
     * @param idUser Id юзера
     * @param idComment Id комментария
     * @return позицию в таблице
     */
    protected long insertToGrandeComment(int idUser, int idComment, int grade) {
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, idUser);
        cv.put(ID_COMMENT, idComment);
        cv.put(GRADE, grade);
        return sqLiteDatabase.insert(GRADE_COMMENT_TABLE, null, cv);
    }


    protected String querryIdFromGrandeComment(int idUser) {
        String id = String.valueOf(idUser);
        String sqlQuery = "SELECT ID from GRADE_COMMENT where ID_USER = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{id});
        int indexId = cursor.getColumnIndex(ID);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            return cursor.getString(indexId);
        }
        return String.valueOf(-1);
    }


    /**
     * Удаляет GrandePost
     * @param idGrandeComment Id GrandePost
     */
    protected void deleteGrandeComment(int idGrandeComment){
        sqLiteDatabase.delete(GRADE_COMMENT_TABLE, "ID = " + idGrandeComment, null);
    }
}
