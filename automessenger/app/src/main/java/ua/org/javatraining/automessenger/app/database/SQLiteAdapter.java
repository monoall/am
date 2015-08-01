package ua.org.javatraining.automessenger.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("DefaultFileTemplate")
public class SQLiteAdapter extends SQLiteOpenHelper implements DbConstants {

    private static SQLiteAdapter sQLiteAdapter;

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
        db.execSQL(UPLOAD_QUEUE_CREATE);
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

}
