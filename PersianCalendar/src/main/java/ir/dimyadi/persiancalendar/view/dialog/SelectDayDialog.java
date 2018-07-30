package ir.dimyadi.persiancalendar.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.adapter.ShapedArrayAdapter;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.fragment.CalendarFragment;

import ir.dimyadi.calendar.CivilDate;
import ir.dimyadi.calendar.DateConverter;
import ir.dimyadi.calendar.IslamicDate;
import ir.dimyadi.calendar.PersianDate;

public class SelectDayDialog extends AppCompatDialogFragment {
    private int startingYearOnYearSpinner = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.selectday_fragment, null);

        final Utils utils = Utils.getInstance(getContext());

        // fill members
        final Spinner calendarTypeSpinner = view.findViewById(R.id.calendarTypeSpinner);
        final Spinner yearSpinner = view.findViewById(R.id.yearSpinner);
        final Spinner monthSpinner = view.findViewById(R.id.monthSpinner);
        final Spinner daySpinner = view.findViewById(R.id.daySpinner);

        utils.setFontAndShape((TextView) view.findViewById(R.id.converterLabelDay));
        utils.setFontAndShape((TextView) view.findViewById(R.id.converterLabelMonth));
        utils.setFontAndShape((TextView) view.findViewById(R.id.converterLabelYear));
        utils.setFontAndShape((TextView) view.findViewById(R.id.calendarTypeTitle));

        startingYearOnYearSpinner = utils.fillYearMonthDaySpinners(getContext(),
                calendarTypeSpinner, yearSpinner, monthSpinner, daySpinner);

        calendarTypeSpinner.setAdapter(new ShapedArrayAdapter<>(getContext(),
                Utils.DROPDOWN_LAYOUT, getResources().getStringArray(R.array.calendar_type)));
        calendarTypeSpinner.setSelection(0);

        calendarTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startingYearOnYearSpinner = utils.fillYearMonthDaySpinners(getContext(),
                        calendarTypeSpinner, yearSpinner, monthSpinner, daySpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCustomTitle(null);
        builder.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int year = startingYearOnYearSpinner + yearSpinner.getSelectedItemPosition();
                int month = monthSpinner.getSelectedItemPosition() + 1;
                int day = daySpinner.getSelectedItemPosition() + 1;

                CalendarFragment calendarFragment = (CalendarFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(CalendarFragment.class.getName());

                try {
                    switch (utils.calendarTypeFromPosition(calendarTypeSpinner.getSelectedItemPosition())) {
                        case GREGORIAN:
                            calendarFragment.bringDate(DateConverter.civilToPersian(
                                    new CivilDate(year, month, day)));
                            break;

                        case ISLAMIC:
                            calendarFragment.bringDate(DateConverter.islamicToPersian(
                                    new IslamicDate(year, month, day)));
                            break;

                        case SHAMSI:
                            calendarFragment.bringDate(new PersianDate(year, month, day));
                            break;
                    }
                } catch (RuntimeException e) {
                    utils.quickToast(getString(R.string.date_exception));
                    Log.e("SelectDayDialog", "", e);
                }
            }
        });

        return builder.create();
    }
}
