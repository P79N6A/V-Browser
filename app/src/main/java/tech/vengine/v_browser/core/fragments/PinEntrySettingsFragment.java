package tech.vengine.v_browser.core.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tech.vengine.v_browser.R;
import tech.vengine.v_browser.core.applications.BrowserApplication;
import tech.vengine.v_browser.core.miscellaneous.EditTextIntegerPreference;
import tech.vengine.v_browser.core.utilities.AndroidAlertBuilder;

public class PinEntrySettingsFragment extends PreferenceFragment {

    EditTextIntegerPreference editTextIntegerPreference;
    public static boolean loadAlert = false;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pin_preferences);
        editTextIntegerPreference = (EditTextIntegerPreference) getPreferenceScreen().findPreference("USER_PIN");
        bindPreferenceSummaryToValue(editTextIntegerPreference);
    }


    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference
                .setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(
                        preference.getContext()).getString(preference.getKey(), ""));
    }

    public void sBindPreferenceSummaryToValueListener(Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener) {
        PinEntrySettingsFragment.sBindPreferenceSummaryToValueListener = sBindPreferenceSummaryToValueListener;
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String key = preference.getKey();
            Context ctx = preference.getContext();
            String stringValue = value.toString();


            if (key.equals("USER_PIN")) {
                if (stringValue.length() == 4) {
                    if (loadAlert) {
                        if (!stringValue.equals((BrowserApplication.defaultPreferences.getString("USER_PIN", null)))) {
                            loadAlert = false;
                            PinDialog(ctx, value.toString());
                            return false;
                        } else {
                            loadAlert = false;
                            BrowserApplication.webToast(ctx, "No Change To Pin Made");
                            return false;
                        }
                    }
                    return false;
                } else {
                    AndroidAlertBuilder.AndroidAlert(ctx, "Woops", "Sorry but pin must be 4 digits long!");
                    return false;
                }
            }
            return true;
        }
    };


    // Pin Confirmation
    public static void PinDialog(final Context ctx, final String stringValue) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup parent = ((ViewGroup) ((Activity) ctx).findViewById(android.R.id.content));
        View rootView = inflater.inflate(R.layout.dialog_pinconfirm, parent, false);

        TextView textView = (TextView) rootView.findViewById(R.id.pin_confirm_message);
        final EditText editText = (EditText) rootView.findViewById(R.id.pin_edit_text);
        Button confirmButton = (Button) rootView.findViewById(R.id.pin_confirm);
        Button cancelButton = (Button) rootView.findViewById(R.id.pin_cancel);

        final Dialog dialog = new Dialog(ctx);
        dialog.setTitle("Confirm Pin");
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                BrowserApplication.webToast(ctx, "No Change To Pin Made");
            }
        });
        dialog.setCancelable(true);

        textView.setText(ctx.getString(R.string.confirm_pin));

        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int submittedTextLength = editText.getText().length();
                String submittedText = editText.getText().toString();

                Log.i("CLICK", submittedText + " : " + stringValue);
                if (submittedTextLength < 4) {
                    dialog.dismiss();
                    BrowserApplication.webToast(ctx, "Sorry Pin Doesn't Match!");
                } else if (submittedTextLength == 4) {
                    if (stringValue.equals(submittedText)) {
                        dialog.dismiss();
                        BrowserApplication.defaultPreferencesEditor.putString("USER_PIN", submittedText);
                        BrowserApplication.defaultPreferencesEditor.commit();
                        BrowserApplication.webToast(ctx, "Pin Updated");

                        ((Activity) ctx).finish();
                        ctx.startActivity(((Activity) ctx).getIntent());

                    } else {
                        dialog.dismiss();
                        BrowserApplication.webToast(ctx, "Sorry Pin Doesnt Match!");
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setContentView(rootView);
        dialog.show();

    }

}
