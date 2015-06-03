package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by berkut on 29.05.15.
 */
public class Subsription implements DbConstants {

    private static SQLiteDatabase sqLiteDatabase;
    private SQLiteAdapter sqLiteAdapter;
    private int idUser;
    private int idTag;

    public Subsription(Context context, int idUser, int idTag){
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        this.idUser = idUser;
        this.idTag = idTag;
        insertToSubscription(idUser, idTag);
    }


    /**
     * Записывает в таблицу Subscription информацию о подписках пользователя
     * @param idUser Id юзера
     * @param idTag  Id тега
     * @return позицию в таблице
     */
    protected long insertToSubscription(int idUser, int idTag) {
        sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, idUser);
        cv.put(TAG_ID, idTag);
        return sqLiteDatabase.insert(SUBSCRIPTION_TABLE, null, cv);
    }

    protected int querryIdFromSubscription(int idUser) {
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        String id = String.valueOf(idUser);
        String sqlQuery = "SELECT ID from SUBSCRIPTION where ID_USER = ?";
        Cursor cursor = sqLiteDatabase
                .rawQuery(sqlQuery, new String[]{id});
        int indexId = cursor.getColumnIndex(ID);
        return cursor.getInt(indexId);
    }

    /**
     * Удаляет подписку из Subscription
     * @param idSubscription Id Subscription
     */
    protected void deleteSubscription(int idSubscription){
        sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(SUBSCRIPTION_TABLE, "ID = " + idSubscription, null);
    }


}
