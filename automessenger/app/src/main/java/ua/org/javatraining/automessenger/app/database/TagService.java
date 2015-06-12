package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Tag;

import java.util.ArrayList;

public class TagService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public TagService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставляет новый тег
     * @param tag объект тег
     * @return вставленный объект
     */
    public Tag insertTag(Tag tag) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(TAG_NAME, tag.getTagName());
            id = sqLiteDatabase.insert(TAG_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        tag.setTagId(id);
        return tag;
    }


    /**
     * Возвращает тег по его id
     * @param id id юзера
     * @return объект тег
     */
    public Tag getTagById(long id){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TAG_TABLE, null,
                "ID = ?", new String[]{String.valueOf(id)}, null, null, null);
        Tag tag = null;
        if(cursor.moveToFirst()){
            tag =  buildTag(cursor);
        }
        return tag;
    }


    /**
     * Возвращает список тегов названия которых
     * содержат в себе подстроку str;
     * @param str строка, которую должно
     *            содержать имя тега
     * @return Список тегов
     */
    public ArrayList<Tag> searchTags(String str){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(null, null, null, null, null, null, null);
        ArrayList<Tag> al = new ArrayList<Tag>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Tag tag = buildTag(cursor);
            if(tag.getTagName().contains(str)){
                al.add(tag);
            }
        }
        return al;
    }


    /**
     * Удаляет тег
     * @param tag объект тег
     */
    public void deleteTag(Tag tag){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try{
            sqLiteDatabase.delete(TAG_TABLE, "ID = ?", new String[]{String.valueOf(tag.getTagId())});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private Tag buildTag(Cursor c){
        Tag t = new Tag();
        t.setTagId(c.getLong(c.getColumnIndex(ID)));
        t.setTagName(c.getString(c.getColumnIndex(TAG_NAME)));
        return t;
    }

}
