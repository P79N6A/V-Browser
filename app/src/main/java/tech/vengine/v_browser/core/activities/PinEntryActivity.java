package tech.vengine.v_browser.core.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tech.vengine.v_browser.R;
import tech.vengine.v_browser.core.applications.BrowserApplication;
import tech.vengine.v_browser.core.constants.KeyVariables;
import tech.vengine.v_browser.core.miscellaneous.PinOnClickListener;
import tech.vengine.v_browser.core.utilities.Functions;

public class PinEntryActivity extends Activity {

    String androidOS = Build.VERSION.RELEASE;
    static int androidSDK = Build.VERSION.SDK_INT;

    public boolean mAllowScreenshots = true;

    public static String userEntered;
    public static String userEnteredTemp;
    public static String userPin = "";
    public static String tempUserPin = "";
    public static boolean userPinExists = false;
    public static boolean tempUserPinExists = false;

    public final static int PIN_LENGTH = 4;
    public static boolean keyPadLockedFlag = false;

    public EditText pinBoxEditText;

    public RelativeLayout statusLayout;
    public TextView statusView;
    public static boolean statusLayoutIsOpen;
    public static int HoloBlue;

    private static AlertDialog.Builder alertDialogBuilder;
    private static AlertDialog alertDialog;
    private static boolean alertDismissed;

    public Context mContext;

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button buttonExit;
    ImageButton buttonDelete;

    // TypeFace
    Typeface ERAS;
    Typeface xpressiveBold;

    public static String intentString = null;

    public LinearLayout pinParentLayout;
    public LinearLayout pinLayout;
    public LinearLayout pinEntryTop;
    public FrameLayout pinDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Stuff that goes before setContentView
        // Setting Context
        mContext = this;
        setPreContent(mContext);
        setContentView(R.layout.pin_entry_frame);
        // Set Listeners and stuff

