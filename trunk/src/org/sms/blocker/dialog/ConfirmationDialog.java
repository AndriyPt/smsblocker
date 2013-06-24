package org.sms.blocker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public final class ConfirmationDialog {

    public interface DialogResultListener {

        void onConfirm();

        void onDismiss();
    }

    public static void show(final String message, final Activity activity, final DialogResultListener resultListener) {
        
        if (null != activity) {
            show(message, activity.getString(android.R.string.dialog_alert_title), activity, resultListener);
        }
        else {
            show(message, "", activity, resultListener);
        }
    }

    public static void show(final String message, final String title, final Activity activity,
        final DialogResultListener resultListener) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (null != resultListener) {
                    resultListener.onConfirm();
                }
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (null != resultListener) {
                    resultListener.onDismiss();
                }
            }
        });
        builder.create().show();
    }
}
