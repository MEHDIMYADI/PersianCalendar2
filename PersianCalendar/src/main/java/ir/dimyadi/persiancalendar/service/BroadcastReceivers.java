package ir.dimyadi.persiancalendar.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.util.UpdateUtils;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.activity.AthanActivity;

public class BroadcastReceivers extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Utils utils = Utils.getInstance(context);
        UpdateUtils updateUtils = UpdateUtils.getInstance(context);

        if (intent != null && intent.getAction() != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                    intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) ||
                    intent.getAction().equals(Constants.BROADCAST_RESTART_APP)) {

                if (!Utils.getInstance(context).isServiceRunning(ApplicationService.class)) {
                //context.startService(new Intent(context, ApplicationService.class));

                //Fix Android 8.0: java.lang.IllegalStateException: Not allowed to start service Intent
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, ApplicationService.class));
                } else {
                    context.startService(new Intent(context, ApplicationService.class));
              }
            }

            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK) ||
                    intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
                    intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

                updateUtils.update(false);

            } else if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED) ||
                    intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {

                updateUtils.update(true);
                utils.loadApp();
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent(Constants.LOCAL_INTENT_DAY_PASSED));

            } else if (intent.getAction().equals(Constants.BROADCAST_ALARM)) {
                startAthanActivity(intent.getStringExtra(Constants.KEY_EXTRA_PRAYER_KEY));
            }
        }
    }

    private void startAthanActivity(String string) {
        Intent intent = new Intent(context, AthanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_EXTRA_PRAYER_KEY, string);
        context.startActivity(intent);
    }
}
