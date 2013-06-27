package org.sms.blocker.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public final class ConfirmationDialog {

    public interface DialogResultListener {

        void onConfirm();

        void onDismiss();
    }

    //TODO: Use android.content.Context instead of activity.
    public static void show(final String message, final Context context, final DialogResultListener resultListener) {
        
        if (null != context) {
            show(message, context.getString(android.R.string.dialog_alert_title), context, resultListener);
        }
        else {
            show(message, "", context, resultListener);
        }
    }

    public static void show(final String message, final String title, final Context context,
        final DialogResultListener resultListener) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
