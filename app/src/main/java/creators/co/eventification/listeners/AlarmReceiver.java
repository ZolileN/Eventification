package creators.co.eventification.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import creators.co.eventification.util.Constants;
import creators.co.eventification.util.EventUtil;

/**
 *
 * This class listens to three different actions:
 * 1. Polling for new active users
 * (created by Vidhi.)
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = "EN:AlarmReceiver";

    private Context mContext;
    public AlarmReceiver() {
    }
/**
 * This method is called when the BroadcastReceiver is receiving
 * an Intent broadcast.
 *
 * @param context application context
 * @param intent calling intent
 *
 */
    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        if(Constants.ACTION_CHECK_ACTIVE_USERS.equals(intent.getAction()))
        {
            long timestamp = System.currentTimeMillis()/1000;
            logi("Timestamp: " + timestamp);
            logi("Poll Server for Active Users");
            String event_id = "1";
            EventUtil.invokeGetUsersService(context, event_id);
        }
    }

    /**
    Method for printing the log messages
     */
    private void logi(String msg)
    {
        Log.i(TAG, msg);
    }
}
