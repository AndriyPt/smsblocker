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
    private static final String SETTINGS_BLACKLIST = "BlackList";
    private static final String SETTINGS_LATEST_SMS_SENDERS = "LatestSmsSenders";
    private static final String SETTINGS_STRINGS_SEPARATOR = "|";

    private SharedPreferences preferences;

    public UserSettings(final Context context) {
        Log.d(TAG, "Loading user settings...");
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public boolean isTurnedOn() {
        return preferences.getBoolean(SETTINGS_TURNED_ON, true);
    }

    private List<String> getStringListPropertyValue(final String settingName) {
        final String strings = preferences.getString(settingName, "");
        List<String> result;
        if ((null == strings) || (0 == strings.trim().length())) {
            result = new ArrayList<String>();
        }
        else {
            final String[] stringsArray = strings.split(Pattern.quote(SETTINGS_STRINGS_SEPARATOR));
            result = new ArrayList<String>(Arrays.asList(stringsArray));
        }
        return result;
    }

    private String saveStringListPropertyToString(final List<String> value) {

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            if (i > 0) {
                result.append(SETTINGS_STRINGS_SEPARATOR);
            }
            result.append(value.get(i));
        }
        return result.toString();
    }

    public List<String> getBlacklist() {
        return getStringListPropertyValue(SETTINGS_BLACKLIST);
    }

    public List<String> getLastestSmsSenders() {
        return getStringListPropertyValue(SETTINGS_LATEST_SMS_SENDERS);
    }

    public void save(final boolean isTurnedOn, final List<String> blacklist) {

        Log.d(TAG, "Saving user settings...");
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SETTINGS_TURNED_ON, isTurnedOn);

        editor.putString(SETTINGS_BLACKLIST, saveStringListPropertyToString(blacklist));
        editor.commit();
        Log.d(TAG, "Saved user settings");
    }

    public void save(final List<String> latestSmsSenders) {

        Log.d(TAG, "Saving latest SMS senders user settings...");
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SETTINGS_LATEST_SMS_SENDERS, saveStringListPropertyToString(latestSmsSenders));
        editor.commit();
        Log.d(TAG, "Saved latest SMS senders user settings");
    }
}
