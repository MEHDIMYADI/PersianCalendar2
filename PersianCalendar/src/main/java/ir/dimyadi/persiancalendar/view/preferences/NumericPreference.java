package ir.dimyadi.persiancalendar.view.preferences;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;

import ir.dimyadi.persiancalendar.util.Utils;

public class NumericPreference extends EditTextPreference {

    public NumericPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NumericPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NumericPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumericPreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        Utils.getInstance(getContext()).setFontAndShape(holder);
    }

    private String mString;

    // http://stackoverflow.com/a/10848393
    @Override
    public void setText(String text) {
        mString = parseDouble(text);
        persistString(mString != null ? mString : null);
    }

    @Override
    public String getText() {
        return mString != null ? mString : null;
    }

    private String parseDouble(String text) {
        try {
            return String.valueOf(text);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}
