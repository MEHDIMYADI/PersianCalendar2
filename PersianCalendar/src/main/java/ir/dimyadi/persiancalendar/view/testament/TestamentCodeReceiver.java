package ir.dimyadi.persiancalendar.view.testament;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.dimyadi.persiancalendar.util.Utils;

public class TestamentCodeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils utils = Utils.getInstance(context);

        //use secret code
        if(intent.getAction().equals("android.provider.Telephony.SECRET_CODE") && utils.isTestament()) {
            String uri = intent.getDataString();
            String[] sep = uri.split("://");
            String secretCode = utils.isTestamentCode();
            if (sep[1].equalsIgnoreCase(secretCode)) {
                Intent i = new Intent(context, TestamentSOSActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }
}