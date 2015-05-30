package ua.org.javatraining.automessenger.app.database;

import android.content.Context;

/**
 * Created by berkut on 29.05.15.
 */
public class Tag {

    private String nameTag;
    private SQLiteAdapter sqLiteAdapter;

    public Tag(Context context, String nameTag){
        this.nameTag = nameTag;
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        sqLiteAdapter.openToWrite();
        sqLiteAdapter.insertToTag(nameTag);
    }

}
