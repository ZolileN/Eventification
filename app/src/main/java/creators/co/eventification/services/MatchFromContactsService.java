package creators.co.eventification.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import creators.co.eventification.R;
import creators.co.eventification.model.User;
import creators.co.eventification.util.Constants;
import creators.co.eventification.util.EventUtil;

public class MatchFromContactsService extends Service {
    ArrayList<User> users;
    private static String TAG = "EN:MatchFromContactsService";
    public MatchFromContactsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        logi(":: onCreate");
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.
     * Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link android.content.Context#startService}.
     * Do not call this method directly.
     * This method starts the IntentService to download the survey data.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        logi(":: onStartCommand");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            users = bundle.getParcelableArrayList(Constants.EXTRA_USER_DETAILS);

        }

        matchFromPhoneBook();

        return super.onStartCommand(intent, flags, startId);
    }

    private void matchFromPhoneBook() {
        int id = 0;
        for (User user: users){
            logi("User : " + user.getName() + " " + user.getPhone());
            String display_name = EventUtil.getContactName(this, user.getPhone());
            logi("Display name for user : " + user.getName() + " is " + display_name);
            launchNotification(++id, display_name);


        }
    }

    /**
     * This method creates the notification based on the result data
     * received from the server
     */
    private void launchNotification(int id, String name)
    {
        String title = getResources().getString(R.string.nearby_friends_notification_title);
        String text = name + " " + getResources().getString(R.string.nearby_friends_notification_text);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification mNotification = EventUtil.buildNotification(this, title, text );
        if(mNotification != null)
            notificationManager.notify(id, mNotification);

        stopSelf();

    }

    /**
     * Method for printing the log messages
     */
    private void logi(String msg)
    {
        Log.i(TAG, msg);
    }

    private void loge(String msg)
    {
        Log.e(TAG, msg);
    }
}
