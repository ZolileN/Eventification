package creators.co.eventification.services;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import creators.co.eventification.net.HttpGetPostDataService;
import creators.co.eventification.util.Constants;
import creators.co.eventification.util.EventUtil;

public class GeoCodingService extends Service {
    private static String TAG = "EN:GeoCodingService";
    public GeoCodingService() {
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

        String location = "";
        if(intent != null)
            location = intent.getStringExtra(Constants.EXTRA_ADDRESS_STRING);

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(location , 1);
            logi("Latitude reached here" + location);
            if (addresses.size() > 0)
            {
                logi("latitude" + addresses.get(0).getLatitude());
                logi("Longitude " + addresses.get(0).getLongitude());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
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
