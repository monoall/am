package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by berkut on 29.05.15.
 */
public class Tag implements DbConstants {

    private static SQLiteDatabase sqLiteDatabase;
    private String nameTag;
    private static SQLiteAdapter sqLiteAdapter;

    public Tag(Context context, String nameTag){
        this.nameTag = nameTag;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        sqLiteAdapter.openToWrite();
        insertToTag(nameTag);
    }

    /**
     * Записывает в таблицу Tag новый тег
     * @param tagName имя тега
     * @return позицию в таблице
     */
    private long insertToTag(String tagName) {
        sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TAG_NAME, tagName);
        return sqLiteDatabase.insert(TAG_TABLE, null, cv);
    }

    /**
     * Выбирает ID тега по его имени
     * @param tagName имя юзера
     * @return Id тега или -1, если такого тега не существует в таблице
     */
    protected static int querryIdFromTag(String tagName) {
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        String sqlQuery = "SELECT ID from TAG where TAG_NAME = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{tagName});
        int indexId = cursor.getColumnIndex(ID);
        return cursor.getInt(indexId);

    }

    /**
     * Удаляет тег
     */
    protected void deleteTag(){
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        int id = querryIdFromTag(nameTag);
        sqLiteDatabase.delete(TAG_TABLE, "ID = " + id, null);
    }


    /**
     * Выбирает и упаковывает тег в объект
     * @param context контекст
     * @param tagName имя тега
     * @return объект Tag
     */
    public static Tag getTag(Context context, String tagName){
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TAG_TABLE, null,
                null, null, null, null, null);
        int indexTagName = cursor.getColumnIndex(TAG_NAME);
        cursor.move(querryIdFromTag(tagName));
        return new Tag(context, cursor.getString(indexTagName));
    }

}
