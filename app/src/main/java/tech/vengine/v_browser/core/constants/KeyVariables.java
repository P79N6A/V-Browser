package tech.vengine.v_browser.core.constants;

import tech.vengine.v_browser.core.applications.BrowserApplication;

/**
 * Created by Dalton on 7/22/2017.
 */

public class KeyVariables {

    // Log String
    public static final String LOG_TAG = "Main Activity debugger";

    // Non Changing Variables
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?q=";
    public static final String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/?q=";
    public static final String HOMPAGE_URL = "https://duckduckgo.com/";
    public static final String SHARE_LINK_MESSAGE = "Hi, I found this link and thought you might like it:";

    // Non Changing User Agent Variables
    public static final String UA_DESKTOP_CHROME = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19";
    public static final String UA_DESKTOP_SAFARI = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/534.53.11 (KHTML, like Gecko) Version/5.1.3 Safari/534.53.10";
    public static final String UA_IPHONE_SAFARI = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543 Safari/419.3";
    public static final String UA_IPAD_SAFARI = "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10";

    // User Settings / details
    public static boolean settingsSkipPinEntryScreen = BrowserApplication.defaultPreferences.getBoolean("SKIP_PIN_SCREEN", false);
    public static String settingsUserPin = null;

    // Variables That May Change Depending On Conditions
    public static String INTENT_URL;
//	public static boolean SWITCHING_ACTIVITIES = false;
//	public static boolean COMING_BACK_FROM_ACTIVIY = false;


    // WebView variables
//	public static int webViewCount = 0;
//	public static int currentWebView = 0;

}
