package org.sms.blocker;

import java.util.ArrayList;
import java.util.List;

import org.sms.blocker.dialog.AddEditPhoneDialog;
import org.sms.blocker.dialog.Alert;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ApplicationSettings extends Activity {

    private static final String TAG = "ApplicationSettings";

    private ArrayList<String> blacklistItems = new ArrayList<String>();

    private ArrayAdapter<String> blacklistDataAdapter;

    private ListView blackListView;

    private Button addBlacklistItemButton;
    private Button saveSettingsButton;

    private Button findButtom(final int id) {
        return (Button)findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);

        blackListView = (ListView)findViewById(R.id.blackList);
        blackListView.setItemsCanFocus(false);
        blackListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        blacklistDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.blacklistItems);
        blackListView.setAdapter(this.blacklistDataAdapter);
        registerForContextMenu(blackListView);

        addBlacklistItemButton = findButtom(R.id.addPhoneButton);

        saveSettingsButton = findButtom(R.id.saveSettings);

        addBlacklistItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAddBlacklistItemButtonClick();
            }
        });

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveSettingsButtonClicked();
            }
        });
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

        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getTitle().equals(getString(R.string.editPhoneMenuLabel))) {
            onEditBlacklistItemMenuClick(info.position);
        }
        else if (item.getTitle().equals(getString(R.string.deletePhoneMenuLabel))) {
            onDeleteBlacklistItemMenuClick(info.position);
        }

        return true;
    }

    protected void onDeleteBlacklistItemMenuClick(final int listPosition) {
        // TODO Auto-generated method stub

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

    protected void onSaveSettingsButtonClicked() {
        Alert.show(this, "Hello World");
    }
}
