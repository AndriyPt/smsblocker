package org.sms.blocker.dialog;

import java.util.ArrayList;
import java.util.List;

import org.sms.blocker.R;
import org.sms.blocker.constant.GeneralConstants;
import org.sms.blocker.settings.UserSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AddPhoneFromSmsHistoryDialog {

    public interface DialogResultListener {

        void onSuccess(final String phone);

        void onCancel();
    }

    private static List<String> getSmsSendersFromInbox(final Activity activity) {

        final List<String> result = new ArrayList<String>();

        final Cursor cursor = activity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null,
            null);

        if (cursor.moveToFirst() && !cursor.isNull(cursor.getColumnIndex("date"))
            && !cursor.isNull(cursor.getColumnIndex("address"))) {

            do {
                final String address = cursor.getString(cursor.getColumnIndex("address"));
                if (!result.contains(address)) {
                    result.add(address);
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    private static List<String> getSmsLog(final List<String> blacklist, final Activity activity) {

        final UserSettings userSettings = new UserSettings(activity);
        List<String> result = userSettings.getLastestSmsSenders();

        boolean isInitialization = false;
        if ((null == result) || (0 == result.size())) {

            result = getSmsSendersFromInbox(activity);
            isInitialization = true;
        }

        for (int i = result.size() - 1; i >= 0; i--) {
            if (blacklist.contains(result.get(i))) {
                result.remove(i);
            }
        }

        while (result.size() > GeneralConstants.LATEST_SMS_SENDERS_MAX_COUNT) {
            result.remove(result.size() - 1);
        }

        if (isInitialization) {
            userSettings.save(result);
        }

        return result;
    }

    public static void show(final List<String> blacklist, final Activity activity,
        final DialogResultListener resultListener) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_phone_from_sms_history, null);

        final ListView phonesToBlockListView = (ListView)dialogView.findViewById(R.id.phonesListToBlock);

        final List<String> latestSmsSendersList = getSmsLog(blacklist, activity);

        phonesToBlockListView.setAdapter(new ArrayAdapter<String>(activity,
            android.R.layout.simple_list_item_single_choice, latestSmsSendersList));
        phonesToBlockListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        builder.setTitle(R.string.blacklist);
        builder.setIcon(android.R.drawable.ic_menu_send);

        builder.setView(dialogView);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (null != resultListener) {
                    final int position = phonesToBlockListView.getCheckedItemPosition();
                    if (0 <= position) {
                        resultListener.onSuccess(latestSmsSendersList.get(position));
                    }
                }
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (null != resultListener) {
                    resultListener.onCancel();
                }
            }
        });
        builder.create().show();
    }
}
