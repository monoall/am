package ua.org.javatraining.automessenger.app;

/**
 * Created by berkut on 29.05.15.
 */

import android.app.Application;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;

public class App extends Application {
    @Override
    public void onCreate() {
        SQLiteAdapter.initInstance(this);
        super.onCreate();
    }
}
