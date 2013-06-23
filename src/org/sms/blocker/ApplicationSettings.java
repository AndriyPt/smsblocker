package org.sms.blocker;

import java.util.ArrayList;
import java.util.List;

import org.sms.blocker.dialog.AddEditPhoneDialog;
import org.sms.blocker.dialog.Alert;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;

public class ApplicationSettings extends Activity {

    private static final String TAG = "ApplicationSettings";

    private ArrayList<String> blacklistItems = new ArrayList<String>();

    private ArrayAdapter<String> blacklistDataAdapter;

    private ListView blackListView;

    private Button addBlacklistItemButton;
    private Button editBlacklistItemButton;
    private Button deleteBlacklistItemButton;
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

        blacklistDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
            this.blacklistItems);
        blackListView.setAdapter(this.blacklistDataAdapter);

        addBlacklistItemButton = findButtom(R.id.addPhoneButton);
        editBlacklistItemButton = findButtom(R.id.editPhoneButton);
        deleteBlacklistItemButton = findButtom(R.id.deletePhoneButton);

        saveSettingsButton = findButtom(R.id.saveSettings);

        addBlacklistItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAddBlacklistItemButtonClick();
            }
        });

        editBlacklistItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onEditBlacklistItemButtonClick();
            }
        });

        deleteBlacklistItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onDeleteBlacklistItemButtonClick();
            }
        });

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveSettingsButtonClicked();
            }
        });
    }

    protected void onDeleteBlacklistItemButtonClick() {
        // TODO Auto-generated method stub

    }

    protected void onEditBlacklistItemButtonClick() {

        final List<String> listItems = this.blacklistItems;
        final ArrayAdapter<String> adapter = this.blacklistDataAdapter;

        final int position = 0; // TODO: Determine position

        AddEditPhoneDialog.editExistingPhone(listItems.get(position), this,
            new AddEditPhoneDialog.DialogResultListener() {

                @Override
                public void onSuccess(final String phone) {

                    listItems.set(position, phone);
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
