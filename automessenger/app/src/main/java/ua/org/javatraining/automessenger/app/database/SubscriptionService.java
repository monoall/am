package ua.org.javatraining.automessenger.app.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.entities.User;

import java.util.ArrayList;

public class SubscriptionService implements DbConstants {

    private SQLiteAdapter sqLiteAdapter;

    public SubscriptionService(SQLiteAdapter sqLiteAdapter) {
        this.sqLiteAdapter = sqLiteAdapter;
    }

    public Subscription getSubscribtion(long id){
        Subscription subscription = null;
            SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
            @SuppressLint("Recycle")
            Cursor cursor = sqLiteDatabase
                    .rawQuery(QUERY_SUBSCRIPTION_BY_ID, new String[]{String.valueOf(id)});
            if(cursor!= null){
                cursor.moveToFirst();
                subscription = new Subscription();
                subscription.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                subscription.setTagId(cursor.getString(cursor.getColumnIndex(TAG_NAME)));
                subscription.setUserId(cursor.getString(cursor.getColumnIndex(USER_NAME)));
            }

        return subscription;
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
            cv.put(USER_NAME, subscription.getUserId());
            cv.put(TAG_NAME, subscription.getTagId());
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
     * @param user объект юзера
     * @return Список подписок
     */
    public ArrayList<Subscription> getSubscriptionsList(User user){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        Cursor cursor = sqLiteDatabase
                .rawQuery(QUERY_ALL_TAG_ID_BY_USER_NAME, new String[]{user.getName()});
        int indexUser = cursor.getColumnIndex(USER_NAME);
        int indexTag = cursor.getColumnIndex(TAG_NAME);
        ArrayList<Subscription> al = new ArrayList<Subscription>();
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            Subscription subscription = new Subscription();
            subscription.setUserId(cursor.getString(indexUser));
            subscription.setTagId(cursor.getString(indexTag));
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
        sqLiteDatabase.beginTransaction();
        try{
            sqLiteDatabase.delete(SUBSCRIPTION_TABLE, USER_NAME + " = ? AND " + TAG_NAME + " = ?", new String[]{subscription.getUserId(), subscription.getTagId()});
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

}
