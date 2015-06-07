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

    public Subscription insertSubscription(Subscription subscription) {
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID, subscription.getIdUser());
        cv.put(TAG_ID, subscription.getIdTag());
        long id = sqLiteDatabase.insert(SUBSCRIPTION_TABLE, null, cv);
        subscription.setId(id);
        return subscription;
    }

    public ArrayList<Subscription> getAllSubscriptions(long userId){
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

    protected void deleteSubscription(Subscription subscription){
        SQLiteDatabase sqLiteDatabase = sqLiteAdapter.getReadableDatabase();
        sqLiteDatabase.delete(SUBSCRIPTION_TABLE, "ID = " + subscription.getId(), null);
    }

}
