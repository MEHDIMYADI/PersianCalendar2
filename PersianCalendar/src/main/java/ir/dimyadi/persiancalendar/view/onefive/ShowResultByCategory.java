package ir.dimyadi.persiancalendar.view.onefive;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ir.dimyadi.persiancalendar.R;

public class ShowResultByCategory extends Fragment {

    private MyDBHandler dbHandler;
    private TextView totaltv, totalonefive;

    private Calendar todate, fromdate;
    private static SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat sdrevformat = new SimpleDateFormat("yyyy-MM-dd");

    private SimpleCursorAdapter adapter;
    private int mcategory = -1;

    public ShowResultByCategory() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_five_show_result_by_category, container, false);

        //initialize all
        ListView sumlv = (ListView) view.findViewById(R.id.managecustomlv);
        dbHandler = new MyDBHandler(getContext(),null,null,1);
        Button frombtn = (Button) view.findViewById(R.id.managefrombutton);
        Button tobtn = (Button) view.findViewById(R.id.managetobtn);
        totaltv = (TextView)view.findViewById(R.id.total_capital);
        totalonefive = (TextView)view.findViewById(R.id.capital_one_five);
        Spinner categoryspinner = (Spinner) view.findViewById(R.id.managecatspinner);

        //initalize calender variables
        todate = Calendar.getInstance();
        fromdate = Calendar.getInstance();
        fromdate.add(Calendar.DATE, -30);

        //set button text
        frombtn.setText(sdformat.format(fromdate.getTime()));
        tobtn.setText(sdformat.format(todate.getTime()));

        //set to and from button listeners
        //frombtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        DialogFragment newFragment = new SelectDateFragment(fromdate,frombtn,null,todate);
        //        newFragment.setTargetFragment(ShowResultByCategory.this,4);
        //        newFragment.show(getFragmentManager(), "DatePicker");
        //    }
        //});
        //tobtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        DialogFragment newFragment = new SelectDateFragment(todate,tobtn,fromdate,null);
        //        newFragment.setTargetFragment(ShowResultByCategory.this,4);
        //        newFragment.show(getFragmentManager(), "DatePicker");
        //    }
        //});

        //set an adapter for spinner listener
        Cursor allcats = dbHandler.getAllCategories();
        MatrixCursor extra = new MatrixCursor(new String[]{"_id",dbHandler.COLUMN_CAT_NAME});
        extra.addRow(new String[]{"-1",getString(R.string.all)});
        Cursor[] cursors = {extra,allcats};
        final Cursor extendedcur = new MergeCursor(cursors);

        SimpleCursorAdapter SpinnerAdapter = new SimpleCursorAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                extendedcur,
                new String[]{dbHandler.COLUMN_CAT_NAME},
                new int[]{android.R.id.text1},
                0
        );
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryspinner.setAdapter(SpinnerAdapter);
        categoryspinner.setSelection(0,true);

        //categoryspinner on select listener
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                extendedcur.moveToPosition(position);

                mcategory=extendedcur.getInt(0);

                updatelistandtotal();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //get cursor
        Cursor querycur =changeAdapterCursor();
        //set adapter for list view
        adapter = new CustomSimpleCursorAdapter(getContext(),
                R.layout.one_five_listview_item,
                querycur,
                new String[]{dbHandler.COLUMN_TITLE, ""+dbHandler.COLUMN_AMOUNT,dbHandler.COLUMN_DESC, dbHandler.COLUMN_CAT_NAME},
                new int[]{R.id.itemtitle,R.id.itemamount,R.id.itemdesc, R.id.itemcategory},
                0);
        sumlv.setAdapter(adapter);

        updatelistandtotal();
        registerForContextMenu(sumlv);

        //let item click listener for listview
        sumlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) adapter.getItem(position);
                Intent intent = new Intent(getActivity(),ItemPopupActivity.class);
                intent.putExtra("cursorid",c.getInt(c.getColumnIndex(dbHandler.COLUMN_ID)));
                startActivityForResult(intent,8);
            }
        });



        return view;
    }

    public void refreshApi(){
        //write the code here to refresh your Api
        updatelistandtotal();
    }

    private Cursor changeAdapterCursor() {
        if(mcategory!=-1)
            return dbHandler.getQueryExpense("where (substr(tdate,7,4)||'-'||substr(tdate,4,2)||'-'||substr(tdate,1,2)) between '"+sdrevformat.format(fromdate.getTime())+"' and '"+sdrevformat.format(todate.getTime())+"' and "+dbHandler.COLUMN_CATEGORY+"='"+mcategory+"'");
        else
            return dbHandler.getQueryExpense("where (substr(tdate,7,4)||'-'||substr(tdate,4,2)||'-'||substr(tdate,1,2)) between '"+sdrevformat.format(fromdate.getTime())+"' and '"+sdrevformat.format(todate.getTime())+"'");
    }


    //public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    //    super.onCreateContextMenu(menu, v, menuInfo);

    //    if(v.getId()== sumlv.getId()){
    //        menu.add(0,0,0,getString(R.string.edit));
    //        menu.add(0,1,1,getString(R.string.delete));
    //    }
    //}

    //@Override
    //public boolean onContextItemSelected(MenuItem item) {
    //    AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    //    int position = menuInfo.position;
    //    Cursor c = (Cursor) adapter.getItem(position);

    //    switch (item.getItemId()){
    //        //edit context item
    //        case 0:
    //            Intent intent = new Intent(getActivity(),ItemPopupActivity.class);
    //            intent.putExtra("cursorid",c.getInt(c.getColumnIndex(dbHandler.COLUMN_ID)));
    //            startActivityForResult(intent,8);

    //            break;
    //        //delete context item
    //        case 1:
    //            boolean check = dbHandler.deleteExpense(c.getInt(0));
    //            //check if data deleted ??
    //            if(check){
    //                Toast.makeText(getContext(),getString(R.string.result_one_five_deleted),Toast.LENGTH_LONG).show();
    //            }
    //            else{
    //                Toast.makeText(getContext(),getString(R.string.result_one_five_delete_error),Toast.LENGTH_LONG).show();
    //            }

    //            updatelistandtotal();

    //            break;
    //    }

    //    return true;

    //}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //update listview after update command
        if(requestCode==8){
            boolean check;

            try{
                check = data.getBooleanExtra("done",false);
            }catch (NullPointerException e){
                check = false;
            }

            if(check) {
                updatelistandtotal();
            }
        }
    }

    public void updatelistandtotal(){
        //get cursor
        Cursor querycur =changeAdapterCursor();

        //loop to get total and set value of total tv
        //no use double because it show 2E3 i like show 2000, so long is better
        double sumtotal=0;
        //long sumtotal=0;
        boolean exporinc;

        if(querycur.moveToFirst()) {
            do {
                exporinc = querycur.getString(querycur.getColumnIndex(dbHandler.COLUMN_TYPE)).equals("Expense");

                if(exporinc)
                    //sumtotal -= querycur.getDouble(querycur.getColumnIndex(dbHandler.COLUMN_AMOUNT));
                    //no use double because it show 2E3 i like show 2000, so long is better
                    sumtotal += querycur.getDouble(querycur.getColumnIndex(dbHandler.COLUMN_AMOUNT));
                    //sumtotal += querycur.getLong(querycur.getColumnIndex(dbHandler.COLUMN_AMOUNT));
                else
                    //no use double because it show 2E3 i like show 2000, so long is better
                    sumtotal += querycur.getDouble(querycur.getColumnIndex(dbHandler.COLUMN_AMOUNT));
                    //sumtotal += querycur.getLong(querycur.getColumnIndex(dbHandler.COLUMN_AMOUNT));

            } while (querycur.moveToNext());
        }

        //NEW METHOUD//no use double because it show 2E3 i like show 2000, so long is better
        //String total = getString(R.string.total_capital) + " " + sumtotal + " " + getString(R.string.irr);
        NumberFormat DoubleToDecimal = NumberFormat.getNumberInstance(Locale.ENGLISH);
        String totalResult = DoubleToDecimal.format(sumtotal);
        totaltv.setText(getString(R.string.total_capital) + " " + totalResult + " " + getString(R.string.irr));
        //String onefive = getString(R.string.capital_one_five) + " " + (sumtotal / 5) + " " + getString(R.string.irr);
        String totalOneFive = DoubleToDecimal.format(sumtotal/5);
        totalonefive.setText(getString(R.string.capital_one_five) + " " + totalOneFive + " " + getString(R.string.irr));

        adapter.changeCursor(querycur);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        updatelistandtotal();
    }

    //date fragment class..!!
    //public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        //to update and get value
    //    private Calendar date;
    //    private Button btn;
    //    private Calendar minval, maxval;

    //    public SelectDateFragment(Calendar c, Button b, Calendar min, Calendar max) {
    //        date = c;
    //        btn = b;
    //        minval = min;
    //        maxval = max;
    //    }

    //    @NonNull
    //    @Override
    //    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //        DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(),this,date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
    //        Calendar cal = Calendar.getInstance();

    //        if(maxval==null)
    //            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
    //        else
    //            datePickerDialog.getDatePicker().setMaxDate(maxval.getTimeInMillis());


    //        if(minval != null){
    //            datePickerDialog.getDatePicker().setMinDate(minval.getTimeInMillis());
    //        }

    //        return datePickerDialog;
    //    }

    //    @Override
    //    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    //        //update date
    //        date.set(year,month,dayOfMonth);
    //        //reset button value
    //        btn.setText(sdformat.format(date.getTime()));
    //    }

    //    @Override
    //    public void onDismiss(DialogInterface dialog) {
    //        super.onDismiss(dialog);
    //        ShowResultByCategory activity = (ShowResultByCategory) getTargetFragment();
    //        activity.updatelistandtotal();
    //    }
    //}
}
