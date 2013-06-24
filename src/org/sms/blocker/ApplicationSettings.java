package org.sms.blocker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.sms.blocker.dialog.AddEditPhoneDialog;
import org.sms.blocker.dialog.ConfirmationDialog;

import android.app.Activity;
import android.content.SharedPreferences;
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

public class ApplicationSettings extends Activity {

    private static final String TAG = ApplicationSettings.class.getSimpleName();

    private static final String PREFERENCES_NAME = "SimpleSMSBlockerPreferencesFile";

    private static final String SETTINGS_TURNED_ON = "TurnedOn";
    private static final String SETTINGS_PHONES_LIST = "PhonesList";
    private static final String SETTINGS_PHONES_SEPARATOR = "|";

    private List<String> blacklistItems = new ArrayList<String>();

    private ArrayAdapter<String> blacklistDataAdapter;

    private ListView blackListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);

        Log.v(TAG, "Loading user settings...");
        final SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        final CheckBox turnOn = (CheckBox)findViewById(R.id.turnOn);
        turnOn.setChecked(preferences.getBoolean(SETTINGS_TURNED_ON, true));

        final String phones = preferences.getString(SETTINGS_PHONES_LIST, "");
        final String[] phonesArray = phones.split(Pattern.quote(SETTINGS_PHONES_SEPARATOR));
        blacklistItems = new ArrayList<String>(Arrays.asList(phonesArray));

        blackListView = (ListView)findViewById(R.id.blackList);

        blacklistDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.blacklistItems);
        blackListView.setAdapter(this.blacklistDataAdapter);
        registerForContextMenu(blackListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_application_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.addPhoneToBlacklistMenuItem == item.getItemId()) {
            onAddBlacklistItemButtonClick();
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

    protected void onDeleteBlacklistItemMenuClick(final int listPosition) {

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
                    Log.v(TAG, "Successfully process delete event for phone");
                }
            });

    }

    protected void onEditBlacklistItemMenuClick(final int listPosition) {

        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        AddEditPhoneDialog.editExistingPhone(listItems.get(listPosition), this,
            new AddEditPhoneDialog.DialogResultListener() {

                @Override
                public void onSuccess(final String phone) {

                    listItems.set(listPosition, phone);
                    adapter.notifyDataSetChanged();
                    Log.v(TAG, "Successfully process edit event for phone");
                }

                @Override
                public void onCancel() {
                }
            });
    }

    protected void onAddBlacklistItemButtonClick() {

        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        AddEditPhoneDialog.enterNewPhone(this, new AddEditPhoneDialog.DialogResultListener() {

            @Override
            public void onSuccess(final String phone) {

                listItems.add(phone);
                adapter.notifyDataSetChanged();
                Log.v(TAG, "Successfully process add event for phone");
            }

            @Override
            public void onCancel() {
            }
        });
    }

    protected void onSaveSettingsMenuClicked() {

        Log.v(TAG, "Saving user settings...");
        final SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        final CheckBox turnOn = (CheckBox)findViewById(R.id.turnOn);
        editor.putBoolean(SETTINGS_TURNED_ON, turnOn.isChecked());

        final StringBuilder phonesList = new StringBuilder();
        for (int i = 0; i < this.blacklistItems.size(); i++) {
            if (i > 0) {
                phonesList.append(SETTINGS_PHONES_SEPARATOR);
            }
            phonesList.append(this.blacklistItems.get(i));
        }

        editor.putString(SETTINGS_PHONES_LIST, phonesList.toString());
        editor.commit();
        Log.v(TAG, "Saved user settings");
    }
}
