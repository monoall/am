package ua.org.javatraining.automessenger.app.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSourceManager;

public class Authentication extends MultiDexApplication {

    public static final String USERNAME = "username";
    public static final int ACCOUNT_REQUEST_CODE = 3141;
    private String user;

    /**
     * metod returned array google account from this dewice.
     *
     * @param context
     * @return Account array
     */
    static Account[] getGoogleAccounts(Context context) {

        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType("com.google");

        return accounts;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        DataSourceManager.init(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private Account findAccount(Context context, String searchingAccount, String accountType) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(accountType);
        for (Account account : accounts) {
            if (searchingAccount.equals(account.name)) {
                return account;
            }
        }
        return null;
    }

    public static String getLastUser(Context context) {
        SharedPreferences userSettings = context.getSharedPreferences(USERNAME, 0);
        String username = userSettings.getString(USERNAME, "");
        Log.i("username", username);

        return username;

    }

    /**
     * method added new account to android device. If account is already exist return null.
     *
     * @param user
     * @param accountType
     * @return
     */
    private boolean addAccount(String user, String accountType, Context context) {
        Account account = new Account(user, accountType);
        AccountManager accountManager = AccountManager.get(context);
        PackageManager pm = context.getPackageManager();

        try {
            Log.i("MyActivity", String.valueOf(pm.getApplicationInfo("ua.org.javatraining.automessenger.app", PackageManager.GET_META_DATA)));
        } catch (Exception e) {

        }
        return accountManager.addAccountExplicitly(account, null, null);
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
}
