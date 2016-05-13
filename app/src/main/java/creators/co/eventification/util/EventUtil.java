package creators.co.eventification.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import creators.co.eventification.model.Event;
import creators.co.eventification.model.User;
import creators.co.eventification.services.GetActiveUsersService;
import creators.co.eventification.R;
import creators.co.eventification.services.PostEventUserService;
import creators.co.eventification.ui.MainApplication;

/**
 * Created by vidhigoel on 5/7/16.
 */
public class EventUtil {

    private static String TAG = "EN:EventUtil";

    public static String getActiveUsersURL(String event_id)
    {
        return "http://building-occupant-gateway.com/test/api/getActiveUsers.php?event_id=" + event_id;
    }

    public static String recordAttendanceURL(String event_id, String user_name)
    {
        return "http://building-occupant-gateway.com/test/api/recordAttendance.php?event_id=" + event_id + "&user_name=" + user_name;
    }

    public static String setReachabilityURL(String event_id, String user_name, int status)
    {
        return "http://building-occupant-gateway.com/test/api/setReachability.php?event_id=" + event_id + "&user_name=" + user_name + "&status=" + status;
    }

    public static String getAddEventUrl()
    {
        return "http://building-occupant-gateway.com/test/api/postEventDetails.php";
    }

    public static void invokeGetUsersService(Context context, String event_id)
    {
        logi("::invokeGetUsersService");
        Intent action = new Intent();
        action.setClass(context, GetActiveUsersService.class);
        action.putExtra(Constants.PARAM_EVENT_ID, event_id);
        // TODO: check if service is not already started
        context.startService(action);
    }

    public static void invokePostEventUserService(Context context, Event event, User user){
        logi("::invokePostEventService");
        Intent action = new Intent();
        action.setClass(context, PostEventUserService.class);
        action.putExtra(Constants.EXTRA_EVENT_DETAILS, event);
        action.putExtra(Constants.EXTRA_USER_DETAILS, user);
        // TODO: check if service is not already started
        context.startService(action);

    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public static Notification buildNotification(Context context, String title, String text) {

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                //.setContentIntent(getPendingAction(context, Constants.ACTION_TAKE_SURVEY, bundle))
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .build();

        return notification;
    }

    public static boolean updateStoredEvents(String name, String location){

        if(Globals.storedEvents.containsKey(name) && Globals.storedEvents.get(name).equals(location))
            return false;
        Globals.storedEvents.put(name, location);
        return true;
    }

    public static SharedPreferences getPreference()
    {
        return getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor()
    {
        return getPreference().edit();
    }

    private static Context getContext()
    {
        return MainApplication.getContext();
    }
    /*
Method for printing the log messages
*/
    private static void logi(String msg) {
        Log.i(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }

}

