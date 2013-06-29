package org.sms.blocker.service;

import org.sms.blocker.receiver.SmsBroadcastReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class BroadcastListeningService extends Service {

    private static final String TAG = BroadcastListeningService.class.getSimpleName();

    private final SmsBroadcastReceiver smsBroadcastReceiver = new SmsBroadcastReceiver();

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(999);

        registerReceiver(this.smsBroadcastReceiver, filter);

        Log.d(TAG, "Registered SMS broadcast listener");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(this.smsBroadcastReceiver);

        Log.d(TAG, "Unregistered SMS broadcast listener");

        super.onDestroy();
    }

}
