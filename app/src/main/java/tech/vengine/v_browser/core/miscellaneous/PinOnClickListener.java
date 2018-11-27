package tech.vengine.v_browser.core.miscellaneous;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tech.vengine.v_browser.R;
import tech.vengine.v_browser.core.AsyncTasks.LockKeyPadOperation;
import tech.vengine.v_browser.core.activities.PinEntryActivity;
import tech.vengine.v_browser.core.applications.BrowserApplication;

public class PinOnClickListener {

    public static View.OnClickListener pinButtonHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Context ctx = ((View) v.getParent()).getContext();

            // If keypad is locked stop action
            if (PinEntryActivity.keyPadLockedFlag) {
                return;
            }

            // get the value of the button
            Button pressedButton = (Button) v;

            EditText pinTextView = ((Activity) ctx).findViewById(R.id.pin_box_edittext);
            TextView statusTextView = ((Activity) ctx).findViewById(R.id.statusMessage);
            RelativeLayout statusRelativeLayout = ((Activity) ctx).findViewById(R.id.statusMessageLayout);


            // check PinEntryActivity.userEntered string length  - starts as ""
            // if Less than 4
            if (PinEntryActivity.userEntered.length() < PinEntryActivity.PIN_LENGTH) {
                PinEntryActivity.userEntered = PinEntryActivity.userEntered + pressedButton.getText();
                // Log.v("PinView", "User entered="+PinEntryActivity.userEntered);

                // Update pin boxes
                // pinBoxArray[PinEntryActivity.userEntered.length()-1].setText("8");

                // Add button text to the TextView
                String stringPlaceholder = pinTextView.getText().toString() + pressedButton.getText();
                pinTextView.setText(stringPlaceholder);

                // check if PIN equals 4 length yet
                if (PinEntryActivity.userEntered.length() == PinEntryActivity.PIN_LENGTH) {

                    // determine if GUESSING PIN or CREATING PIN
                    if (PinEntryActivity.userPinExists) {
                        // User PIN already exists, check if correct

                        //Check if entered PIN is correct
                        if (PinEntryActivity.userEntered.equals(PinEntryActivity.userPin)) {
                            PinEntryActivity.updateStatus(ctx, false, PinEntryActivity.HoloBlue, Color.GREEN, "", statusRelativeLayout, statusTextView);
                            // finish();

                            PinEntryActivity.launchIntent(ctx);
                        } else {
                            // updateStatus(true, Color.RED,Color.WHITE, "Wrong PIN - try again");
                            PinEntryActivity.updateStatus(ctx, false, Color.RED, Color.WHITE, "Wrong PIN - try again", statusRelativeLayout, statusTextView);
                            PinEntryActivity.keyPadLockedFlag = true;

                            new LockKeyPadOperation(ctx).execute("");
                        }
                    } else {
                        // User PIN doesn't exist

                        // Determine if VERIFYING CREATED PIN, or CREATING
                        if (PinEntryActivity.tempUserPinExists) {
                            // tempUserPinExists - Check if entered PIN is correct
                            if (PinEntryActivity.userEntered.equals(PinEntryActivity.tempUserPin)) {
                                // PIN is correct
                                PinEntryActivity.updateStatus(ctx, false, Color.GREEN, Color.WHITE, "", statusRelativeLayout, statusTextView);

                                // Save the new PIN


                                BrowserApplication.defaultPreferencesEditor.putString("USER_PIN", PinEntryActivity.tempUserPin);
                                BrowserApplication.defaultPreferencesEditor.commit();

                                PinEntryActivity.androidAlert(ctx, "", "Your PIN has been set: " + PinEntryActivity.tempUserPin, true); // MARKER - SET PIN REMINDER If you would like to setup a PIN reminder, do so by setting an email
                                // marker
                            } else {
                                // Entered wrong verification PIN, reset the PIN
                                // updateStatus(true, Color.RED,Color.WHITE, "Wrong PIN - Set your PIN");
                                PinEntryActivity.updateStatus(ctx, false, Color.RED, Color.WHITE, "Wrong PIN - Set your PIN", statusRelativeLayout, statusTextView);
                                PinEntryActivity.tempUserPinExists = false;
                                PinEntryActivity.keyPadLockedFlag = true;
                                new LockKeyPadOperation(ctx).execute("");
                            }
                        } else {

                            // tempUserPin doesn't exist yet, store it


                            // assign temp user PIN
                            PinEntryActivity.tempUserPin = PinEntryActivity.userEntered;
                            PinEntryActivity.tempUserPinExists = true;

                            // reset user entered
                            PinEntryActivity.userEntered = "";

                            // Reset pin on text view
                            pinTextView.setText("");

                            // notify user to verify PIN
                            // updateStatus(true, HoloBlue, Color.WHITE,"Verify your PIN: " + tempUserPin);
                            PinEntryActivity.updateStatus(ctx, false, PinEntryActivity.HoloBlue, Color.WHITE, "Verify your PIN: " + PinEntryActivity.tempUserPin, statusRelativeLayout, statusTextView);
                        }

                    }
                }
            } else {
                //Roll over

                PinEntryActivity.userEntered = "";

                PinEntryActivity.updateStatus(ctx, false, PinEntryActivity.HoloBlue, Color.WHITE, "", statusRelativeLayout, statusTextView);
                // statusTextView.setText("");

                PinEntryActivity.userEntered = PinEntryActivity.userEntered + pressedButton.getText();

                // Reset pin on text view to 1 text
                String stringPlaceholder = pinTextView.getText().toString() + pressedButton.getText();
                pinTextView.setText(stringPlaceholder);

                //Update pin boxes
                // pinBoxArray[PinEntryActivity.userEntered.length()-1].setText("8");

            }


        }
    };

}

