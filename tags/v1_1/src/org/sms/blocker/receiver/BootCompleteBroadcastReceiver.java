package org.sms.blocker.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteBroadcastReceiver extends ServiceStarterBroadcastReceiver {

    private static final String TAG = BootCompleteBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d(TAG, "Processing BOOT_COMPLETED event...");
        startServiceIfNeeded(context);
    }

}
