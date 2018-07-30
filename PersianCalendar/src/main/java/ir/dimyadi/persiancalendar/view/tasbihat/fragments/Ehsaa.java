package ir.dimyadi.persiancalendar.view.tasbihat.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.view.tasbihat.CurrentShamsidate;
import ir.dimyadi.persiancalendar.view.tasbihat.db.Contract;
import ir.dimyadi.persiancalendar.view.tasbihat.db.DBHelper;
import ir.dimyadi.persiancalendar.util.Utils;

import java.util.Calendar;

public class Ehsaa extends Fragment {

    CalendarView calendarView;
    Calendar calendar = Calendar.getInstance();
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    SQLiteDatabase db;
    int[] num = new int[]{0, 0, 0, 0, 0};
    int sum = 0;
    TextView shamsi;
    int mDay, mMonth, mYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ehsaa_layout, container, false);
        final Utils utils = Utils.getInstance(getContext());
        shamsi = (TextView) view.findViewById(R.id.shamsi);
        utils.setFont(shamsi);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.FloatingActionGoTo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to return the current day
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                long millitTime = calendar.getTimeInMillis();
                calendarView.setDate(millitTime);
                //show in shamsi date
                CurrentShamsidate ct = new CurrentShamsidate(year,month,day);
                shamsi.setText(utils.formatNumber(ct.getIranianDate()));
            }
        });

        db = new DBHelper(getActivity()).getWritableDatabase();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        //calendarView.setShowWeekNumber(false);
        //calendarView.setFirstDayOfWeek(0);

        //to get the minimum date for the ir.dimyadi.calendar view
        Calendar minCalndarViewLimit = Calendar.getInstance();
        minCalndarViewLimit.set(Calendar.SECOND, 0);
        minCalndarViewLimit.set(Calendar.MINUTE, 0);
        minCalndarViewLimit.set(Calendar.HOUR, 0);
        minCalndarViewLimit.set(Calendar.DAY_OF_MONTH, minCalndarViewLimit.getActualMinimum(Calendar.DAY_OF_MONTH));
        minCalndarViewLimit.set(Calendar.MONTH, minCalndarViewLimit.get(Calendar.MONTH));
        minCalndarViewLimit.set(Calendar.YEAR, minCalndarViewLimit.get(Calendar.YEAR));

        //to get the maximum date for the ir.dimyadi.calendar view
        Calendar maxCalndarViewLimit = Calendar.getInstance();
        maxCalndarViewLimit.set(Calendar.SECOND, 0);
        maxCalndarViewLimit.set(Calendar.MINUTE, 0);
        maxCalndarViewLimit.set(Calendar.HOUR, 0);
        maxCalndarViewLimit.set(Calendar.DAY_OF_MONTH, maxCalndarViewLimit.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxCalndarViewLimit.set(Calendar.MONTH, maxCalndarViewLimit.get(Calendar.MONTH));
        maxCalndarViewLimit.set(Calendar.YEAR, maxCalndarViewLimit.get(Calendar.YEAR));

        //settng them now
        calendarView.setMinDate(minCalndarViewLimit.getTimeInMillis());
        calendarView.setMaxDate(maxCalndarViewLimit.getTimeInMillis());

        //when we change the selected date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                //show in shamsi date
                CurrentShamsidate ct = new CurrentShamsidate(year,month,dayOfMonth);
                shamsi.setText(utils.formatNumber(ct.getIranianDate()));

                mDay = dayOfMonth;
                mMonth = month;
                mYear = year;

                sum = 0;

                //get the whole numbers from db
                Cursor cursor = db.query(Contract.Tasbiha.TABLE_NAME,
                        new String[]{Contract.FREE_TASBIH,
                                Contract.ALLAH_AKBAR,
                                Contract.ALHAMDULELLAH,
                                Contract.SOBHAN_ALLAH,
                                Contract.LA_ELAH_ELLA_ALLAH},
                        Contract.DATE_TASBIH + " = ?",
                        new String[]{dayOfMonth + "/" + (month + 1) + "/" + year},
                        null, null, null);

                if (cursor.moveToFirst()) {
                    do {
                        num[0] = cursor.getInt(cursor.getColumnIndex(Contract.ALLAH_AKBAR));
                        num[1] = cursor.getInt(cursor.getColumnIndex(Contract.ALHAMDULELLAH));
                        num[2] = cursor.getInt(cursor.getColumnIndex(Contract.SOBHAN_ALLAH));
                        num[3] = cursor.getInt(cursor.getColumnIndex(Contract.LA_ELAH_ELLA_ALLAH));
                        num[4] = cursor.getInt(cursor.getColumnIndex(Contract.FREE_TASBIH));
                    } while (cursor.moveToNext());

                    //getting the sum of them
                    for (int i : num) {
                        sum += i;
                    }
                }

                snackbar = Snackbar.make(coordinatorLayout, R.string.done, Snackbar.LENGTH_INDEFINITE)
                        .setAction(String.valueOf(utils.formatNumber(sum)), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                snackbar.show();
                //Toast.makeText(getActivity(),ct.getIranianDate(), Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //to return the current day
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long millitTime = calendar.getTimeInMillis();
        calendarView.setDate(millitTime);
        //show in shamsi date
        CurrentShamsidate ct = new CurrentShamsidate(year,month,day);
        Utils utils = Utils.getInstance(getContext());
        shamsi.setText(utils.formatNumber(ct.getIranianDate()));
    }
}
