package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Subscription;

import java.util.ArrayList;

public class SubscriptionService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public SubscriptionService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    /**
     * Вставить подписку
     * @param subscription объект subscription
     * @return объект subscription
     */
    public Subscription insertSubscription(Subscription subscription) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        long id;
        sqLiteDatabase.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            cv.put(USER_ID, subscription.getIdUser());
            cv.put(TAG_ID, subscription.getIdTag());
            id = sqLiteDatabase.insert(SUBSCRIPTION_TABLE, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
        subscription.setId(id);
        return subscription;
    }

    /**
     * Возвращает все подписки юзера
     * @param userId id юзера
     * @return Список подписок
     */
    public ArrayList<Subscription> getSubscriptionsList(long userId){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_TAG_ID_BY_USER_ID, new String[]{String.valueOf(userId)});
        int indexUser = cursor.getColumnIndex(USER_ID);
        int indexTag = cursor.getColumnIndex(TAG_ID);
        ArrayList<Subscription> al = new ArrayList<Subscription>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Subscription subscription = new Subscription();
            subscription.setIdUser(cursor.getInt(indexUser));
            subscription.setIdTag(cursor.getInt(indexTag));
            al.add(subscription);
        }
        return al;
    }

    /**
     * Удаляет подписку
     * @param subscription объект Subscription
     */
    public void deleteSubscription(Subscription subscription){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        try{
            sqLiteDatabase.delete(SUBSCRIPTION_TABLE, "ID = ?", new String[]{String.valueOf(subscription.getId())});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

}
