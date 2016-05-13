package creators.co.eventification.util;

/**
 * Created by Vidhi on 5/28/2015.
 */
public class Constants {

    public static String PARAM_REQUEST_ID = "requestid";
    public static String PARAM_URL = "url";

    public static String PARAM_RECEIVER = "receiver";
    public static String PARAM_USERS_DATA = "usersdata";
    public static String PARAM_EVENT_ID = "eventid";


    public static String EXTRA_USER_DETAILS = "user_details";
    public static String EXTRA_EVENT_DETAILS = "event_details";
    public static String EXTRA_ADDRESS_STRING = "address";

    //actions
    public static String ACTION_CHECK_ACTIVE_USERS = "co.creators.eventification.check_active_users";


    //json fields

    public static String NAME_VALUE_PAIR_RESPONSE = "response";
    public static String HTTP_POST_RESULT_STRING = "postresultstring";
    public static String HTTP_POST_RESPONSE_CODE = "code";
    public static String HTTP_POST_RESPONSE_DATA = "data";
    public static String JSON_EVENT_NAME = "event_name";
    public static String JSON_EVENT_LOCATION = "event_location";
    public static String JSON_EVENT_SD = "start_date";
    public static String JSON_EVENT_ED = "end_date";
    public static String JSON_USER_NAME = "user_name";
    public static String JSON_USER_PHONE = "user_phone";
    public static String JSON_USER_REACHABILITY = "user_reach";
    public static String JSON_TIMESTAMP = "timestamp";


    //status strings
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";
    public static String OK = "ok";
    public static String ONE_ROW = "1";


    //local storage
    public static String PREFERENCES = "shared_preferences";
    public static String PREF_USER_NAME = "user_name";
    public static String PREF_USER_PHONE = "user_phone";
    public static String PREF_USER_REACHABILITY = "user_reach";
    public static String USER_EMAIL = "user_registered_email";
    public static String AVATAR_TAG = "avatar_tag";
    public static String REG_COMPLETE = "reg_complete";
    public static String DEVICE_WIDTH = "device_width";
    public static String DEVICE_HEIGHT = "device_height";
    public static int LOCATION_REQUEST_INTERVAL = 30 * 60 * 1000;
    public static int LOCATION_REQUEST_FAST_INTERVAL = 10 * 60 * 1000;


}


