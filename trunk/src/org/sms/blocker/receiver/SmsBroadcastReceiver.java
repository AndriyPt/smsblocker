package org.sms.blocker.receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sms.blocker.constant.GeneralConstants;
import org.sms.blocker.settings.UserSettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String action = intent.getAction();
        if (!action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            return;
        }

        final UserSettings userSettings = new UserSettings(context);

        if (userSettings.isTurnedOn()) {

            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[])pudsBundle.get("pdus");
            SmsMessage message = SmsMessage.createFromPdu((byte[])pdus[0]);
            final List<String> blacklist = userSettings.getBlacklist();

            if (blacklist.contains(message.getDisplayOriginatingAddress())) {

                Log.i(TAG, "Deleting SMS from \"" + message.getDisplayOriginatingAddress()
                    + "\" with following text \"" + message.getMessageBody() + "\"");

                String smsText = message.getMessageBody();
                smsText = smsText.replace("\r", "").replace("\n", "");
                final String messageInfo = message.getDisplayOriginatingAddress() + " => " + smsText;

                writeMessageToLogFile(context, messageInfo);

                abortBroadcast();
            }
            else {
                List<String> latestSmsSenders = userSettings.getLastestSmsSenders();
                for (int i = latestSmsSenders.size() - 1; i >= 0; i--) {
                    if (blacklist.contains(latestSmsSenders.get(i))) {
                        latestSmsSenders.remove(i);
                    }
                }

                final int senderIndex = latestSmsSenders.indexOf(message.getDisplayOriginatingAddress());
                if (senderIndex >= 0) {
                    latestSmsSenders.remove(senderIndex);
                    latestSmsSenders.add(0, message.getDisplayOriginatingAddress());
                }
                else {
                    latestSmsSenders.add(0, message.getDisplayOriginatingAddress());
                    while (latestSmsSenders.size() > GeneralConstants.LATEST_SMS_SENDERS_MAX_COUNT) {
                        latestSmsSenders.remove(latestSmsSenders.size() - 1);
                    }
                }
                userSettings.save(latestSmsSenders);
            }
        }
    }

    private void writeMessageToLogFile(final Context context, final String messageInfo) {

        final File file = new File(context.getFilesDir(), GeneralConstants.DELETED_SMS_LOG_FILE_NAME);

        final List<String> fileContent = new ArrayList<String>();
        if (file.exists()) {
            BufferedReader inputStream = null;
            try {
                inputStream = new BufferedReader(new FileReader(file));
                String line = null;
                while (null != (line = inputStream.readLine())) {
                    fileContent.add(line);
                }
                inputStream.close();
            }
            catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
            catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        fileContent.add(messageInfo);

        while (fileContent.size() > GeneralConstants.LOG_FILE_MAX_LINES_COUNT) {
            fileContent.remove(0);
        }

        final BufferedWriter outputStream;

        try {
            outputStream = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < fileContent.size(); i++) {
                outputStream.write(fileContent.get(i));
                outputStream.newLine();
            }
            outputStream.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
