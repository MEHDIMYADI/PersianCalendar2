package ir.dimyadi.persiancalendar.view.preferences;

import android.os.Build;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.util.Utils;

public class NumericDialog extends EditTextPreferenceDialogFragmentCompat {

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        View dialogMessageView = view.findViewById(android.R.id.message);
        if (dialogMessageView != null && dialogMessageView instanceof TextView) {
            Utils.getInstance(getContext()).setFontAndShape((TextView) dialogMessageView);
        }

        EditText editText = (EditText) view.findViewById(android.R.id.edit);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(10)
        });

        // on platforms supporting direction as LTR direction is more handy on editing numbers
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            editText.setTextDirection(View.TEXT_DIRECTION_LTR);
            editText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }
}
