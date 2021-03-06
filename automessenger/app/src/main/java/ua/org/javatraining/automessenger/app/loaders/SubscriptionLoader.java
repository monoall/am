package ua.org.javatraining.automessenger.app.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import ua.org.javatraining.automessenger.app.database.SQLiteAdapter;
import ua.org.javatraining.automessenger.app.database.SubscriptionService;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.entities.User;
import ua.org.javatraining.automessenger.app.App;

import java.util.List;

public class SubscriptionLoader extends AsyncTaskLoader<List<Subscription>> {

    private SQLiteAdapter sqLiteAdapter;
    private SubscriptionService subscriptionService;
    private User user;

    public SubscriptionLoader(Context context) {
        super(context);
        user = new User();
        user.setName(App.getLastUser(context));
        sqLiteAdapter = SQLiteAdapter.initInstance(context);
        subscriptionService = new SubscriptionService(sqLiteAdapter);
    }

    @Override
    public List<Subscription> loadInBackground() {
        List<Subscription> result =
                subscriptionService.getSubscriptionsList(user);
        return result;
    }
}
