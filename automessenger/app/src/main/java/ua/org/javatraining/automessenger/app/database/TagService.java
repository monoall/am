package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Tag;

/**
 * Created by berkut on 05.06.15.
 */
public class TagService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public TagService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public Tag insertTag(Tag tag) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TAG_NAME, tag.getTagName());
        long id = sqLiteDatabase.insert(TAG_TABLE, null, cv);
        tag.setTagId(id);
        return tag;
    }

    public Tag getTagById(long id){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_TAG_BY_ID, new String[]{String.valueOf(id)});
        int indexUser = cursor.getColumnIndex(USER_NAME);
        Tag tag = new Tag();
        tag.setTagId(id);
        tag.setTagName(cursor.getString(indexUser));
        return tag;
    };

    public void deleteTag(Tag tag){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(TAG_TABLE, TAG_NAME + " = " + tag.getTagName(), null);
    }

}
