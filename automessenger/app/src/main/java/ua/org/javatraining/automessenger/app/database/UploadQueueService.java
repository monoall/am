package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ua.org.javatraining.automessenger.app.entities.*;
import ua.org.javatraining.automessenger.app.vo.UploadQueueItem;

import java.util.ArrayList;
import java.util.List;

public class UploadQueueService implements DbConstants {

    public static final int POST = 1264664243;
    public static final int COMMENT = 23553226;
    public static final int POST_GRADE = 345534354;
    public static final int COMMENT_GRADE = 45644227;
    public static final int USER = 564423007;
    public static final int SUBSCRIPTION = 673321458;
    public static final int DELETE_SUBSCRIPTION = 688821458;

    private SQLiteAdapter sqLiteAdapter;

    public UploadQueueService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public <T extends UploadQueueItemInterface> long insertInQueue(T item) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(CONTENT_TYPE, item.getDataTpe());
            cv.put(CONTENT_IDENTIFIER, item.getIdentifier());
            id = sqLiteDatabase.insert(UPLOAD_QUEUE_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
            Log.i("mytag", "UploadQueueService, insertInQueue(), insert OK, id =  " + id);
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return id;
    }

    public long insertInQueueForDelete(Subscription subscription) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(CONTENT_TYPE, DELETE_SUBSCRIPTION);
            cv.put(CONTENT_IDENTIFIER, subscription.getId());
            cv.put(EXTRA_TEXT_1, subscription.getTagId());
            cv.put(EXTRA_TEXT_2, subscription.getUserId());
            id = sqLiteDatabase.insert(UPLOAD_QUEUE_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
            Log.i("mytag", "UploadQueueService, insertInQueueForDelete(), insert OK, id =  " + id);
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return id;
    }

    public UploadQueueItem getQueueItem(long id) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_UPLOAD_ITEM_BY_ID, new String[]{Long.toString(id)});
        cursor.moveToFirst();
        return build(cursor);
    }

    public List<UploadQueueItem> getQueue(){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_UPLOAD_ITEMS, new String[]{});
        ArrayList<UploadQueueItem> al = new ArrayList<UploadQueueItem>();
        for (cursor.moveToLast(); !(cursor.isBeforeFirst()); cursor.moveToPrevious()) {
            UploadQueueItem item = build(cursor);
            al.add(item);
        }
        return al;
    }

    public void deleteQueueItem(long id) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete(UPLOAD_QUEUE_TABLE, ID + " = ?", new String[]{String.valueOf(id)});
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private UploadQueueItem build(Cursor c) {
        UploadQueueItem upi = new UploadQueueItem();
        upi.setId(c.getLong(c.getColumnIndex(ID)));
        upi.setContentType(c.getInt(c.getColumnIndex(CONTENT_TYPE)));
        upi.setContentIdentifier(c.getString(c.getColumnIndex(CONTENT_IDENTIFIER)));
        return upi;
    }

}
