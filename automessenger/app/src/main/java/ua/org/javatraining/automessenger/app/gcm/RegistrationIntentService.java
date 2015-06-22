package ua.org.javatraining.automessenger.app.gcm;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import ua.org.javatraining.automessenger.app.R;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
        Log.i(TAG, "RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                sharedPreferences.edit().putBoolean(GSMPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(GSMPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }
}