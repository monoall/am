package ua.org.javatraining.automessenger.app.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.org.javatraining.automessenger.app.entityes.Subscription;
import ua.org.javatraining.automessenger.app.entityes.User;

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
            cv.put(USER_NAME, subscription.getNameUser());
            cv.put(TAG_NAME, subscription.getNameTag());
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
            subscription.setNameUser(cursor.getString(indexUser));
            subscription.setNameTag(cursor.getString(indexTag));
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
