package tech.vengine.v_browser.core.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static ConnectivityManager manager;
    private static NetworkInfo networkInfo;
    private static boolean isAvailable = false;

    // Checks to see if Network is available
    public static boolean isNetworkAvailable(Context ctx) {
        manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
        isAvailable = networkInfo != null && networkInfo.isConnected();
        return isAvailable;
    }

}
