package creators.co.eventification.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.util.Log;

import creators.co.eventification.model.Event;
import creators.co.eventification.model.User;
import creators.co.eventification.net.HttpGetPostDataService;
import creators.co.eventification.util.Constants;
import creators.co.eventification.util.EventUtil;

public class PostEventUserService extends Service {

    private String TAG = "EN:PostEventService";
    private PostEventResponseReceiver mPostEventResponseReceiver;
    private String POST_EVENT_DATA_ID = "401";
    public PostEventUserService() {
        logi(":: PostEventService");
        mPostEventResponseReceiver = new PostEventResponseReceiver(new Handler());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link android.content.Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     * This method sends the following data to server wrt the pushed/default survey:
     * 1. Question details (check Question class)
     * 2. User response (check Response class)
     * 3. UI TYPE chosen by user for survey
     * 4. Timestamp at which user take survey
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){

            Event event = intent.getParcelableExtra(Constants.EXTRA_EVENT_DETAILS);
            User user = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS);
            logi("received event from activity, event = " + event);

            Intent uploadAction = new Intent(this, HttpGetPostDataService.class);
            uploadAction.putExtra(Constants.PARAM_REQUEST_ID, POST_EVENT_DATA_ID);
            uploadAction.putExtra(Constants.PARAM_URL, EventUtil.getAddEventUrl());
            uploadAction.putExtra(Constants.PARAM_RECEIVER, mPostEventResponseReceiver);
            uploadAction.putExtra(Constants.EXTRA_EVENT_DETAILS, event);
            uploadAction.putExtra(Constants.EXTRA_USER_DETAILS, user);

            startService(uploadAction);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * This class is a Result Receiver that is called when then network service
     * sends a message on completion or failure. The network service communicates
     * to this service via Result Receiver.
     */
    class PostEventResponseReceiver extends ResultReceiver {

        private String TAG = "EN:PostAttendanceService:PostEventResponseReceiver";
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public PostEventResponseReceiver(Handler handler) {

            super(handler);
            logi(":: PostEventResponseReceiver");
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
                    updatePostResponse(resultData);
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

        /*
        Method for printing the log messages
         */
        private void logi(String msg)
        {
            Log.i(TAG, msg);
        }
    }

    /**
     * This method updates the UI that the survey response has
     * been added to the server successfully or not based on the response from the server.
     *
     * @param resultData
     */
    private void updatePostResponse(Bundle resultData) {
        String results = resultData.getString(Constants.HTTP_POST_RESULT_STRING);
        /*try{
            JSONObject jsonObject = new JSONObject(results);
            String code = jsonObject.getString(Constants.HTTP_POST_RESPONSE_CODE);
            String dataStatus = jsonObject.getString(Constants.HTTP_POST_RESPONSE_DATA);

            logi("code = " + code + " and data = " + dataStatus);

            if(code.equals("1") && dataStatus.equals("OK"))
            {
                //Toast.makeText(this, getResources().getString(R.string.response_recorded), Toast.LENGTH_SHORT).show();
            }
            Intent uiIntent = new Intent(Constants.ACTION_RESPONSE_ADDED);
            uiIntent.putExtra(Constants.HTTP_POST_RESPONSE_CODE, code);
            uiIntent.putExtra(Constants.HTTP_POST_RESPONSE_DATA, dataStatus);
            sendBroadcast(uiIntent);
        }
        catch(JSONException e)
        {
            loge(e.getMessage().toString());
        }*/

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
