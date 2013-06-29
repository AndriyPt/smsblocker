package org.sms.blocker.dialog;

import org.sms.blocker.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public final class AddEditPhoneDialog {

    public interface DialogResultListener {

        void onSuccess(final String phone);

        void onCancel();
    }

    public static void enterNewPhone(final Activity activity, final DialogResultListener resultListener) {
        editExistingPhone("", activity, resultListener);
    }

    public static void editExistingPhone(final String phoneNumber, final Activity activity,
        final DialogResultListener resultListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_edit_blacklist_item, null);
        final EditText phoneEditText = (EditText)dialogView.findViewById(R.id.phoneNumber);

        if (null != phoneNumber) {
            phoneEditText.setText(phoneNumber);
        }

        builder.setTitle(R.string.phone);
        builder.setIcon(android.R.drawable.ic_menu_call);
        builder.setView(dialogView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (null != resultListener) {
                    resultListener.onSuccess(phoneEditText.getText().toString());
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (null != resultListener) {
                    resultListener.onCancel();
                }
            }
        });
        builder.create().show();
    }
}
