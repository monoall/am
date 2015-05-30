package ua.org.javatraining.automessenger.app.database;

import android.content.Context;

/**
 * Created by berkut on 29.05.15.
 */
public class Subsription {

    private SQLiteAdapter sqLiteAdapter;

    public Subsription(Context context){
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        sqLiteAdapter.openToWrite();
        //sqLiteAdapter.insertToSubscription();
    }
}
