package org.sms.blocker.receiver;

import org.sms.blocker.service.BroadcastListeningService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final Intent startServiceIntent = new Intent(context, BroadcastListeningService.class);
        context.startService(startServiceIntent);
    }

}
