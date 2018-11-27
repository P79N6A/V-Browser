package tech.vengine.v_browser.core.miscellaneous;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import tech.vengine.v_browser.core.constants.KeyVariables;
import tech.vengine.v_browser.core.utilities.Functions;

/**
 * Created by Dalton on 7/22/2017.
 */

public class Intents {

    protected static Activity activity;
    protected static Intent intent;
    protected static String action;
    protected static Uri data;

    // Used to check if an intent was used to open weblockr
    public static void checkIfIntentStarted(Context context) {
        KeyVariables.INTENT_URL = null;
        try {
            // Get the activity that started this.
            activity = (Activity) context;
            // Get the intent that started this activity
            intent = activity.getIntent();
            // Get the action of the intent
            action = intent.getAction();
            // Get the uri of the data
            data = intent.getData();
            // Figure out what to do based on the intent type
            if (action.equals("android.intent.action.VIEW")) {
                // Checks to see if data contains http or https
                if (data.toString().contains("http") || data.toString().contains("https")) {
                    // Checks to see if it is a valid link and sets The INTENT_URL To The Intents Data
                    if (Functions.checkDataForAddress(data.toString())) {
                        KeyVariables.INTENT_URL = data.toString();
                    }
                    // Else sets The INTENT_URL To Google Search Url Plus The Intents Data
                    else {
                        KeyVariables.INTENT_URL = KeyVariables.GOOGLE_SEARCH_URL + data.toString();
                    }
                }
                // Just in case something opened the app might as well search it in google.
                else {
                    KeyVariables.INTENT_URL = KeyVariables.GOOGLE_SEARCH_URL + data.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
