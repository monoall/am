package com.examplebroad.drivermessager.app;

/**
 * Created by berkut on 15.05.15.
 */
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAdapter {

    private static SQLiteAdapter sQLiteAdapter;
    private static SQLiteHelper sqLiteHelper;
    private static SQLiteDatabase sqLiteDatabase;

    private static final String MYDATABASE_NAME = "driverMessager";
    private static final int MYDATABASE_VERSION = 1;
    private static final String LASTPOSTS = "LASTPOSTS";
    private static final String ID = "ID";
    private static final String NAMENUMBER = "NAMENUMBER";
    private static final String POST = "POST";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";

    private SQLiteAdapter(){

    }

    private SQLiteAdapter(Context context){
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        Log.d("myLogs", "init");
    }

    public static void initInstance(Context context) {
        if (sQLiteAdapter == null) {
            sQLiteAdapter = new SQLiteAdapter(context);
        }
    }

    public static SQLiteAdapter getInstance() {
       /* if (sQLiteAdapter == null) {
            sQLiteAdapter = new SQLiteAdapter(context);
        }*/
        Log.d("myLogs", "getInstance");
        return sQLiteAdapter;
    }


    /**
     * Подключает к БД и разрешает чтение
     *
     * @return SQLiteAdapter
     * @throws android.database.SQLException
     */
    protected SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
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
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        Log.d("myLogs", "write");
        return this;
    }

    protected void close() {
        try {
            sqLiteHelper.close();
            Log.d("myLogs", "close");
        } catch (SQLiteException e) {
            close();
        }
    }



    /**
     * Загружает в таблицу данные
     *
     * @param name
     * @param post
     * @param date
     * @param time
     * @return
     */
    protected long insert(String name, String post, String date,
                        String time) {
        ContentValues cv = new ContentValues();
        // загрузка в колонку c ID
        cv.put(NAMENUMBER, name);
        cv.put(POST, post);
        cv.put(DATE, date);
        cv.put(TIME, time);
        Log.d("myLogs", "insert");
        return sqLiteDatabase.insert(LASTPOSTS, null, cv);
    }


    protected int deleteAll() {
        return sqLiteDatabase.delete(LASTPOSTS, null, null);
    }

    /**
     *
     * @return Возвращает список с массивами данных из БД в History
     */
    protected ArrayList<String[]> queryPosts() {
        Cursor cursor = sqLiteDatabase.query(LASTPOSTS, null,
                null, null, null, null, null);
        Log.d("myLogsget", "query");
        ArrayList<String[]> al = new ArrayList<String[]>();

        int indexName = cursor.getColumnIndex(NAMENUMBER);
        int indexPost = cursor.getColumnIndex(POST);
        int indexDate = cursor.getColumnIndex(DATE);
        int indexTime = cursor.getColumnIndex(TIME);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            String[] ar = new String[4];
            ar[0] = cursor.getString(indexName);
            ar[1] = cursor.getString(indexPost);
            ar[2] = cursor.getString(indexDate);
            ar[3] = cursor.getString(indexTime);
            Log.d("myLogsget", cursor.getString(indexName));
            Log.d("myLogsget", cursor.getString(indexPost));
            Log.d("myLogsget",  cursor.getString(indexDate));
            Log.d("myLogsget", cursor.getString(indexTime));
            al.add(ar);
        }
        return al;
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table IF NOT EXISTS LASTPOSTS (ID integer primary key, NAMENUMBER text not null, POST text, DATE text not null, TIME text not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS LASTPOSTS");
            onCreate(db);
        }
    }
}

