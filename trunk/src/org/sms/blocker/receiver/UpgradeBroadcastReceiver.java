package org.sms.blocker.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpgradeBroadcastReceiver extends ServiceStarterBroadcastReceiver {

    private static final String TAG = UpgradeBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if ((null != intent) && (null != intent.getAction())) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
                if ((null != intent.getDataString()) && intent.getDataString().contains("org.sms.blocker")) {
                    
                    Log.d(TAG, "Processing PACKAGE_REPLACED event for current package...");
                    startServiceIfNeeded(context);
                }
            }
        }
    }
}