        // Set pin entry as last launcher used
        /*if (BrowserApplication.lastLauncherUsed != "Pin_Lock") {
            BrowserApplication.defaultPreferencesEditor.putString("LAST_LAUNCHER_USED", "Pin_Lock");
            BrowserApplication.defaultPreferencesEditor.commit();
        }*/
        pinParentLayout = findViewById(R.id.pinParentLinearLayout);
        pinLayout = findViewById(R.id.pinLinearLayout);
        pinEntryTop = findViewById(R.id.pinEntryTop);
        pinDisplay = findViewById(R.id.pinDisplayLayout);
        setContent(mContext);
    }

    void setPreContent(Context ctx) {

        // skip PIN Entry Screen
        if (KeyVariables.settingsSkipPinEntryScreen) {
            launchIntent(ctx);
        } else {
            // requestWindowFeature(Window.FEATURE_NO_TITLE);
            // getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
            if (!mAllowScreenshots) {
                getWindow().setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE);
            }
            userEntered = "";

            statusLayoutIsOpen = false;

            HoloBlue = Color.parseColor("#33B5E5");
        }
    }

    void setContent(Context ctx) {


        // TypeFace
        xpressiveBold = Typeface.createFromAsset(getAssets(), "fonts/XpressiveBold.ttf");
        ERAS = Typeface.createFromAsset(getAssets(), "fonts/ERAS DEMI ITC.TTF");

        // Exit Button
        buttonExit = findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Exit app
                alertDismissed = false;
                ExitActivity.exitApplication(mContext);
            }
        });
        buttonExit.setTypeface(ERAS);

        buttonDelete = findViewById(R.id.buttonDeleteBack);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (keyPadLockedFlag) {
                    return;
                }
                if (userEntered.length() > 0) {
                    userEntered = userEntered.substring(0, userEntered.length() - 1);
                    pinBoxEditText.setText(userEntered);
                }
            }
        });

        //	titleView = (TextView)findViewById(R.id.titleBox);
        //	titleView.setTypeface(ERAS);

        String sampleString = "sample";
        pinBoxEditText = findViewById(R.id.pin_box_edittext);
        pinBoxEditText.setKeyListener(null);
        if (pinBoxEditText.getText().toString().equals(sampleString)) {
            pinBoxEditText.setText("");
        }

        statusView = findViewById(R.id.statusMessage);
        statusView.setTypeface(ERAS);

        statusLayout = findViewById(R.id.statusMessageLayout);

        button0 = findViewById(R.id.button0);
        button0.setTypeface(ERAS);
        button0.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button1 = findViewById(R.id.button1);
        button1.setTypeface(ERAS);
        button1.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button2 = findViewById(R.id.button2);
        button2.setTypeface(ERAS);
        button2.setOnClickListener(PinOnClickListener.pinButtonHandler);


        button3 = findViewById(R.id.button3);
        button3.setTypeface(ERAS);
        button3.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button4 = findViewById(R.id.button4);
        button4.setTypeface(ERAS);
        button4.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button5 = findViewById(R.id.button5);
        button5.setTypeface(ERAS);
        button5.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button6 = findViewById(R.id.button6);
        button6.setTypeface(ERAS);
        button6.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button7 = findViewById(R.id.button7);
        button7.setTypeface(ERAS);
        button7.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button8 = findViewById(R.id.button8);
        button8.setTypeface(ERAS);
        button8.setOnClickListener(PinOnClickListener.pinButtonHandler);

        button9 = findViewById(R.id.button9);
        button9.setTypeface(ERAS);
        button9.setOnClickListener(PinOnClickListener.pinButtonHandler);

        // Action
        String userPIN = BrowserApplication.defaultPreferences.getString("USER_PIN", null);
        if (userPIN != null) {
            // User PIN already exists
            userPinExists = true;
            userPin = userPIN;
            KeyVariables.settingsUserPin = userPin;

            // move the old pin location (deprecated ) to new pref location
            BrowserApplication.defaultPreferencesEditor.putString("pref_pin", userPIN);
            BrowserApplication.defaultPreferencesEditor.commit();

            // Toast.makeText(getApplicationContext(), userPIN, Toast.LENGTH_SHORT).show();
        } else {
            // User PIN doesn't exist
            userPinExists = false;
            // Toast.makeText(getApplicationContext(), "No PIN", Toast.LENGTH_SHORT).show();
            // updateStatus(true, HoloBlue, Color.WHITE, "Create your new PIN");
            if (!alertDismissed) {
                if (alertDialog != null) {
                    if (!alertDialog.isShowing()) {
                        updateStatus(ctx, false, HoloBlue, Color.WHITE, "Create your new PIN", statusLayout, statusView);
                    }
                } else {
                    updateStatus(ctx, false, HoloBlue, Color.WHITE, "Create your new PIN", statusLayout, statusView);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (keyPadLockedFlag) {
            return;
        }
        if (userEntered.length() > 0) {
            userEntered = userEntered.substring(0, userEntered.length() - 1);
            pinBoxEditText.setText(userEntered);
        } else {
            alertDismissed = false;
            ExitActivity.exitApplication(mContext);
            super.onBackPressed();
        }
    }

    public static void androidAlert(final Context ctx, String title, String msg, boolean exit) {
        alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        if (exit) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDismissed = true;
                            alertDialog.dismiss();
                            launchIntent(ctx);
                        }
                    });
                }
            });
        } else {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDismissed = true;
                            dialog.dismiss();
                        }
                    });
            /*alert.setPositiveButton("OK", null).;*/
        }

        alertDialog.show();
    }

    public static void launchIntent(Context ctx) {
        // Used to check if an intent was used to open weblockr
        Log.i("RUNING LOG", "RUNNING LOG");
        Functions.checkIfIntentStarted(ctx);
        Log.i("RUNING LOG", "RUNNING LOG");

        Intent intent = new Intent(ctx, BrowserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intentString != null) {
            Log.i("PINENTRY INTENT STRING", intentString);
            intent.putExtra("IntentString", intentString);
        }
        ctx.startActivity(intent);
        // startActivityForResult(intent, 1234);
    }

    public static void updateStatus(Context ctx, boolean openStatusLayout, final int layoutColor, final int txtColor, final String msg, final RelativeLayout statusLayout, final TextView statusView) {

        statusView.setTextColor(txtColor);
        statusView.setText(msg);
        statusLayout.setBackgroundColor(layoutColor);

        // if (androidSDK < 14) {
        String emptyString = "";

        if (!msg.equals(emptyString)) {
            androidAlert(ctx, emptyString, msg, false);
        }
        // }

        if (openStatusLayout) {

            if (!statusLayoutIsOpen) {
                statusLayoutIsOpen = true;

                if (androidSDK >= 14) {
                    statusLayout.animate().translationY(-50).setListener(null).start();
                }
            }
        } else {
            if (statusLayoutIsOpen) {
                statusLayout.animate().translationY(0).setListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationCancel(Animator arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        // TODO Auto-generated method stub
                        statusView.setTextColor(txtColor);
                        statusView.setText(msg);
                        statusLayout.setBackgroundColor(layoutColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationStart(Animator arg0) {
                        // TODO Auto-generated method stub

                    }
                }).start();
                statusLayoutIsOpen = false;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (pinParentLayout != null) {
            pinParentLayout.removeAllViews();
        }
        if (pinDisplay != null) {
            pinDisplay.removeAllViews();
        }
        super.onConfigurationChanged(newConfig);
        initui();
        // Checks the orientation of the screen
        /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            startActivity(getIntent());

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            finish();
            startActivity(getIntent());
        }*/
    }

    public void initui() {
        setPreContent(mContext);
        setContentView(R.layout.pin_entry_frame);
        if (pinLayout == null) {
            pinLayout = new LinearLayout(this);
            pinParentLayout.addView(pinLayout);
        }
        if (pinEntryTop == null) {
            pinEntryTop = new LinearLayout(this);
            pinDisplay.addView(pinEntryTop);
        }
        setContent(mContext);
    }

}
