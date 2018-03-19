package zee.example.com.carparking.utilities;

import android.app.Application;
import android.content.Context;

/**
 * Created by Zul Qarnain on 11/28/2017.
 */

public class MyApplication extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

}
