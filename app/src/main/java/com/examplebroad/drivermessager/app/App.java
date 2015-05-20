package com.examplebroad.drivermessager.app;

/**
 * Created by berkut on 15.05.15.
 */
import android.app.Application;

//@author Братухин Сергей
public class App extends Application {


    @Override
    public void onCreate() {
        SQLiteAdapter.initInstance(this);
        super.onCreate();
    }


}
