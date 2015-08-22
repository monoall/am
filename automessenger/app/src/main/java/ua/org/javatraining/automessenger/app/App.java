package ua.org.javatraining.automessenger.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import io.fabric.sdk.android.Fabric;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;
import ua.org.javatraining.automessenger.app.services.GPSMonitor;

public class App extends MultiDexApplication {

    public static final String USERNAME = "username";
    public static final int ACCOUNT_REQUEST_CODE = 3141;
    private String user;
    private static GPSMonitor gpsMonitor;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        DataSourceManager.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static String getLastUser(Context context) {
        SharedPreferences userSettings = context.getSharedPreferences(USERNAME, 0);
        String username = userSettings.getString(USERNAME, "");
        Log.i("username", username);

        return username;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String userAuth(Context context) {
        String user = getLastUser(context);
        return user;
    }


    public static GPSMonitor getGpsMonitor() {
        return gpsMonitor;
    }

    public static void setGpsMonitor(GPSMonitor m) {
        gpsMonitor = m;
    }
}
