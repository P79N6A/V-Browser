package tech.vengine.v_browser.core.applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import java.io.File;

public class BrowserApplication extends Application {
    // Context
    public Context mContext;

    // Variables
    private String APP_PACKAGE_NAME;
    public static int androidSDK = Build.VERSION.SDK_INT;

    // Settings Stuff
    public static SharedPreferences defaultPreferences;
    public static SharedPreferences.Editor defaultPreferencesEditor;

    // Helpers
    protected File cacheDir;

    //Access User Pin
    public static String userPin;

    // V-Browser Preferences
    public static String currentUserPin;
    public static String userPinConfirm = null;

    // Last Launcher Used
    /*public static String lastLauncherUsed;*/

    @Override
    public void onCreate() {
        super.onCreate();
        // Setting Context
        mContext = this;

        // Settings Stuff
        APP_PACKAGE_NAME = getPackageName();
        defaultPreferences = mContext.getSharedPreferences(APP_PACKAGE_NAME + "_preferences", 0);
        defaultPreferencesEditor = defaultPreferences.edit();
        defaultPreferencesEditor.apply();

        //Get User Pin
        userPin = String.valueOf(defaultPreferences.getString("USER_PIN", null));

        // Last Launcher Used
        /*lastLauncherUsed = String.valueOf(BrowserApplication.defaultPreferences.getString("Last_Launcher_Used", null));*/


    }

    // Universal Toast
    public static void webToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
