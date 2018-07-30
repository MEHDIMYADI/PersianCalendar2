package ir.dimyadi.persiancalendar.view.preferences;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;

import ir.dimyadi.persiancalendar.util.Utils;

public class LocationPreference extends DialogPreference {

    public LocationPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Utils.getInstance(getContext()).setFontAndShape(holder);
    }

    void setSelected(String selected) {
        final boolean wasBlocking = shouldDisableDependents();
        persistString(selected);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) notifyDependencyChange(isBlocking);
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(new Intent("update-preference"));
    }
}
