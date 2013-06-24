package org.sms.blocker;

import org.sms.blocker.settings.UserSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsFilter extends BroadcastReceiver {

    private static final String TAG = SmsFilter.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final UserSettings userSettings = new UserSettings(context);

        if (userSettings.isTurnedOn()) {

            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[])pudsBundle.get("pdus");
            SmsMessage message = SmsMessage.createFromPdu((byte[])pdus[0]);

            if (userSettings.getBlacklist().contains(message.getDisplayOriginatingAddress())) {
                Log.i(TAG, "Deleting SMS from \"" + message.getDisplayOriginatingAddress()
                    + "\" with following text \"" + message.getMessageBody() + "\"");
                abortBroadcast();
            }
        }
    }
}
