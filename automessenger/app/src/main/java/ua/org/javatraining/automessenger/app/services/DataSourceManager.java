package ua.org.javatraining.automessenger.app.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class DataSourceManager {
    public static DataSource getSource(Context context){
        AsyncTask checkConn = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                boolean c = isConnectedToServer("http://google.com",5000);
                Log.i("mytag","Connection status is: " + c);

                return null;
            }
        };

        checkConn.execute();
        return new LocalDataSource(context);
    }

    public static DataSource getRemoteSource(Context context){
        return new RemoteDataSource(context);
    }

    public static boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
