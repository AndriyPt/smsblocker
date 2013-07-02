package org.sms.blocker.dialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sms.blocker.R;
import org.sms.blocker.constant.GeneralConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public final class ShowLogDialog {

    private static final String TAG = ShowLogDialog.class.getSimpleName();

    private static List<String> getDeleteSmsLog(final Activity activity) {

        final List<String> result = new ArrayList<String>();

        final File file = new File(activity.getFilesDir(), GeneralConstants.DELETED_SMS_LOG_FILE_NAME);

        if (file.exists()) {
            BufferedReader inputStream = null;
            try {
                inputStream = new BufferedReader(new FileReader(file));
                String line = null;
                while (null != (line = inputStream.readLine())) {
                    result.add(0, line);
                }
                inputStream.close();
            }
            catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
            catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return result;
    }

    public static void show(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_show_log, null);

        final ListView deleteSmsListView = (ListView)dialogView.findViewById(R.id.deleteSmsLog);

        final List<String> deletedSmsList = getDeleteSmsLog(activity);

        deleteSmsListView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1,
            deletedSmsList));

        builder.setTitle(R.string.deletedSmsLog);
        builder.setIcon(android.R.drawable.ic_menu_send);

        builder.setView(dialogView);

        builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
}
