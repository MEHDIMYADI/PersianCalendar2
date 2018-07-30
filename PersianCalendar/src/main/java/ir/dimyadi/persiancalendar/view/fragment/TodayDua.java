package ir.dimyadi.persiancalendar.view.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;

public class TodayDua extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todaydua, container, false);
        final Utils utils = Utils.getInstance(getContext());

        //title day of week name
        String weekDay = "";
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek3);
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek4);
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek5);
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek6);
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek7);
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek1);
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = getResources().getString(R.string.dayofweek2);
        }
        System.out.println(weekDay);

        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.todaydua) + " " + weekDay, "");

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_TODAYDUA);

        TextView god = (TextView) view.findViewById(R.id.inthenameofgod);
        god.setTypeface(type);

        TextView txt = (TextView) view.findViewById(R.id.TextWeekDayDua);
        txt.setTypeface(type);

        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SATURDAY:
                txt.setText(R.string.td1);
                break;
            case Calendar.SUNDAY:
                txt.setText(R.string.td2);
                break;
            case Calendar.MONDAY:
                txt.setText(R.string.td3);
                break;
            case Calendar.TUESDAY:
                txt.setText(R.string.td4);
                break;
            case Calendar.WEDNESDAY:
                txt.setText(R.string.td5);
                break;
            case Calendar.THURSDAY:
                txt.setText(R.string.td6);
                break;
            case Calendar.FRIDAY:
                txt.setText(R.string.td7);
                break;
            default:
                break;
        }

        return view;
    }
}
