package org.sms.blocker.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserSettings {

    private static final String TAG = UserSettings.class.getSimpleName();

    private static final String PREFERENCES_NAME = "SimpleSMSBlockerPreferencesFile";

    private static final String SETTINGS_TURNED_ON = "TurnedOn";
    private static final String SETTINGS_PHONES_LIST = "PhonesList";
    private static final String SETTINGS_PHONES_SEPARATOR = "|";

    private SharedPreferences preferences;

    public UserSettings(final Context context) {
        Log.i(TAG, "Loading user settings...");
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isTurnedOn() {
        return preferences.getBoolean(SETTINGS_TURNED_ON, true);
    }

    public List<String> getBlacklist() {
        final String phones = preferences.getString(SETTINGS_PHONES_LIST, "");
        final String[] phonesArray = phones.split(Pattern.quote(SETTINGS_PHONES_SEPARATOR));
        return new ArrayList<String>(Arrays.asList(phonesArray));
    }
    
    public void save(final boolean isTurnedOn, final List<String> blacklist) {
        
        Log.v(TAG, "Saving user settings...");
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SETTINGS_TURNED_ON, isTurnedOn);

        final StringBuilder phonesList = new StringBuilder();
        for (int i = 0; i < blacklist.size(); i++) {
            if (i > 0) {
                phonesList.append(SETTINGS_PHONES_SEPARATOR);
            }
            phonesList.append(blacklist.get(i));
        }

        editor.putString(SETTINGS_PHONES_LIST, phonesList.toString());
        editor.commit();
        Log.v(TAG, "Saved user settings");
    }

}
