package ua.org.javatraining.automessenger.app.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import ua.org.javatraining.automessenger.app.dataSourceServices.DataSource;
import ua.org.javatraining.automessenger.app.database.*;
import ua.org.javatraining.automessenger.app.entities.Subscription;
import ua.org.javatraining.automessenger.app.App;
import ua.org.javatraining.automessenger.app.vo.UploadQueueItem;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class Uploader extends IntentService {

    private UploadQueueService queueService;
    private CommentService commentService;
    private GradeCommentService gradeCommentService;
    private GradePostService gradePostService;
    private SubscriptionService subscriptionService;
    private DataSource remoteSource;
    private DataSource localSource;
    private boolean connectionStatus;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectionStatus = intent.getBooleanExtra(ConnectionMonitor.CONNECTION_STATUS, false);
            Log.i("mytag", "Uploader, mReceiver, onReceive, connectionStatus = " + connectionStatus);
        }
    };

    public Uploader() {
        super("Uploader");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteAdapter sqLiteAdapter = SQLiteAdapter.initInstance(this);
        queueService = new UploadQueueService(sqLiteAdapter);
        commentService = new CommentService(sqLiteAdapter);
        gradeCommentService = new GradeCommentService(sqLiteAdapter);
        gradePostService = new GradePostService(sqLiteAdapter);
        subscriptionService = new SubscriptionService(sqLiteAdapter);
        //remoteSource = DataSourceManager.getInstance().getRemoteSource(this);
        //localSource = DataSourceManager.getInstance().getLocalSource(this);
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(mReceiver, new IntentFilter(ConnectionMonitor.UPDATE_CONNECTION_STATUS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            Log.i("myTag", "Uploader, onHandleIntent, uploadQueue initiated");
            Log.i("myTag", "Uploader, onHandleIntent, connectionStatus = " + connectionStatus);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadQueue() {
        if (connectionStatus) {
            List<UploadQueueItem> items = queueService.getQueue();
            if (!items.isEmpty()) {
                for (UploadQueueItem item : items) {
                    switch (item.getContentType()) {
                        case UploadQueueService.COMMENT:
                            remoteSource.addComment(commentService.getCommentById(Long.getLong(item.getContentIdentifier())));
                            queueService.deleteQueueItem(item.getId());
                            break;
                        case UploadQueueService.COMMENT_GRADE:
                            int cGrade = gradeCommentService.getCommentGrade(Long.getLong(item.getContentIdentifier()), App.getLastUser(this)).getGrade();
                            remoteSource.setCurrentUserCommentGrade(Long.getLong(item.getContentIdentifier()) ,cGrade);
                            queueService.deleteQueueItem(item.getId());
                            break;
                        case UploadQueueService.POST:
                            remoteSource.addPost(localSource.getPostByID(Long.getLong(item.getContentIdentifier())));
                            queueService.deleteQueueItem(item.getId());
                            break;
                        case UploadQueueService.POST_GRADE:
                            int pGrade = gradePostService.getPostGrade(Long.getLong(item.getContentIdentifier()), App.getLastUser(this)).getGrade();
                            remoteSource.setCurrentUserCommentGrade(Long.getLong(item.getContentIdentifier()) ,pGrade);
                            queueService.deleteQueueItem(item.getId());
                            break;
                        case UploadQueueService.SUBSCRIPTION:
                            remoteSource.addSubscription(subscriptionService.getSubscribtion(Long.getLong(item.getContentIdentifier())).getTagId());
                            queueService.deleteQueueItem(item.getId());
                            break;
                        case UploadQueueService.DELETE_SUBSCRIPTION:
                            Subscription s = new Subscription();
                            s.setTagId(item.getExtraText1());
                            s.setUserId(item.getExtraText2());
                            remoteSource.removeSubscription(s);
                            queueService.deleteQueueItem(item.getId());
                            break;
                        case UploadQueueService.USER:
                            remoteSource.setUser(item.getContentIdentifier());
                            queueService.deleteQueueItem(item.getId());
                            break;

                    }
                }
            }
        }
    }

}
