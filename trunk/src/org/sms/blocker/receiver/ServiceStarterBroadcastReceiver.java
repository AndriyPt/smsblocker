package org.sms.blocker.receiver;

import org.sms.blocker.service.BroadcastListeningService;
import org.sms.blocker.settings.UserSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class ServiceStarterBroadcastReceiver extends BroadcastReceiver {
    
    private static final String TAG = ServiceStarterBroadcastReceiver.class.getSimpleName();

    protected void startServiceIfNeeded(final Context context) {
        final UserSettings userSettings = new UserSettings(context);
        if (userSettings.isTurnedOn()) {
            Log.d(TAG, "Starting service...");
            final Intent startServiceIntent = new Intent(context, BroadcastListeningService.class);
            context.startService(startServiceIntent);
        }
    }

    @Override
    public abstract void onReceive(final Context context, final Intent intent);

}
