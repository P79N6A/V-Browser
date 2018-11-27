package tech.vengine.v_browser.core.miscellaneous;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import tech.vengine.v_browser.core.applications.BrowserApplication;
import tech.vengine.v_browser.core.constants.KeyVariables;
import tech.vengine.v_browser.core.utilities.Functions;

public class CustomWebViewClient {

    public void setCurrentWebSettings(boolean loadWebViewUrl, final Context mContext, WebView mWebView) {
        if (mWebView != null) {
            mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebView.setScrollbarFadingEnabled(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setJavaScriptEnabled(BrowserApplication.defaultPreferences.getBoolean("JAVASCRIPT_SWITCH", true));
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setDisplayZoomControls(BrowserApplication.defaultPreferences.getBoolean("DISPLAY_ZOOM_CONTROLS", false));
            if (BrowserApplication.defaultPreferences.getBoolean("ENABLE_CACHE", false)) {
                mWebView.getSettings().setAppCacheEnabled(true);
                mWebView.getSettings().setAppCachePath(mContext.getCacheDir().getAbsolutePath());
                mWebView.getSettings().setAppCacheEnabled(true);
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                mWebView.getSettings().setAppCacheEnabled(false);
            }
            // Depreciated in API 18 used to speed up WebView processing
            setPluginState(mWebView);
            // Sets the User Agent String based on preference
            mWebView.getSettings().setUserAgentString(Functions.setUserAgentBasedOnPreference());
            mWebView.getSettings().setDomStorageEnabled(true);
        }
        // Load WebView after settings set to WebView
        if (loadWebViewUrl == true) {
            // Checks if intent has set KeyVariables.INTENT_URL note equal to null
            if (KeyVariables.INTENT_URL == null) {
                // Loads The Default Homepage If There Isn't A Intent That Opened WebLockr
                mWebView.loadUrl(setDefaultHomepage());
            } else {
                // Loads The Intents Data If An Intent Was Used To Open WebLockr
                mWebView.loadUrl(KeyVariables.INTENT_URL);
                // Reset back to null
                KeyVariables.INTENT_URL = null;
            }
        }
    }

    // <!-- SET DEFAULT HOMPAGE FUNCTION -->
    private static String urlLink = null;

    // Sets Default Url
    public static String setDefaultHomepage() {
        if (BrowserApplication.defaultPreferences.getBoolean("DEFAULT_HOMEPAGE_URL", true)) {
            urlLink = KeyVariables.HOMPAGE_URL;
        } else {
            urlLink = BrowserApplication.defaultPreferences.getString("CUSTOM_HOMEPAGE_URL", "https://duckduckgo.com/");
        }
        return urlLink;
    }

    @SuppressLint("depreciation")
    private void setPluginState(WebView mWebView) {
        if (BrowserApplication.androidSDK < 18) {
            mWebView.getSettings().setPluginState(setPluginStateViaPreference());
            mWebView.getSettings().setRenderPriority(setRenderPriorityViaPreference());
        }
        return;
    }

    private static String stringState;
    private static WebSettings.PluginState pluginState;
    private static WebSettings.RenderPriority renderState;

    // Gets PluginState from user Preferences
    private WebSettings.PluginState setPluginStateViaPreference() {
        stringState = BrowserApplication.defaultPreferences.getString("PLUGIN_STATE", "OFF");
        if (stringState.equals("ON")) {
            pluginState = WebSettings.PluginState.ON;
        } else if (stringState.equals("ON DEMAND")) {
            pluginState = WebSettings.PluginState.ON_DEMAND;
        } else {
            pluginState = WebSettings.PluginState.OFF;
        }
        return pluginState;
    }

    private WebSettings.RenderPriority setRenderPriorityViaPreference() {
        stringState = BrowserApplication.defaultPreferences.getString("RENDER_STATE", "NORMAL");
        if (stringState.equals("HIGH")) {
            renderState = WebSettings.RenderPriority.HIGH;
        } else {
            renderState = WebSettings.RenderPriority.NORMAL;
        }
        return renderState;
    }

}

