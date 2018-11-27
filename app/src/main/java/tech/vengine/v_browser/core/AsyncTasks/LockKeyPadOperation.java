package tech.vengine.v_browser.core.AsyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import tech.vengine.v_browser.R;
import tech.vengine.v_browser.core.activities.PinEntryActivity;

public class LockKeyPadOperation extends AsyncTask<String, Void, String> {

    private Context mContext;

    public LockKeyPadOperation(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {

        // updateStatus(false,HoloBlue,Color.WHITE,"");
        // statusView.setText("");

        //Roll over
        PinEntryActivity.userEntered = "";
        EditText pinBoxEditText = (EditText) ((Activity) mContext).findViewById(R.id.pin_box_edittext);
        pinBoxEditText.setText(PinEntryActivity.userEntered);
        PinEntryActivity.keyPadLockedFlag = false;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}