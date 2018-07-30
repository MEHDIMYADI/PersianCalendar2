package ir.dimyadi.persiancalendar.view.testament;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import ir.dimyadi.persiancalendar.util.Utils;


public class TestamentSmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils utils = Utils.getInstance(context);
        final Bundle bundle = intent.getExtras();

        //use sms code
        if(utils.isTestament()) {
            try {
            if (bundle != null) {
                final Object[] pdus = (Object[]) bundle.get("pdus");

                assert pdus != null;
                for (Object pdu : pdus) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String message = currentMessage.getDisplayMessageBody();

                    String messageCode = utils.isTestamentCode();
                    if (message.trim().equalsIgnoreCase(messageCode)) {
                        Intent startIntent = new Intent(context, TestamentSOSActivity.class);
                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(startIntent);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TestamentSmsReceiver", "Exception smsReceiver" + e);
        }
    }
  }
}