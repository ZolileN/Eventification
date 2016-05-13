package creators.co.eventification.listeners;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import creators.co.eventification.util.Constants;

/**
 *  @author Vidhi Goel on 05/16/2015.
 * This class listens to the Android Boot Completed action.
 * It invokes the Configure Service that initializes the Alarms
 * for Polling, Sensing and Wifi actions.
 * This class is the original design and used for Static Sampling.
 *
*/
public class BootUpReceiver extends BroadcastReceiver {

    private static String TAG = "EN:BootUpReceiver";
    private long timeInterval = 10*60*1000;
    public BootUpReceiver() {
    }

    /**
     * This method is called when the BroadcastReceiver is receiving
     * an Intent broadcast.
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            logi("Boot Completed");
            Intent alarmAction = new Intent(Constants.ACTION_CHECK_ACTIVE_USERS);
            PendingIntent pendingAlarm = PendingIntent.getBroadcast(context, 0, alarmAction, 0);
            long firstTime = System.currentTimeMillis();

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, timeInterval, pendingAlarm);
        }
    }

    /**
     * Method for printing the log messages
     */
    private void logi(String msg)
    {
        Log.i(TAG, msg);
    }
}
