package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
