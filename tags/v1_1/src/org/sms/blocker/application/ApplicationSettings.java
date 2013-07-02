package org.sms.blocker.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sms.blocker.R;
import org.sms.blocker.constant.GeneralConstants;
import org.sms.blocker.dialog.AddEditPhoneDialog;
import org.sms.blocker.dialog.AddPhoneFromSmsHistoryDialog;
import org.sms.blocker.dialog.ConfirmationDialog;
import org.sms.blocker.dialog.ShowLogDialog;
import org.sms.blocker.service.BroadcastListeningService;
import org.sms.blocker.settings.UserSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class ApplicationSettings extends Activity {

    private static final String TAG = ApplicationSettings.class.getSimpleName();

    private List<String> blacklistItems = new ArrayList<String>();

    private ArrayAdapter<String> blacklistDataAdapter;

    private ListView blackListView;

    private UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);

        this.userSettings = new UserSettings(this);

        final CheckBox turnOn = (CheckBox)findViewById(R.id.turnOn);
        turnOn.setChecked(this.userSettings.isTurnedOn());

        final CheckBox keepLog = (CheckBox)findViewById(R.id.keepSmsLog);
        keepLog.setChecked(this.userSettings.isKeepSmsLog());

        blacklistItems = this.userSettings.getBlacklist();

        blackListView = (ListView)findViewById(R.id.blackList);

        blacklistDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.blacklistItems);
        blackListView.setAdapter(this.blacklistDataAdapter);
        registerForContextMenu(blackListView);

        updateServiceStatus();
    }

    private void updateServiceStatus() {

        final Intent serviceIntent = new Intent(this, BroadcastListeningService.class);

        if (this.userSettings.isTurnedOn()) {
            this.startService(serviceIntent);
            Log.d(TAG, "Starting service...");
        }
        else {
            this.stopService(serviceIntent);
            Log.d(TAG, "Stopping service...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_application_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.addLatestSmsPhoneToBlacklistMenuItem == item.getItemId()) {
            onAddRecentSmsSenderBlacklistItemButtonClick();
        }
        else if (R.id.addPhoneToBlacklistMenuItem == item.getItemId()) {
            onAddBlacklistItemButtonClick();
        }
        else if (R.id.showDeletedSmsLog == item.getItemId()) {
            onShowDeletedSmsLog();
        }
        else if (R.id.showCleanSmsLog == item.getItemId()) {
            onCleanSmsLog();
        }
        else if (R.id.saveSettingsMenuItem == item.getItemId()) {
            onSaveSettingsMenuClicked();
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.blackList) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(this.blacklistItems.get(info.position));

            menu.add(R.string.editPhoneMenuLabel);
            menu.add(R.string.deletePhoneMenuLabel);
        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getTitle().equals(getString(R.string.editPhoneMenuLabel))) {
            onEditBlacklistItemMenuClick(info.position);
        }
        else if (item.getTitle().equals(getString(R.string.deletePhoneMenuLabel))) {
            onDeleteBlacklistItemMenuClick(info.position);
        }
        else {
            super.onContextItemSelected(item);
        }

        return true;
    }

    private void onDeleteBlacklistItemMenuClick(final int listPosition) {

        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        ConfirmationDialog.show(String.format(getString(R.string.deletePhoneMessage), listItems.get(listPosition)),
            getString(R.string.deletePhoneMenuLabel), this, new ConfirmationDialog.DialogResultListener() {

                @Override
                public void onDismiss() {
                }

                @Override
                public void onConfirm() {
                    listItems.remove(listPosition);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Successfully process delete event for phone");
                }
            });
    }

    private void onEditBlacklistItemMenuClick(final int listPosition) {

        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        AddEditPhoneDialog.editExistingPhone(listItems.get(listPosition), this,
            new AddEditPhoneDialog.DialogResultListener() {

                @Override
                public void onSuccess(final String phone) {

                    if ((null != phone) && (phone.trim().length() > 0)) {
                        listItems.set(listPosition, phone.trim());
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Successfully process edit event for phone");
                    }
                }

                @Override
                public void onCancel() {
                }
            });
    }

    private void onAddRecentSmsSenderBlacklistItemButtonClick() {
        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        AddPhoneFromSmsHistoryDialog.show(this.blacklistItems, this,
            new AddPhoneFromSmsHistoryDialog.DialogResultListener() {

                @Override
                public void onSuccess(final String phone) {

                    if ((null != phone) && (phone.trim().length() > 0)) {
                        listItems.add(0, phone.trim());
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Successfully process add event for phone");
                    }
                }

                @Override
                public void onCancel() {
                }
            });
    }

    private void onAddBlacklistItemButtonClick() {

        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        AddEditPhoneDialog.enterNewPhone(this, new AddEditPhoneDialog.DialogResultListener() {

            @Override
            public void onSuccess(final String phone) {

                if ((null != phone) && (phone.trim().length() > 0)) {
                    listItems.add(0, phone.trim());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Successfully process add event for phone");
                }
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void onShowDeletedSmsLog() {

        ShowLogDialog.show(this);
    }

    private void onCleanSmsLog() {

        final Activity that = this;

        ConfirmationDialog.show(getString(R.string.doYouWantToCleanSmsLog), getString(R.string.cleanSmsLog), this,
            new ConfirmationDialog.DialogResultListener() {

                @Override
                public void onDismiss() {
                }

                @Override
                public void onConfirm() {

                    final File file = new File(getFilesDir(), GeneralConstants.DELETED_SMS_LOG_FILE_NAME);
                    if (file.exists()) {
                        file.delete();
                    }
                    Log.d(TAG, "Successfully delete SMS log");

                    Toast.makeText(that, R.string.toastSmsLogCleaned, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void onSaveSettingsMenuClicked() {

        final CheckBox turnOn = (CheckBox)findViewById(R.id.turnOn);
        final CheckBox keepLog = (CheckBox)findViewById(R.id.keepSmsLog);
        this.userSettings.save(turnOn.isChecked(), keepLog.isChecked(), this.blacklistItems);

        Toast.makeText(this, R.string.toastSettingsSaved, Toast.LENGTH_SHORT).show();

        updateServiceStatus();
    }
}
