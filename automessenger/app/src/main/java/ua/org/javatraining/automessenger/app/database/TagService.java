package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Tag;

public class TagService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public TagService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

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

    public void deleteTag(Tag tag){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(TAG_TABLE, "ID = ?", new String[]{String.valueOf(tag.getTagId())});
    }

    private Tag buildTag(Cursor c){
        Tag t = new Tag();
        t.setTagId(c.getLong(c.getColumnIndex(ID)));
        t.setTagName(c.getString(c.getColumnIndex(TAG_NAME)));
        return t;
    }

}
