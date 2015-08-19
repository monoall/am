package ua.org.javatraining.automessenger.app.services;

import android.app.Service;
import android.content.Intent;
import android.location.*;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import ua.org.javatraining.automessenger.app.vo.ShortLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSMonitor extends Service implements LocationListener {

    private final IBinder mBinder = new LocalBinder();
    private Location location;
    private ShortLocation shortLocation;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("myTag", "GPSMonitor, onCreate");

        shortLocation = new ShortLocation();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myTag", "GPSMonitor, onStartCommand");

        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            //todo

        }

        return START_STICKY;
    }

    @Override
    public LocalBinder onBind(Intent intent) {
        return (LocalBinder) mBinder;
    }

    public class LocalBinder extends Binder {
        public GPSMonitor getService() {
            return GPSMonitor.this;
        }
    }

    /**
     * Return location object with current location
     *
     * @return current location, return NULL if location is unavailable
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Return ShortLocation object with short location of current device
     */
    public ShortLocation getShortLocation() {
        return this.shortLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("myTag", "GPSMonitor, onLocationChanged, Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());

        this.location = location;

        ShortLocationLoader sll = new ShortLocationLoader();
        sll.execute(location.getLatitude(), location.getLongitude());

        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i("myTag", "GPSMonitor, onStatusChanged, s = " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i("myTag", "GPSMonitor, onProviderEnabled, s = " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i("myTag", "GPSMonitor, onProviderDisabled, s = " + s);
    }

    private class ShortLocationLoader extends AsyncTask<Double, Void, ShortLocation> {
        @Override
        protected ShortLocation doInBackground(Double... args) {
            Log.i("myTag", "GPSMonitor, ShortLocationLoader, doInBackground arg0=" + args[0] + ", arg1=" + args[1]);

            ShortLocation sl = new ShortLocation();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

            if (args[0] != null && args[1] != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(args[0], args[1], 1);

                    if (!addresses.isEmpty()) {
                        sl.setCountry(addresses.get(0).getCountryName());
                        sl.setAdminArea(addresses.get(0).getAdminArea());
                        if (addresses.get(0).getLocality() == null) {
                            sl.setRegion(addresses.get(0).getSubAdminArea());
                        } else {
                            sl.setRegion(addresses.get(0).getLocality());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sl;
        }

        @Override
        protected void onPostExecute(ShortLocation sl) {
            super.onPostExecute(sl);

            Log.i("myTag", "GPSMonitor, ShortLocationLoader, onPostExecute, (" + sl.getAdminArea() + ", " + sl.getCountry() + ", " + sl.getRegion() + ")");

            shortLocation = sl;
        }
    }
}
