package tech.vengine.v_browser.core.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import tech.vengine.v_browser.R;
import tech.vengine.v_browser.core.applications.BrowserApplication;
import tech.vengine.v_browser.core.constants.KeyVariables;
import tech.vengine.v_browser.core.miscellaneous.Content;
import tech.vengine.v_browser.core.miscellaneous.CustomWebView;
import tech.vengine.v_browser.core.utilities.Functions;
import tech.vengine.v_browser.core.utilities.Network;

import static tech.vengine.v_browser.core.activities.PinEntryActivity.intentString;
import static tech.vengine.v_browser.core.applications.BrowserApplication.webToast;

public class BrowserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // ActionBar & Drawer
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBar mActionBar;
    // ActionBar Search
    private SearchManager mSearchManager;
    private SearchView mSearchView;
    // Loading ProgressBar
    private ProgressBar mProgressBar;
    // WebView
    private CustomWebView mWebView;
    // WebView Navigation
    private ImageButton mBackButton;
    private ImageButton mForwardButton;
    private ImageButton mRefreshButton;
    private ImageButton mOverflowButton;
    // Context
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting Context
        mContext = this;

        // Get String From Intent
        try {
            Intent intent = getIntent();
            intentString = intent.getExtras().getString("IntentString");
            if (intentString != null) {
                KeyVariables.INTENT_URL = intentString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Find The Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Get a support ActionBar corresponding to this toolbar
        mActionBar = getSupportActionBar();

        // Enable the Up button
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // <-------------------- WebView Stuff -------------------->

        // Assigning WebView Varriables
        mWebView = (CustomWebView) findViewById(R.id.browserWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);
        mBackButton = (ImageButton) findViewById(R.id.backButton);
        mForwardButton = (ImageButton) findViewById(R.id.forwardButton);
        mRefreshButton = (ImageButton) findViewById(R.id.refreshButton);
        mOverflowButton = (ImageButton) findViewById(R.id.overflowMenuButton);

        /*// If the Array's size is equal to zero
        if (Content.webViewTabList.size() == 0) { NewWebClient(); }
        // Else it has stuff inside
        else {
            // If an Intent was used to reopen WebLockr
            if (KeyVariables.INTENT_URL != null) { NewWebClient(); }
            // Else just show first tab in Array
            else { checkWebView(0);
                showWebView(0); }
        }*/

        // Setting OnClickListeners
        webviewNavigationOnClickListeners();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BrowserApplication.defaultPreferences.getBoolean("CACHE_CLEANER", false)) {
            Content.webViewTabList.removeAll(Content.webViewTabList);
            Functions.cacheCleaner(mContext);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Checks changed preferences;
        Functions.checkSavedPreferences();
        // Checks and sets the user agent depending on the preference
        Functions.checkUserAgentPreference();
        // Log.i("USER AGENT STRING PREFERENCE", CoreWebApplication.defaultPreferences.getString("USER_AGENT", null));
        if (mWebView != null) {
            Log.i("USER AGENT STRING", mWebView.getSettings().getUserAgentString());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean backButtonFunction(boolean fromNavBar) {
        // WebView Go back Button Settings
        // If Possible then go back & send toast else just send toast
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                BrowserApplication.webToast(mContext, "Going Back");
                mWebView.goBack();
                return false;
            } else {
                if (fromNavBar) {
                    BrowserApplication.webToast(mContext, "You Can't Go Any Further Back!");
                }
                return true;
            }
        } else {
            return true;
        }
    }

    // WebView Navigation OnClckListeners
    private void webviewNavigationOnClickListeners() {

        // TODO Back Button OnClickListener
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction(true);
                // WebView Go back Button Settings
                // If Possible then go back & send toast else just send toast
                if (mWebView != null) {
                    if (mWebView.canGoBack()) {
                        BrowserApplication.webToast(mContext, "Going Back");
                        mWebView.goBack();
                    } else {
                        BrowserApplication.webToast(mContext, "There's No Back To Go To!");
                    }
                }
            }
        });

        // TODO Forward Button OnClickListener
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView != null) {
                    if (mWebView.canGoForward()) {
                        BrowserApplication.webToast(mContext, "Going Forward");
                        mWebView.goForward();
                    } else {
                        BrowserApplication.webToast(mContext, "There's No Forward To Go To!");
                    }
                }
            }
        });

        // TODO Refresh Button OnClickListener
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView != null) {
                    BrowserApplication.webToast(mContext, "Refresh");
                    mWebView.reload();
                }
            }
        });

        // TODO Overflow Button OnClickListener
        mOverflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Context Menu
                //Toast.makeText(getBaseContext(), "overflow", Toast.LENGTH_LONG).show();
                try {
                    // CoreWebActivity.CreatePopupMenu(v);
                } catch (Exception ex) {
                    Log.i("on click test", ex.toString());
                }
            }
        });
    }

    // Address/Search Bar
    // Variables
    public EditText searchPlate;
    public MenuItem searchMenuItem;
    private ViewGroup mSearchGroup;
    private LinearLayout mGoButtonParent;
    private ImageButton mGoButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(mSearchManager.getSearchableInfo(getComponentName()));
        mSearchView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        searchPlate = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchPlate.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        searchPlate.setSelectAllOnFocus(true);
        searchPlate.setTextIsSelectable(true);
        searchPlate.setLongClickable(true);

        mGoButtonParent = (LinearLayout) getLayoutInflater().inflate(R.layout.go_button, null);
        mSearchGroup = (ViewGroup) searchPlate.getParent();
        mSearchGroup.addView(mGoButtonParent);

        mGoButton = mSearchGroup.findViewById(R.id.goButton);

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        /*searchPlate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                InputMethodManager imm = (InputMethodManager) arg0.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchPlate.getWindowToken(), 0);
                OnClickListeners.softKeyboardShown = false;
                *//*if (arg0.getContext().clipboardSlider.getVisibility() == View.GONE) {
                    arg0.getContext().clipboardSlider.setVisibility(View.VISIBLE);
                } else if (arg0.getContext().clipboardSlider.getVisibility() == View.VISIBLE) {
                    arg0.getContext().clipboardSlider.setVisibility(View.GONE);
                }*//*
                return true; // Originally return false; but changed to remove context menu
            }
        });*/
        mSearchView.setLongClickable(false);
        // Will be magnifying glass if less than sdk 14
        if (BrowserApplication.androidSDK >= 14) {
            mSearchView.setImeOptions(EditorInfo.IME_ACTION_GO);
        }
        // Close and clear the query on focus change
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    /*if (CoreWebActivity.clipboardSlider.getVisibility() == View.VISIBLE) {
                        CoreWebActivity.clipboardSlider.setVisibility(View.GONE);
                    }*/
                    //searchMenuItem.collapseActionView();
                    mSearchView.setQuery("", false);
                }
            }
        });

        SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Network.isNetworkAvailable(mContext)) {
                    if (!query.equals("")) {
                        searchMenuItem.collapseActionView();
                        mSearchView.setQuery("", false);
                        query = Functions.formatAdressUrl(query, true);
                        goToAddress(query);
                    }
                } else {
                    webToast(mContext, "Network is Unavailable!");
                }
                return true;
            }
        };
        mSearchView.setOnQueryTextListener(mQueryTextListener);

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (CoreWebActivity.mWebView != null) {
                    String url = CoreWebActivity.mWebView.getUrl().toString();
                    mSearchView.setQuery(url, false);
                }*/
            }

        });
        mSearchView.setQueryHint("Search or type URL");


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_tab_new) {

        } else if (id == R.id.nav_bookmarks) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_downloads) {

        } else if (id == R.id.nav_user_agent) {

        } else if (id == R.id.nav_adblock) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_report_problem) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit) {
            ExitActivity.exitApplication(mContext);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sharePage() {
        String shareLinkMassage;
        // If use default message equals true use the default preset message.
        if (BrowserApplication.defaultPreferences.getBoolean("DEFAULT_SHARE_MESSAGE", true)) {
            shareLinkMassage = KeyVariables.SHARE_LINK_MESSAGE;
        }
        // Else use the custom message created by the user.
        else {
            shareLinkMassage = BrowserApplication.defaultPreferences.getString("CUSTOM_SHARE_MESSAGE", KeyVariables.SHARE_LINK_MESSAGE);
        }
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareLinkMassage + "\n\n" + mWebView.getUrl());
            startActivity(Intent.createChooser(shareIntent, "How would you want to share this link?"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goToAddress(String query) {
        String addressUrl = query;
        if (Network.isNetworkAvailable(mContext)) {
            if (addressUrl != "") {
                mWebView.loadUrl(addressUrl);
            }
        } else {
            BrowserApplication.webToast(null, "Network is Unavailable!");
        }
    }

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
