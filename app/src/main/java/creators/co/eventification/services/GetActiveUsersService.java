package creators.co.eventification.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import creators.co.eventification.model.User;
import creators.co.eventification.net.HttpGetPostDataService;
import creators.co.eventification.util.Constants;
import creators.co.eventification.util.EventUtil;


/**
 *
 * This class is an Android service that is used to create the notification UI.
 * It calls an IntentService that downloads the survey data.
 * It uses the request ID 101 to call the network service that communicates with the server.
 * (created by Vidhi on 05/16/2015.)
 */

public class GetActiveUsersService extends Service {

    private static String TAG = "EN:GetActiveUsersService";

    private GetUsersDataReceiver mUsersDataReceiver;
    private String REQUEST_USERS_DATA_ID = "101";
    private String event_id = "";

    /**
     * This method initializes the Result Receiver through which the network services communicates
     * the result of server communication
     */
    public GetActiveUsersService() {
        logi(":: GetActiveUsersService");
        mUsersDataReceiver = new GetUsersDataReceiver(new Handler());
    }

    /**
     *
     * @return an instance of Result Receiver
     */
    public ResultReceiver getSurveyDataReceiver()
    {
        return mUsersDataReceiver;
    }

    /**
     * Return the communication channel to the service.
     * May return null if clients can not bind to the service.
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
            return null;
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

        if(intent != null)
            event_id = intent.getStringExtra(Constants.PARAM_EVENT_ID);

        Intent action = new Intent(this, HttpGetPostDataService.class);
        action.putExtra(Constants.PARAM_REQUEST_ID, REQUEST_USERS_DATA_ID);
        action.putExtra(Constants.PARAM_URL, EventUtil.getActiveUsersURL(event_id));
        action.putExtra(Constants.PARAM_RECEIVER, mUsersDataReceiver);
        startService(action);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * This class is a Result Receiver that is called when then network service
     * sends a message on completion or failure. The network service communicates
     * to this service via Result Receiver.
     */

    class GetUsersDataReceiver extends ResultReceiver {

        private String TAG = "EN:GetActiveUsersService:GetUsersDataReceiver";
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public GetUsersDataReceiver(Handler handler) {

            super(handler);
            logi(":: GetSurveyDataReceiver");
        }

        /**
         * Override to receive results delivered to this object.
         *
         * @param resultCode Result code containing the survey data delivered by the sender.         *
         * @param resultData Additional data like URL, Username etc. provided by the sender.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            logi(":: onReceiveResult");

            switch(resultCode)
            {
                case HttpGetPostDataService.STATUS_RUNNING:
                    break;
                case HttpGetPostDataService.STATUS_FINISHED:
                    // The service was invoked for default survey from Main Activity

                         buildUsers(resultData);

                      stopSelf();
                     break;
                case HttpGetPostDataService.STATUS_ERROR:
                    break;
                default:

            }
        }

        /**
         * Deliver a result to this receiver.  Will call {@link #onReceiveResult},
         * always asynchronously if the receiver has supplied a Handler in which
         * to dispatch the result.
         *
         * @param resultCode Arbitrary result code to deliver, as defined by you.
         * @param resultData Any additional data provided by you.
         */
        @Override
        public void send(int resultCode, Bundle resultData) {
            super.send(resultCode, resultData);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
        }

        /**
        * Method for printing the log messages
        */
        private void logi(String msg)
        {
            Log.i(TAG, msg);
        }
    }


    /**
     * This method creates the notification based on the result data
     * received from the server (Survey_id and push_type)
     */
    private void buildUsers(Bundle resultData)
    {
        String results = resultData.getString(Constants.PARAM_USERS_DATA);
        logi("received data " + results);
        ArrayList<User> users = parseJSONObject(results);

        if(users != null && users.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.EXTRA_USER_DETAILS, users);
            Intent action = new Intent();
            action.setClass(this, MatchFromContactsService.class);
            action.putExtras(bundle);

            startService(action);

        }
        stopSelf();

   }


    /**
     * This method parses the JSON object received from the network service
     * containing the user notification identifiers. Included details are:
     * 1. UserName
     * 2. User Phone
     * @param results result string received from network service as a JSONObject
     * @return list of question objects (refer to Question model)
     */
    private ArrayList parseJSONObject(String results) {

        ArrayList<User> listUsers = new ArrayList<User>();
        try{
            JSONObject jsonObject = new JSONObject(results);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++){
                JSONObject userObject = dataArray.getJSONObject(i);
                String name = userObject.getString("user");
                String phone = userObject.getString("phone");

                User user = new User(name, phone);
                listUsers.add(user);
            }
        }
        catch(JSONException e) {
            loge(e.getMessage().toString());
        }
        return listUsers;

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
