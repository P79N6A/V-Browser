package tech.vengine.v_browser.core.miscellaneous;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;

import tech.vengine.v_browser.core.fragments.PinEntrySettingsFragment;

/**
 * Created by Dalton on 7/22/2017.
 */

public class EditTextIntegerPreference extends EditTextPreference {

    private Integer mInteger;

    public EditTextIntegerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    public EditTextIntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    public EditTextIntegerPreference(Context context) {
        super(context);
        getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    @Override
    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();
        mInteger = parseInteger(text);
        persistString(mInteger != null ? mInteger.toString() : null);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking);
    }

    @Override
    public String getText() {
        return mInteger != null ? mInteger.toString() : null;
    }

    private static Integer parseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        Log.i("PREFERENCE DIALOG CLOSE", "TEST");

        if (positiveResult) {

            Log.i("PREFERENCE DIALOG CLOSE", "POSITIVE");

            if (getEditText().getText().length() == 4) {
                Log.i("PREFERENCE DIALOG CLOSE", "Lenth equals 4");
                String value = getEditText().getText().toString();
                Log.i("PREFERENCE VALUE", value);
                PinEntrySettingsFragment.loadAlert = true;
                if (callChangeListener(value)) {
                    setText(value);
                }

            }

        }

    }
}
