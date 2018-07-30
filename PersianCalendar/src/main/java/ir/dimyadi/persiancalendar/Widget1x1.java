package ir.dimyadi.persiancalendar;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import ir.dimyadi.persiancalendar.service.ApplicationService;
import ir.dimyadi.persiancalendar.util.UpdateUtils;
import ir.dimyadi.persiancalendar.util.Utils;

public class Widget1x1 extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Utils.getInstance(context).isServiceRunning(ApplicationService.class)) {
            context.startService(new Intent(context, ApplicationService.class));
        }
        UpdateUtils.getInstance(context).update(true);
    }
}
