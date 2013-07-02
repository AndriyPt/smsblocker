package org.sms.blocker.receiver;

import org.sms.blocker.service.BroadcastListeningService;
import org.sms.blocker.settings.UserSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteBroadcastReceiver extends BroadcastReceiver {
    
    private static final String TAG = BootCompleteBroadcastReceiver.class.getSimpleName();
    
    private void startServiceIfNeeded(final Context context) {
        final UserSettings userSettings = new UserSettings(context);
        if (userSettings.isTurnedOn()) {
            Log.d(TAG, "Starting service...");
            final Intent startServiceIntent = new Intent(context, BroadcastListeningService.class);
            context.startService(startServiceIntent);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if ((null != intent) && (null != intent.getAction())) {

            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Log.d(TAG, "Processing BOOT_COMPLETED event...");
                startServiceIfNeeded(context);
            }
            else if (intent.getAction().equals("android.intent.action.PACKAGE_RESTARTED")) {
                if ((null != intent.getDataString()) && (intent.getDataString().contains("org.sms.blocker"))) {
                    Log.d(TAG, "Processing PACKAGE_RESTARTED event for current package...");
                    startServiceIfNeeded(context);
                }
            }
        }
    }

}
