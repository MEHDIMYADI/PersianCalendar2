package ir.dimyadi.persiancalendar.view.onefive;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;

public class AddOneFiveFragment extends Fragment {

    EditText title, desc, amount;
    Button add;
    Spinner addcatspinner;
    MyDBHandler dbHandler;
    Button expdatebtn;
    ListView addedlv;
    TextView addedtv;

    Calendar c;
    private static SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_five_add, container, false);

        //initiate all
        title = (EditText) view.findViewById(R.id.exptitle);
        desc = (EditText) view.findViewById(R.id.expdesc);
        amount = (EditText) view.findViewById(R.id.expamount);
        //NEW METHOUD//no use double because it show 2E3 i like show 2000, so long is better
        amount.addTextChangedListener(new NumberTextWatcher(amount));
        add = (Button) view.findViewById(R.id.expadd);
        expdatebtn = (Button) view.findViewById(R.id.expchgdate);
        //addedlv = (ListView) view.findViewById(R.id.expaddeddatalv);
        //addedtv = (TextView) view.findViewById(R.id.expaddeddatatv);


        //initiate date variables
        c = Calendar.getInstance();
        Date date = c.getTime();

        //set initial now value of date
        expdatebtn.setText(sdformat.format(date));

        //date button listener
        //expdatebtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        DialogFragment newFragment = new SelectDateFragment(c, expdatebtn);
        //        newFragment.show(getFragmentManager(), "DatePicker");
        //    }
        //});


        //database handler
        dbHandler = new MyDBHandler(getContext(), null, null, 1);

        //for categories spinner
        addcatspinner = (Spinner) view.findViewById(R.id.addcatspinner);

        //set an adapter for spinner listener
        SimpleCursorAdapter SpinnerAdapter = new SimpleCursorAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                dbHandler.getAllCategories(),
                new String[]{dbHandler.COLUMN_CAT_NAME},
                new int[]{android.R.id.text1},
                0
        );
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addcatspinner.setAdapter(SpinnerAdapter);
        addcatspinner.setSelection(0, true);

        //add button click listener
        add.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean chck = false;

                        String mtitle = title.getText().toString();
                        String mdesc = desc.getText().toString();
                        //NEW METHOUD//no use double because it show 2E3 i like show 2000, so long is better
                        //String mamount = amount.getText().toString();
                        String mamount = amount.getText().toString().replace(",","");
                        int mcat = 0;

                        try {
                            mcat = ((Cursor) addcatspinner.getSelectedItem()).getInt(0);
                        } catch (NullPointerException e) {
                            chck = true;
                        }

                        String mdate = expdatebtn.getText().toString();

                        if (mtitle.isEmpty()) {
                            Toast.makeText(getContext(), getString(R.string.one_five_title_error), Toast.LENGTH_LONG).show();
                        } else if (mamount.isEmpty()) {
                            Toast.makeText(getContext(), getString(R.string.one_five_amount_error), Toast.LENGTH_LONG).show();
                        } else if (mdate.isEmpty()) {
                            Toast.makeText(getContext(), "Please Enter Title", Toast.LENGTH_LONG).show();
                        } else if (chck) {
                            Toast.makeText(getContext(), getString(R.string.one_five_category_error), Toast.LENGTH_LONG).show();
                        } else {
                            //no use double because it show 2E3 i like show 2000, so long is better
                            double famt = Double.parseDouble(mamount);
                            //long famt = Long.parseLong(mamount);


                            Cursor check = dbHandler.addExpense(mtitle, mdesc, "Expense", famt, mcat, mdate);

                            //check if data added ??
                            if (check != null) {
                                Toast.makeText(getContext(), getString(R.string.one_five_added), Toast.LENGTH_LONG).show();
                                title.setText("");
                                desc.setText("");
                                amount.setText("");

                                //added data show
                                //addedtv.setText(getString(R.string.one_five_last_data_added_title));

                                //CustomSimpleCursorAdapter csca = new CustomSimpleCursorAdapter(getContext(),
                                //        R.layout.one_five_listview_item,
                                //        check,
                                //        new String[]{dbHandler.COLUMN_TITLE, "" + dbHandler.COLUMN_AMOUNT, dbHandler.COLUMN_DESC, dbHandler.COLUMN_CAT_NAME},
                                //        new int[]{R.id.itemtitle, R.id.itemamount, R.id.itemdesc, R.id.itemcategory},
                                //        0);

                                //addedlv.setAdapter(csca);

                            } else {
                                Toast.makeText(getContext(), getString(R.string.one_five_add_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );

        //Error in long cant exceeded number more than 50 number
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 12) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.number_limited))
                            .setMessage(getString(R.string.number_limited_message_one) + " " + "12" + " " + getString(R.string.number_limited_message_two))
                            .setPositiveButton(getString(R.string.ok), null)
                            .show();
                }
            }
        });

        return view;
    }

    public void refreshApi(){
        //write the code here to refresh your Api
        //database handler
        dbHandler = new MyDBHandler(getContext(), null, null, 1);
        //set an adapter for spinner listener
        SimpleCursorAdapter SpinnerAdapter = new SimpleCursorAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                dbHandler.getAllCategories(),
                new String[]{dbHandler.COLUMN_CAT_NAME},
                new int[]{android.R.id.text1},
                0
        );
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addcatspinner.setAdapter(SpinnerAdapter);
        addcatspinner.setSelection(0, true);
    }

    //date fragment class..!!
    //public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //    Calendar cal;
    //    Button btn;

    //    public SelectDateFragment(Calendar c, Button b) {
    //        cal = c;
    //        btn = b;
    //    }

    //    @NonNull
    //    @Override
    //    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    //        Calendar cal = Calendar.getInstance();
    //        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
    //        return datePickerDialog;
    //    }

    //    @Override
    //    public void onDateSet(DatePicker view, int syear, int smonth, int sdayOfMonth) {
    //        cal.set(syear, smonth, sdayOfMonth);
    //        btn.setText(sdformat.format(cal.getTime()));
    //    }
    //}


}