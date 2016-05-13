package creators.co.eventification.ui;

import android.app.Application;
import android.content.Context;

/**
 * This is the Main Application and I use it to get the
 * application context at any time during the application lifecycle.
 * (Created by Vidhi on 6/17/2015.)
 */
public class MainApplication extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getContext()
    {
        return context;
    }
}
