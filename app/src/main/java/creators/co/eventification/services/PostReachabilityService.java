package creators.co.eventification.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import creators.co.eventification.net.HttpGetPostDataService;
import creators.co.eventification.util.Constants;


/**
 *
 * This class is an Android service that is used to post the Survey Response
 * It uses the request ID 201 to call the network service that communicates with the server.
 * (created by Vidhi on 05/25/2015.)
 */
public class PostReachabilityService extends Service {

    private String TAG = "EN:PostReachabilityService";
    private PostReachabilityResponseReceiver mPostReachabilityResponseReceiver;
    private String RESPONSE_SURVEY_DATA_ID = "201";

    /**
     * This method initializes the Result Receiver through which the network services communicates
     * the result of server communication
     */
    public PostReachabilityService() {
        logi(":: PostReachabilityService");
        mPostReachabilityResponseReceiver = new PostReachabilityResponseReceiver(new Handler());
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
        throw null;
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

       /* if(intent != null){
            Question question = intent.getParcelableExtra(OmgConstants.EXTRA_SURVEY_QUESTION_DETAILS);
            Response response = (Response)intent.getSerializableExtra(OmgConstants.EXTRA_SURVEY_RESPONSE);
            int ui_type = intent.getIntExtra(OmgConstants.EXTRA_SURVEY_RESPONSE_UI_TYPE, 0);
            long timestamp = intent.getLongExtra(OmgConstants.EXTRA_TIMESTAMP, 0);
            logi("received response from activity, question = " + question + " and response = " + response );

            Intent uploadAction = new Intent(this, HttpGetPostSurveyDataService.class);
            uploadAction.putExtra(OmgConstants.PARAM_REQUEST_ID, RESPONSE_SURVEY_DATA_ID);
            uploadAction.putExtra(OmgConstants.PARAM_URL, SurveyUtil.getAddSurveyResponseUrl());
            uploadAction.putExtra(OmgConstants.PARAM_RECEIVER, mPostSurveyResponseReceiver);
            uploadAction.putExtra(OmgConstants.EXTRA_SURVEY_QUESTION_DETAILS, question);
            uploadAction.putExtra(OmgConstants.EXTRA_SURVEY_RESPONSE, response);
            uploadAction.putExtra(OmgConstants.EXTRA_SURVEY_RESPONSE_UI_TYPE, ui_type);
            uploadAction.putExtra(OmgConstants.EXTRA_TIMESTAMP, timestamp);
            startService(uploadAction);
        }
*/
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * This class is a Result Receiver that is called when then network service
     * sends a message on completion or failure. The network service communicates
     * to this service via Result Receiver.
     */
    class PostReachabilityResponseReceiver extends ResultReceiver {

        private String TAG = "OMG:PostReachabilityService:PostReachabilityResponseReceiver";
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public PostReachabilityResponseReceiver(Handler handler) {

            super(handler);
            logi(":: PostSurveyResponseReceiver");
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
       /* try{
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
        }
*/
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
