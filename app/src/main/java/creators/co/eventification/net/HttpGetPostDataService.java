package creators.co.eventification.net;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import creators.co.eventification.model.Event;
import creators.co.eventification.model.User;
import creators.co.eventification.util.Constants;

/**
 *
 * This is the Network communication service and is capable of handling all network communication with
 * the server. It takes the request ID and request data from various services and creates a JSON object for the
 * final response. It then sends the data to the server via HTTP Request/Response mechanism.
 *
 * REUSE THIS EVERY TIME YOU WANT TO PERFORM NETWORK COMMUNICATION
 * (created by Vidhi on 05/16/2015)
 */
public class HttpGetPostDataService extends IntentService {
    private static String TAG = "EN:HttpGetPostDataService";
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public HttpGetPostDataService() {
        super("HttpGetPostSurveyDataService");
    }

    /**
     * This method handles all the requests from the services and calls the corresponding routine
     * to parse the response and recreate the JSON format.
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String requestId = intent.getStringExtra(Constants.PARAM_REQUEST_ID);
            final String url = intent.getStringExtra(Constants.PARAM_URL);
            final ResultReceiver receiver = intent.getParcelableExtra(Constants.PARAM_RECEIVER);

            logi("request id is " + requestId + " and  url is " + url);

                if("101".equals(requestId)) {

                    handleActionGetActiveUsers(url, receiver);
                }
                else if("201".equals(requestId)){
                    ;
                    handleActionPostAttendance(url, receiver);

                }
            else if ("301".equals(requestId)){
                    handleActionPostReachability(url, receiver);
                }
            else if("401".equals(requestId)){
                    Event event = intent.getParcelableExtra(Constants.EXTRA_EVENT_DETAILS);
                    User user = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS);
                    handleActionPostEventDetails(url, receiver, event, user);
            }

        }
    }

    /**
     * Handle action Get Survey in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetActiveUsers(String url, ResultReceiver receiver) {
        logi("::handleActionGetSurvey");

        logi("url is " + url + " and receiver is " + receiver);
        String resultString = communicateToServer(url, "", "");

        logi("Received data from the server " + resultString);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_USERS_DATA, resultString);
        receiver.send(STATUS_FINISHED, bundle);

    }


    /**
     * Handle action Post Contextual response in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPostAttendance(String url, ResultReceiver receiver) {

        logi("url is " + url + " and receiver is " + receiver);
        String resultString = communicateToServer(url, "", "");

        logi("Received data from the server after adding response " + resultString);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.HTTP_POST_RESULT_STRING, resultString);
        receiver.send(STATUS_FINISHED, bundle);
    }


    private void handleActionPostReachability(String url, ResultReceiver receiver) {
        logi("::handleActionGetSensingInterval");

        logi("url is " + url + " and receiver is " + receiver);
        String resultString = communicateToServer(url, "", "");

        logi("Received data from the server " + resultString);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.HTTP_POST_RESULT_STRING, resultString);
        receiver.send(STATUS_FINISHED, bundle);

    }

    /**
     * Handle action Post Survey response in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPostEventDetails(String url, ResultReceiver receiver, Event event, User user) {

        logi("url is " + url + " receiver is " + receiver + " event is " + event);
        try{
            JSONObject responseObject = new JSONObject();
            responseObject.put(Constants.JSON_EVENT_NAME, event.getName());
            responseObject.put(Constants.JSON_EVENT_LOCATION, event.getLocation());
            responseObject.put(Constants.JSON_EVENT_SD, event.getStartdate());
            responseObject.put(Constants.JSON_EVENT_ED, event.getEnddate());
            responseObject.put(Constants.JSON_USER_NAME, user.getName() );
            responseObject.put(Constants.JSON_USER_PHONE, user.getPhone());
            responseObject.put(Constants.JSON_USER_REACHABILITY, user.getReachability());

            String responseString = responseObject.toString();
            logi("final response object is " + responseString);
            String resultString = communicateToServer(url, responseString, Constants.NAME_VALUE_PAIR_RESPONSE);
            logi("Received data from the server after adding response " + resultString);

            Bundle bundle = new Bundle();
            bundle.putString(Constants.HTTP_POST_RESULT_STRING, resultString);
            receiver.send(STATUS_FINISHED, bundle);
        }
        catch(JSONException e)
        {
            loge(e.getMessage().toString());
        }
    }



    /**
     * This method is the only place where I create the HTTP Connection to the server. Then, I post the
     * request/response to the server and record the server response.
     * @param url
     * @param responseString
     * @param responseTag
     * @return
     */
    private String communicateToServer(String url, String responseString, String responseTag) {

        String resultString = new String();
        try {
            int TIMEOUT = 10000;
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            if(responseString != "")
            {
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair(responseTag, responseString));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                logi("Final post string is " + nameValuePairList);
             //   logi("Final entity  is " + readInputStream(httpPost.getEntity().getContent()));
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            resultString = readInputStream(inputStream);
            logi("result string " + resultString);
            inputStream.close();

        }
        catch (UnsupportedEncodingException e)
        {
            loge(e.getMessage().toString());
        }
        catch (ClientProtocolException e)
        {
            loge(e.getMessage().toString());
        }
        catch (IOException e)
        {
            loge(e.getMessage().toString());
        }
        finally {

            return resultString;
        }

    }

    /**
     *This method will read the data from the input stream of http connection
     */
    private String readInputStream(InputStream in)
    {

        BufferedReader bufferedReader = null;
        StringBuffer sb = new StringBuffer();
        String line = "";
        try
        {
        bufferedReader = new BufferedReader(new InputStreamReader(in));

            while((line = bufferedReader.readLine()) != null)
                sb.append(line);
        }
        catch(IOException e)
        {
        loge(e.getMessage().toString());
        }
        finally {
            if(bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (IOException e)
                {
                    loge(e.getMessage().toString());
                }
              }
            }

        return sb.toString();
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
