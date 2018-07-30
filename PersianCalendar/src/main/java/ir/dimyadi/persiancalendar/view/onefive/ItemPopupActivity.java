package ir.dimyadi.persiancalendar.view.onefive;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ir.dimyadi.persiancalendar.R;

public class ItemPopupActivity extends AppCompatActivity {

    private EditText titletv, desctv, amounttv;
    private Spinner catspinner;
    private Button datebtn;
    private MyDBHandler dbHandler;

    private static SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
    private String ctype;
    private int itemid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_five_item_popup);
        dbHandler = new MyDBHandler(getApplicationContext(),null,null,1);

        //set screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.7));

        //get cursor data
        Intent i = getIntent();
        itemid = i.getIntExtra("cursorid",0);

        //get item data from database
        Cursor c = dbHandler.getQueryExpense("where "+dbHandler.COLUMN_ID+"="+itemid);

        //initialize variables
        titletv = (EditText) findViewById(R.id.poptitle);
        desctv = (EditText) findViewById(R.id.popdesc);
        amounttv = (EditText) findViewById(R.id.popamount);
        //NEW METHOUD//no use double because it show 2E3 i like show 2000, so long is better
        amounttv.addTextChangedListener(new NumberTextWatcher(amounttv));
        catspinner = (Spinner) findViewById(R.id.popcategory);
        datebtn = (Button) findViewById(R.id.popdatebtn);
        Button updatebtn = (Button) findViewById(R.id.popupdate);
        Button deletebtn = (Button) findViewById(R.id.popdeletebtn);

        //set values equal to selecte item
        if(c.moveToFirst()) {
            ctype = c.getString(c.getColumnIndex("type"));

            titletv.setText(c.getString(c.getColumnIndex(dbHandler.COLUMN_TITLE)));
            desctv.setText(c.getString(c.getColumnIndex(dbHandler.COLUMN_DESC)));
            //NEW METHOUD//no use double because it show 2E3 i like show 2000, so long is better
            //amounttv.setText(c.getString(c.getColumnIndex(dbHandler.COLUMN_AMOUNT)));
            Float FloatToDouble = Float.parseFloat(c.getString(c.getColumnIndex(dbHandler.COLUMN_AMOUNT)));
            NumberFormat DoubleToDecimal = NumberFormat.getNumberInstance(Locale.ENGLISH);
            String totalResult = DoubleToDecimal.format(FloatToDouble);

            amounttv.setText(totalResult);
            if(ctype.equals("Expense")) {
                //set spinner adapter
                Cursor spinnercursor = dbHandler.getAllCategories();
                SimpleCursorAdapter SpinnerAdapter = new SimpleCursorAdapter(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        spinnercursor,
                        new String[]{dbHandler.COLUMN_CAT_NAME},
                        new int[]{android.R.id.text1},
                        0
                );
                SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                catspinner.setAdapter(SpinnerAdapter);

                //set spinner selected item
                int itemcategory = c.getInt(c.getColumnIndex(dbHandler.COLUMN_CATEGORY));

                int cpos = 0;
                for (int x = 0; x < SpinnerAdapter.getCount(); x++) {
                    spinnercursor.moveToPosition(x);
                    int Temp = spinnercursor.getInt(spinnercursor.getColumnIndex(dbHandler.COLUMN_CAT_NAME));
                    if (Temp == itemcategory) {
                        cpos = x;
                        break;
                    }
                }
                catspinner.setSelection(cpos);
            }
            else{
                catspinner.setVisibility(View.GONE);
                findViewById(R.id.popcategorytv).setVisibility(View.GONE);
            }
        }

        //set date initial value
        Calendar datecal = Calendar.getInstance();
        String itemdatestr="";
        try {
            itemdatestr = c.getString(c.getColumnIndex(dbHandler.COLUMN_DATE));
        }catch (CursorIndexOutOfBoundsException e){
            Toast.makeText(getApplicationContext(),getString(R.string.one_five_capital_edit_update),Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            intent.putExtra("done",true);
            setResult(8,intent);
            finish();
        }

        try {
            datecal.setTime(sdformat.parse(itemdatestr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datebtn.setText(itemdatestr);

        //set date listener
        //datebtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        DialogFragment newFragment = new SelectDateFragment(datecal,datebtn);
        //        newFragment.show(getSupportFragmentManager(), "DatePicker");
        //    }
        //});

        //update button click listener
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mtitle = titletv.getText().toString();
                String mdesc = desctv.getText().toString();
                //NEW METHOUD//no use double because it show 2E3 i like show 2000, so long is better
                //String mamount = amounttv.getText().toString();
                String mamount = amounttv.getText().toString().replace(",","");
                int mcat = -1;
                if(ctype.equals("Expense"))
                    mcat = ((Cursor)catspinner.getSelectedItem()).getInt(0);
                String mdate = datebtn.getText().toString();


                if(mtitle.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.popup_one_five_title_error),Toast.LENGTH_LONG).show();
                }
                else if(mamount.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.popup_one_five_amount_error),Toast.LENGTH_LONG).show();
                }
                else if(mdate.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Title",Toast.LENGTH_LONG).show();
                }
                else{
                    //no use double because it show 2E3 i like show 2000, so long is better
                    double famt = Double.parseDouble(mamount);
                    //long famt = Long.parseLong(mamount);


                    boolean check = dbHandler.updateExpense(itemid,mtitle,mdesc,famt,mcat,mdate);

                    //check if data added ??
                    if(check){
                        Toast.makeText(getApplicationContext(),getString(R.string.popup_one_five_updated),Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getString(R.string.popup_one_five_update_error),Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent();
                    intent.putExtra("done",check);
                    setResult(8,intent);
                    finish();
                }
            }
        });

        //set listener for delete button
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = dbHandler.deleteExpense(itemid);
                //check if data deleted ??
                if(check){
                    Toast.makeText(getApplicationContext(),getString(R.string.popup_one_five_deleted),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),getString(R.string.popup_one_five_delete__error),Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent();
                intent.putExtra("done",check);
                setResult(8,intent);
                finish();
            }
        });

        //Error in long cant exceeded number more than 50 number
        amounttv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 12) {
                    new AlertDialog.Builder(ItemPopupActivity.this)
                            .setTitle(getString(R.string.number_limited))
                            .setMessage(getString(R.string.number_limited_message_one) + " " + "12" + " " + getString(R.string.number_limited_message_two))
                            .setPositiveButton(getString(R.string.ok), null)
                            .show();
                }
            }
        });
    }

    //date fragment class
    //public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    //    Calendar cal;
    //    Button btn;

    //    public SelectDateFragment(Calendar c, Button b) {
    //        cal=c;
    //        btn=b;
    //    }

    //    @NonNull
    //    @Override
    //    public Dialog onCreateDialog(Bundle savedInstanceState) {
    //        DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(),this,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
    //        Calendar cal = Calendar.getInstance();
    //        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
    //        return datePickerDialog;
    //    }

    //    @Override
    //    public void onDateSet(DatePicker view, int syear, int smonth, int sdayOfMonth) {
    //        cal.set(syear,smonth,sdayOfMonth);
    //        btn.setText(sdformat.format(cal.getTime()));
    //    }
    //}
}
