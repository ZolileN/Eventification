package creators.co.eventification.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


import creators.co.eventification.R;
import creators.co.eventification.model.Event;
import creators.co.eventification.model.User;
import creators.co.eventification.model.UserLocation;
import creators.co.eventification.util.Constants;
import creators.co.eventification.util.EventUtil;
import creators.co.eventification.util.Globals;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    TextView tv1,tv2, tv3;
    private UserLocation mLocation;
    private CalendarSyncTask cs;

    private String TAG = "EN:MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        createAlertDialog(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Constants.LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.LOCATION_REQUEST_FAST_INTERVAL);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        cs = new CalendarSyncTask(this);


    }
    class CalendarSyncTask extends AsyncTask<Void,Void,Void> {
        private Context mContext;

        public CalendarSyncTask(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            String[] proj = new String[]{
                    CalendarContract.Events._ID,
                    CalendarContract.Events.DTSTART,
                    CalendarContract.Events.DTEND,
                    CalendarContract.Events.EVENT_LOCATION,
                    CalendarContract.Events.TITLE};
            Cursor cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, proj, null, null, null);
            if (cursor.moveToFirst()) {
                do{
                    String id = cursor.getString(0);
                    String start_date = cursor.getString(1);
                    String end_date = cursor.getString(2);
                    String location = cursor.getString(3);
                    String title = cursor.getString(4);

                    if(location != null && !location.isEmpty()) {
                        Event event = new Event(id, title, location, start_date, end_date);
                        User user = retrieveUserInfo();
                        logi("title - " + event.getName() + " location - " + event.getLocation() + " start date " + cursor.getString(1) + " end data " + cursor.getString(2));
                        logi("user name : " + user.getName() + "User phone : " + user.getPhone() + " user reach " + user.getReachability());
                        boolean ret = EventUtil.updateStoredEvents(event.getName(), event.getLocation());
                        if(ret)
                            EventUtil.invokePostEventUserService(mContext, event, user);
                    }
                }while (cursor.moveToNext());
            }
            return null;
        }
    }


    private User retrieveUserInfo(){
        SharedPreferences preferences = EventUtil.getPreference();
        String name = preferences.getString(Constants.PREF_USER_NAME, "");
        String phone = preferences.getString(Constants.PREF_USER_PHONE, "");
        String reach = preferences.getString(Constants.PREF_USER_REACHABILITY, "");
        return new User(name, phone, reach);
    }
    private void createAlertDialog(Context context){
        final EditText input = new EditText(this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.name_popup))
                .setCancelable(false)
                .setView(input)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do something
                                String user_name = input.getText().toString();
                                String user_phone = retrieveUserPhone();
                                User user = new User(user_name, user_phone, "1");
                                savePreferences(user);
                                cs.execute();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private String retrieveUserPhone(){
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String phone = telephonyManager.getLine1Number();
        if(phone == null)
            phone = "9999999999";
        return phone;

    }
    private void savePreferences(User user){

        SharedPreferences.Editor editor = EventUtil.getEditor();
        editor.putString(Constants.PREF_USER_NAME, user.getName());
        editor.putString(Constants.PREF_USER_PHONE, user.getPhone());
        editor.putString(Constants.PREF_USER_REACHABILITY, user.getReachability());
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = new UserLocation(location, DateFormat.getTimeInstance().format(new Date()));
        updateUI();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void updateUI() {
        tv1.setText(String.valueOf(mLocation.getLastLocation().getLatitude()));
        tv2.setText(String.valueOf(mLocation.getLastLocation().getLongitude()));
        tv3.setText(mLocation.getLastUpdateTime());
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
