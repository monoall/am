package ua.org.javatraining.automessenger.app;

import android.app.Application;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;

public class App extends Application {
    @Override
    public void onCreate() {
        SQLiteAdapter.initInstance(this);
        super.onCreate();
    }
}
