package ua.org.javatraining.automessenger.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class ConnectionMonitor extends IntentService {

    public final String URL = "http://google.com"; //todo change

    public static final String CONNECTION_STATUS = "ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager.connectionStatus";
    public static final String UPDATE_CONNECTION_STATUS = "ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager.updateConnectionStatus";

    public ConnectionMonitor() {
        super("ConnectionMonitor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("myTsg", "ConnectionMonitor, onCreate()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("myTsg", "ConnectionMonitor, onHandleIntent()");
        doWork(URL);
    }

    private void doWork(String url){
        Intent intent = new Intent(UPDATE_CONNECTION_STATUS);

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                URL myUrl = new URL(url);
                URLConnection connection = myUrl.openConnection();
                connection.setConnectTimeout(3000);
                connection.connect();
                Log.i("myTsg", "ConnectionMonitor, doWork, connection status = true");
                intent.putExtra(CONNECTION_STATUS, true);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } catch (Exception e) {
                Log.i("myTsg", "ConnectionMonitor, doWork, doInBackground, connection status = false");
                intent.putExtra(CONNECTION_STATUS, false);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
