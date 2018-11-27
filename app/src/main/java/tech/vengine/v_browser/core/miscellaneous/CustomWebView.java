package tech.vengine.v_browser.core.miscellaneous;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.ByteArrayInputStream;

import tech.vengine.v_browser.R;
import tech.vengine.v_browser.core.applications.BrowserApplication;
import tech.vengine.v_browser.core.constants.KeyVariables;
import tech.vengine.v_browser.core.list_arrays.AdblockerSitesArray;

public class CustomWebView extends WebView {


    protected float currentScale;
    protected String CurrentScreenshot = null;
    // protected webViewTabs webViewTab;
    protected Bitmap image;
    protected Canvas canvas;

    public CustomWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                try {
                    ProgressBar mProgressBar = ((Activity) context).findViewById(R.id.webViewProgressBar);

                    if (progress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
                        mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    }
                    mProgressBar.setProgress(progress);
                    if (progress == 100) {
                        mProgressBar.setVisibility(ProgressBar.GONE);
                    }
                    ((Activity) context).setProgress(progress * 1000);
                } catch (Exception ex) {
                    Log.d(KeyVariables.LOG_TAG, ex.toString());
                }
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(context)
                        .setTitle("App Titler")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .create()
                        .show();
                return true;
            }
        });


        // TODO Auto-generated constructor stub
        setWebViewClient(new WebViewClient() {


            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                currentScale = newScale;
            }

            //AdBlocker Resource Request
            private final ByteArrayInputStream EMPTY = new ByteArrayInputStream("".getBytes());

            @Override
            public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {

                if (Boolean.valueOf(BrowserApplication.defaultPreferences.getBoolean("ADBLOCKR_SWITCH", true)).equals(true)) {
                    if (AdblockerSitesArray.checkUrlInAdblockArray(url)) {
                        Log.i("BLOCKEDURL WITH BYTE: ", url);

                        return new WebResourceResponse("text/plain", "UTF-8", EMPTY);
                    } else {
                        return super.shouldInterceptRequest(view, url);
                    }
                } else {
                    return super.shouldInterceptRequest(view, url);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean shouldOverride = false;
                try {
                    if (url != null && url.length() > 0) {
                        /*int endIndex = url.lastIndexOf(".");
						String newUrl = url.substring(endIndex, endIndex+4);
						String newUrl2;
						try {
							newUrl2 = url.substring(endIndex, endIndex+5);
						}
						catch(Exception ex) {
							newUrl2 = "";
						}
						// Video Code Formatting
						if (newUrl.endsWith(".mp4")
								||  newUrl.endsWith(".3gp")
								||  newUrl2.endsWith(".webm")
								) {
							shouldOverride = true;
							Log.i("Override","Override");

							//KeyVariables.SWITCHING_ACTIVITIES = true;
							Intent intent = new Intent(context, VideoPlayerActivity.class); // .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("VIDEO", url);
							((Activity) CoreWebApplication.webContext).startActivity(intent);
							//  return true;
						}
						else {
							//AdBlocker
							try {
								if ( AdblockerSitesArray.checkUrlInAdblockArray(url) ){
									// Log.i("BLOCKEDURL", url);
									// view.loadUrl("127.0.0.1");
									return true;
								}
							} catch (Exception ex) { Log.e("ADBLOCKER ERROR!!!", ex.toString()); }
							// shouldOverride = false;
						}*/

                        //AdBlocker
                        try {
                            if (AdblockerSitesArray.checkUrlInAdblockArray(url)) {
                                // Log.i("BLOCKEDURL", url);
                                // view.loadUrl("127.0.0.1");
                                return true;
                            }
                        } catch (Exception ex) {
                            Log.e("ADBLOCKER ERROR!!!", ex.toString());
                        }
                        // shouldOverride = false;
                    }
                } catch (Exception ex) {
                    Log.d("Testing", ex.toString());
                }
                try {
                    if (AdblockerSitesArray.checkUrlInAdblockArray(url)) {
                        Log.i("BLOCKEDURL", url);
                        // view.loadUrl("127.0.0.1");
                        return true;
                    }
                } catch (Exception ex) {
                    Log.e("ADBLOCKER ERROR!!!", ex.toString());
                }

                return shouldOverride;

            }

            //  <!-- End Of WebChromeClient -->


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
				/*String webViewTag = null;
				try { webViewTag = view.getTag().toString(); }
				catch (Exception e) { e.printStackTrace(); }
				if (webViewTag != null) {
					for (int i = 0; i < Content.webViewTabList.size(); i++) {
						if (Content.webViewTabList.get(i).TAG == webViewTag) { // Or use equals() if it actually returns an Object.
							webViewTab = Content.webViewTabList.get(i);
						}
					}
					// set the title
					webViewTab.TITLE = view.getTitle();
					// Set the favicon
					webViewTab.ICON = "http://www.google.com/s2/favicons?domain="+url;  // favicon;
					// Set the url
					webViewTab.URL = url;
					if (CoreWebActivity.adapter != null) {
						CoreWebActivity.adapter.notifyDataSetChanged();
					}
				}*/

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // DO SOMETHING AFTER PAGE LOAD HERE
                // ProgressBar spinner =  (ProgressBar) findViewById(R.id.progressBar1);

				/*final WebView tempView = view;

				view.postDelayed(new Runnable() {

					@Override
					public void run() {
						String webViewTag = null;
						try { webViewTag = tempView.getTag().toString(); }
						catch (Exception e) { e.printStackTrace(); }

						if (webViewTag != null) {
							for (int i = 0; i < Content.webViewTabList.size(); i++) {
								if (Content.webViewTabList.get(i).TAG == webViewTag) { // Or use equals() if it actually returns an Object.
									webViewTab = Content.webViewTabList.get(i);
								}
							}
							// Set the title
							webViewTab.TITLE = tempView.getTitle();

							// Set the screenshot
							// int webViewHeight = (int)( view.getContentHeight() * 100 );  // currentScale
							image = Bitmap.createBitmap(tempView.getWidth(), tempView.getHeight(), Config.RGB_565); // ARGB_8888
							canvas = new Canvas(image);
							tempView.draw(canvas);

							webViewTab.THUMBNAIL = image;
						}
					}
				}, 200);

				if (CoreWebActivity.adapter != null) {
					CoreWebActivity.adapter.notifyDataSetChanged();
				}*/
            }
        });
    }


}
