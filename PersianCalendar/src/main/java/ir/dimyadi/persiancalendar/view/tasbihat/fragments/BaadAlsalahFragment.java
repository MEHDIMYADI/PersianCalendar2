package ir.dimyadi.persiancalendar.view.tasbihat.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.view.tasbihat.adapters.BaadAlsalahAdapter;
import ir.dimyadi.persiancalendar.view.tasbihat.db.Contract;
import ir.dimyadi.persiancalendar.view.tasbihat.db.DBHelper;

import java.util.Calendar;

public class BaadAlsalahFragment extends Fragment {

    RecyclerView recyclerView;
    String[] azkar_list = new String[3];
    int[] num_list = new int[]{0, 0, 0};
    SQLiteDatabase db;
    Calendar calendar = Calendar.getInstance();
    String whereArg = calendar.get(Calendar.DAY_OF_MONTH)
            + "/" + (calendar.get(Calendar.MONTH) + 1)
            + "/" + calendar.get(Calendar.YEAR);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_baad_alsalah, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.FloatingActionReset);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to reset the current list and the temp db
                recyclerView.setAdapter(new BaadAlsalahAdapter(getActivity(), azkar_list, new int[]{0, 0, 0}));
                ContentValues values = new ContentValues();
                values.put(Contract.ALLAH_AKBAR, 0);
                values.put(Contract.ALHAMDULELLAH, 0);
                values.put(Contract.SOBHAN_ALLAH, 0);
                values.put(Contract.LA_ELAH_ELLA_ALLAH, 0);
                db.update(Contract.TempTasbiha.TABLE_NAME, values, Contract.DATE_TASBIH + " = ?", new String[] {whereArg});
            }
        });

        azkar_list = getActivity().getResources().getStringArray(R.array.azkar_list);
        //num_list = getActivity().getResources().getIntArray(R.array.num_list);

        recyclerView = (RecyclerView) v.findViewById(R.id.baad_alsalah_list);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        db = new DBHelper(getActivity()).getWritableDatabase();

        //to check if the current day in the db ot not
        Cursor cursor = db.query(Contract.TempTasbiha.TABLE_NAME,
                new String[]{Contract.FREE_TASBIH,
                        Contract.ALLAH_AKBAR,
                        Contract.ALHAMDULELLAH,
                        Contract.SOBHAN_ALLAH,
                        Contract.DATE_TASBIH
                },
                Contract.DATE_TASBIH + " = ?",
                new String[]{whereArg},
                null, null, null);

        //if so
        if (cursor.moveToFirst()) {
            do {
                //get its number to display them
                num_list[0] = cursor.getInt(cursor.getColumnIndex(Contract.ALLAH_AKBAR));
                num_list[1] = cursor.getInt(cursor.getColumnIndex(Contract.ALHAMDULELLAH));
                num_list[2] = cursor.getInt(cursor.getColumnIndex(Contract.SOBHAN_ALLAH));
            } while (cursor.moveToNext());
        } else {

            //if not, create a raw with zero values for each raw
            ContentValues values = new ContentValues();
            values.put(Contract.FREE_TASBIH, 0);
            values.put(Contract.ALLAH_AKBAR, 0);
            values.put(Contract.ALHAMDULELLAH, 0);
            values.put(Contract.SOBHAN_ALLAH, 0);
            values.put(Contract.LA_ELAH_ELLA_ALLAH, 0);
            values.put(Contract.DATE_TASBIH, whereArg);
            db.insert(Contract.TempTasbiha.TABLE_NAME, null, values);
            db.insert(Contract.Tasbiha.TABLE_NAME, null, values);
        }

        cursor.close();

        //make a list with these numbers
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BaadAlsalahAdapter(getActivity(), azkar_list, num_list));
    }
}
