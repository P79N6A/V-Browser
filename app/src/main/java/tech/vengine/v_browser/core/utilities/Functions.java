package tech.vengine.v_browser.core.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import tech.vengine.v_browser.core.activities.PinEntryActivity;
import tech.vengine.v_browser.core.applications.BrowserApplication;
import tech.vengine.v_browser.core.constants.KeyVariables;

public class Functions {

    private Activity activity;
    private static Intent intent;
    private static String action;
    private static Uri data;

    // Used to check if an intent was used to open V-Browser
    public static void checkIfIntentStarted(Context ctx) {
        KeyVariables.INTENT_URL = null;
        try {
            // Get the activity that started this.
            Activity act = (Activity) ctx;
            // Get the intent that started this activity
            intent = act.getIntent();
            // Get the action of the intent
            action = intent.getAction();
            // Get the uri of the data
            data = intent.getData();
            // Figure out what to do based on the intent type
            if (action.equals("android.intent.action.VIEW")) {
                // Checks to see if data contains http or https
                if (data.toString().contains("http") || data.toString().contains("https")) {
                    // Checks to see if it is a valid link
                    Log.i("CUSTOM FUNCTION", data.toString());
                    if (Functions.checkDataForAddress(data.toString())) {
                        // Sets The INTENT_URL To The Intents Data
                        PinEntryActivity.intentString = data.toString();
                    } else {
                        // Sets The INTENT_URL To Google Search Url Plus The Intents Data
                        PinEntryActivity.intentString = KeyVariables.GOOGLE_SEARCH_URL + data.toString();
                    }
                } else {
                    // Just in case something opened the app might as well search it in google.
                    PinEntryActivity.intentString = KeyVariables.GOOGLE_SEARCH_URL + data.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean hasDomainName;
    private static int endIndex;

    // Searches Invalid Urls & Cleans
    public static String formatAdressUrl(String url, Boolean search_google) {
        endIndex = url.indexOf(".");
        // Checks to see if has a domain
        hasDomainName = endIndex != -1;
        // If address starts with https or http
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            if (hasDomainName) {
                url = "http://" + url;
            } else {
                if (search_google.equals(true)) {
                    url = KeyVariables.GOOGLE_SEARCH_URL + url;
                }
            }
        }
        // Else
        else {
            if (!hasDomainName) {
                if (search_google.equals(true)) {
                    url = KeyVariables.GOOGLE_SEARCH_URL + url;
                }
            }
        }
        return url;
    }

    //Checks if data is a url
    public static boolean checkDataForAddress(String url) {
        boolean hasDomainName;
        int endIndex = url.indexOf(".");
        // Checks to see if has a domain
        hasDomainName = endIndex != -1;
        // If address starts with ...
        return (url.startsWith("https://") || url.startsWith("http://")) && hasDomainName;
        /*if (url.startsWith("https://") || url.startsWith("http://")) {
            return (url.startsWith("https://") || url.startsWith("http://")) && hasDomainName;
        } else {
            return false;
        }*/
    }

    // Checks saved preferences
    @SuppressLint("SetJavaScriptEnabled")
    public static void checkSavedPreferences() {
        // If Pro does not equal true then disable special Pro Features
        if (!BrowserApplication.defaultPreferences.getBoolean("ENABLE_PRO", true)) {
            BrowserApplication.defaultPreferencesEditor.putBoolean("ADBLOCKR_SWITCH", true);
        }
        // If ADVANCED_SETTINGS does not equal true reset all values depending on your Pro status
        if (!BrowserApplication.defaultPreferences.getBoolean("ADVANCED_SETTINGS", false)) {
            BrowserApplication.defaultPreferencesEditor.putBoolean("DISPLAY_ZOOM_CONTROLS", false);
            BrowserApplication.defaultPreferencesEditor.putBoolean("JAVASCRIPT_SWITCH", true);
            // If Pro equals true then enable ADBLOCKR_SWITCH
            if (BrowserApplication.defaultPreferences.getBoolean("ENABLE_PRO", true)) {
                BrowserApplication.defaultPreferencesEditor.putBoolean("ADBLOCKR_SWITCH", true);
            }
            // Else then disable ADBLOCKR_SWITCH
            else {
                BrowserApplication.defaultPreferencesEditor.putBoolean("ADBLOCKR_SWITCH", false);
            }
            BrowserApplication.defaultPreferencesEditor.putBoolean("ENABLE_CACHE", false);
            BrowserApplication.defaultPreferencesEditor.putBoolean("CACHE_CLEANER", false);
            BrowserApplication.defaultPreferencesEditor.putString("USER_AGENT", "Default");
            BrowserApplication.defaultPreferencesEditor.putString("PLUGIN_STATE", "OFF");
            BrowserApplication.defaultPreferencesEditor.putBoolean("DEFAULT_SHARE_MESSAGE", true);
            BrowserApplication.defaultPreferencesEditor.putString("CUSTOM_SHARE_MESSAGE", KeyVariables.SHARE_LINK_MESSAGE);
            BrowserApplication.defaultPreferencesEditor.commit();
            // Setting User Agent String if WebView not equal to null
//            if (CoreWebActivity.mWebView != null) {
//                CoreWebActivity.mWebView.getSettings().setUserAgentString(null);
//            }
        }
        // Setting Javascript Enabled True or False if WebView not equal to null
//        if (CoreWebActivity.mWebView != null) {
//            CoreWebActivity.mWebView.getSettings().setJavaScriptEnabled(BrowserApplication.defaultPreferences.getBoolean("JAVASCRIPT_SWITCH", true));
//        }

    }

    private static String curentUAPref;

    //Checks and sets the user agent depending on the preference
    public static void checkUserAgentPreference() {
//        if (CoreWebActivity.mWebView != null) {
//            CoreWebActivity.mWebView.getSettings().setUserAgentString(setUserAgentBasedOnPreference());
//        }
    }

    //Checks and sets the user agent depending on the preference
    public static String setUserAgentBasedOnPreference() {
        curentUAPref = BrowserApplication.defaultPreferences.getString("USER_AGENT", null);
        String chromeDesktop = "Chrome - Desktop";
        String safariDesktop = "Safari - Desktop";
        String safariIpad = "Safari - IPad";
        String safariIphone = "Safari - IPhone";
        if (curentUAPref != null) {
            if (curentUAPref.equals(chromeDesktop)) {
                return KeyVariables.UA_DESKTOP_CHROME;
            } else if (curentUAPref.equals(safariDesktop)) {
                return KeyVariables.UA_DESKTOP_SAFARI;
            } else if (curentUAPref.equals(safariIpad)) {
                return KeyVariables.UA_IPAD_SAFARI;
            } else if (curentUAPref.equals(safariIphone)) {
                return KeyVariables.UA_IPHONE_SAFARI;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void cacheCleaner(Context ctx) {
        if (BrowserApplication.defaultPreferences.getBoolean("CACHE_CLEANER", false)) {
            BrowserApplication.defaultPreferencesEditor.putBoolean("CACHE_CLEANER", false);
            BrowserApplication.defaultPreferencesEditor.commit();
        }
        try {
            trimCache(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }

            /*for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }*/
        }
        // The directory is now empty so delete it
        return dir != null && dir.delete();
        /*if (dir != null) {
            return dir.delete();
        } else {
            return false;
        }*/
    }

}
