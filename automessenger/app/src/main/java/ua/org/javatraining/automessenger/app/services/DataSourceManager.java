package ua.org.javatraining.automessenger.app.services;

import android.content.Context;

public class DataSourceManager {
    public static DataSource getSource(Context context){
        return new LocalDataSource(context);
    }
    public static DataSource getRemoteSource(Context context){
        return new RemoteDataSource(context);
    }
}
