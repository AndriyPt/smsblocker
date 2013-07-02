package org.sms.blocker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public final class Alert {

    public static void show(final Activity activity, final String message) {
        show(activity, message, activity.getString(android.R.string.dialog_alert_title));
    }

    public static void show(final Activity activity, final String message, final String title) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(activity);
        messageBox.setTitle(title);
        messageBox.setIcon(android.R.drawable.ic_dialog_alert);
        messageBox.setMessage(message);
        messageBox.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        messageBox.setCancelable(true);
        messageBox.create().show();
    }

}
