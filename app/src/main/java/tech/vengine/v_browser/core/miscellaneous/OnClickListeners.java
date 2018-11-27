package tech.vengine.v_browser.core.miscellaneous;

public class OnClickListeners {

    // WebView Navigation OnClckListeners
    /*public static void webviewNavigationOnClickListeners() {

        // TODO Back Button OnClickListener
        BrowserActivity.mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserActivity.backButtonFunction(true);
                // WebView Go back Button Settings
                // If Possible then go back & send toast else just send toast
                if (BrowserActivity.mWebView != null) {
                    if(BrowserActivity.mWebView.canGoBack()) {
                        CoreWebApplication.webToast(null, "Going Back");
                        BrowserActivity.mWebView.goBack();
                    }
                    else {
                        CoreWebApplication.webToast(null, "There's No Back To Go To!");
                    }
                }
            }
        });

        // TODO Forward Button OnClickListener
        BrowserActivity.mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BrowserActivity.mWebView != null) {
                    if(BrowserActivity.mWebView.canGoForward()) {
                        CoreWebApplication.webToast(null, "Going Forward");
                        BrowserActivity.mWebView.goForward();
                    }
                    else {
                        CoreWebApplication.webToast(null, "There's No Forward To Go To!");
                    }
                }
            }
        });

        // TODO Refresh Button OnClickListener
        BrowserActivity.mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BrowserActivity.mWebView != null) {
                    CoreWebApplication.webToast(null, "Refresh");
                    BrowserActivity.mWebView.reload();
                }
            }
        });

        // TODO Overflow Button OnClickListener
        BrowserActivity.mOverflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Context Menu
                //Toast.makeText(getBaseContext(), "overflow", Toast.LENGTH_LONG).show();
                try {
                    BrowserActivity.CreatePopupMenu(v);
                } catch(Exception ex) { Log.i("on click test",ex.toString()); }
            }
        });

        // Nav Drawer Settings Button OnClickListener
        BrowserActivity.navDrawerSettingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change tracker to true because we are switching between activities
                KeyVariables.SWITCHING_ACTIVITIES = true;
                Intent intent = new Intent(CoreWebApplication.webContext, SettingsActivity.class);
                CoreWebApplication.webContext.startActivity(intent);
                BrowserActivity.navDrawerLayout.closeDrawer(BrowserActivity.navDrawer);

            }
        });
    }
    private static InputMethodManager imm;
    private static ClipboardManager cm;
    private static ClipData clip;
    private static int startSelection;
    private static int endSelection;
    private static String selectedText = "";
    protected static String deletedText = "";
    protected static CharSequence pasteData="";
    private static ClipData.Item item;
    public static boolean softKeyboardShown = false;

    // Clipboard Slider OnclickListeners
    public static void clipboardSliderOnClickListeners() {

        imm = (InputMethodManager)CoreWebApplication.webContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        BrowserActivity.clipboardKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!softKeyboardShown){
                    SearchViewAddressBar.searchPlate.requestFocus();
                    imm.toggleSoftInputFromWindow(SearchViewAddressBar.searchPlate.getWindowToken(), 1, 0);
                    softKeyboardShown = false;
                } else {
                    SearchViewAddressBar.searchPlate.requestFocus();
                    imm.hideSoftInputFromWindow(SearchViewAddressBar.searchPlate.getWindowToken(), 0);
                    softKeyboardShown = true;
                }
            }
        });
        BrowserActivity.clipboardSelectAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (SearchViewAddressBar.searchPlate != null) {
                    SearchViewAddressBar.searchPlate.selectAll();
                }
            }
        });
        BrowserActivity.clipboardCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (SearchViewAddressBar.searchPlate != null) {
                    if (SearchViewAddressBar.searchPlate.getText().toString() != "") {
                        cm = (ClipboardManager)CoreWebApplication.webContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        startSelection = SearchViewAddressBar.searchPlate.getSelectionStart();
                        endSelection = SearchViewAddressBar.searchPlate.getSelectionEnd();
                        selectedText = SearchViewAddressBar.searchPlate.getText().toString().substring(startSelection, endSelection);
                        clip = ClipData.newPlainText("simple text", selectedText);
                        cm.setPrimaryClip(clip);
                        CoreWebApplication.webToast(null, "Copied");
                    } else {
                        CoreWebApplication.webToast(null, "Nothing To Copy");
                    }
                }
            }
        });
        BrowserActivity.clipboardCut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (SearchViewAddressBar.searchPlate != null) {
                    if (SearchViewAddressBar.searchPlate.getText().toString() != "") {
                        cm = (ClipboardManager)CoreWebApplication.webContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        startSelection = SearchViewAddressBar.searchPlate.getSelectionStart();
                        endSelection = SearchViewAddressBar.searchPlate.getSelectionEnd();
                        selectedText = SearchViewAddressBar.searchPlate.getText().toString().substring(startSelection, endSelection);
                        clip = ClipData.newPlainText("simple text", selectedText);
                        cm.setPrimaryClip(clip);
                        SearchViewAddressBar.searchPlate.setText("");
                        CoreWebApplication.webToast(null, "Cut");
                    } else {
                        CoreWebApplication.webToast(null, "Nothing To Cut");
                    }
                }
            }
        });
        BrowserActivity.clipboardPaste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (SearchViewAddressBar.searchPlate != null) {
                    try {
                        item = cm.getPrimaryClip().getItemAt(0);
                        if (item != null) {
                            pasteData = item.getText();
                            SearchViewAddressBar.searchPlate.setText(pasteData);
                            CoreWebApplication.webToast(null, "Pasted");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CoreWebApplication.webToast(null, "Nothing To Paste");
                    }
                }
            }
        });
        BrowserActivity.clipboardDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (SearchViewAddressBar.searchPlate != null) {
                    if (SearchViewAddressBar.searchPlate.getText().toString() != "") {
                        startSelection = SearchViewAddressBar.searchPlate.getSelectionStart();
                        endSelection = SearchViewAddressBar.searchPlate.getSelectionEnd();
                        selectedText = SearchViewAddressBar.searchPlate.getText().toString().replace(SearchViewAddressBar.searchPlate.getText().toString().substring(startSelection, endSelection), "");
                        SearchViewAddressBar.searchPlate.setText(selectedText);
                        CoreWebApplication.webToast(null, "Deleted");
                    } else {
                        CoreWebApplication.webToast(null, "Nothing To Delete");
                    }
                }
            }
        });
        BrowserActivity.clipboardClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (BrowserActivity.clipboardSlider.getVisibility() == View.VISIBLE){
                    BrowserActivity.clipboardSlider.setVisibility(View.GONE);
                }
            }
        });

    }*/
}
