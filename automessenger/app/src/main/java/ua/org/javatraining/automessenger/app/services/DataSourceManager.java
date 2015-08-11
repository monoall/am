package ua.org.javatraining.automessenger.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class DataSourceManager {

    private static DataSourceManager instance;
    private Context context;
    private boolean connectionStatus;

    public static void init(Context context) {
        instance = new DataSourceManager(context);
    }

    public static DataSourceManager getInstance() {
        if (instance == null) {
            throw new NullPointerException("First must call DataSourceManager.init()");
        }
        return instance;
    }

    private DataSourceManager(Context context) {
        this.context = context;
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                connectionStatus = intent.getBooleanExtra(ConnectionMonitor.CONNECTION_STATUS, false);
                Log.i("mytag", "DataSourceManager, mReceiver, onReceive, connectionStatus = " + connectionStatus);
            }
        };
        LocalBroadcastManager
                .getInstance(context)
                .registerReceiver(mReceiver, new IntentFilter(ConnectionMonitor.UPDATE_CONNECTION_STATUS));
        Log.i("myTag", "DataSourceManager, CONSTRUCTOR");
    }

    public DataSource getPreferedSource(Context context) {
        if (instance == null) {
            throw new NullPointerException("First must call DataSourceManager.init()");
        }
        //todo

        return new LocalDataSource(context);
    }

    public DataSource getRemoteSource(Context context) {
        if (instance == null) {
            throw new NullPointerException("First must call DataSourceManager.init()");
        }
        //todo

        return new RemoteDataSource(context);
    }

    public DataSource getLocalSource(Context context) {
        if (instance == null) {
            throw new NullPointerException("First must call DataSourceManager.init()");
        }
        //todo

        return new LocalDataSource(context);
    }

}
