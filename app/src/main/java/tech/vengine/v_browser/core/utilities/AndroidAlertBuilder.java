package tech.vengine.v_browser.core.utilities;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class AndroidAlertBuilder {

    private static AlertDialog.Builder builder;

    public static void AndroidAlert(Context ctx, String title, String message) {

        builder = new AlertDialog.Builder(ctx);

        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
