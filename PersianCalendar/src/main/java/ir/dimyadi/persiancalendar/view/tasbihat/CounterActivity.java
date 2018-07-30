package ir.dimyadi.persiancalendar.view.tasbihat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.tasbihat.db.Contract;
import ir.dimyadi.persiancalendar.view.tasbihat.db.DBHelper;

import java.util.Calendar;

public class CounterActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout frameLayout;
    TextView count;
    SQLiteDatabase db;
    int max;
    //to get the coming intent and its data
    //currentTempCount variable >>
    // the exact count to be displayed in the text view
    //and it gets an exact number from the db in starting the app to be displayed,
    // and it will be zero in using the reset menu
    Intent intent;
    int zekr, currentTempCount, restSum;
    String nameInDB;

    //to get the current date for a query
    Calendar calendar = Calendar.getInstance();
    String whereArg = calendar.get(Calendar.DAY_OF_MONTH)
            + "/" + (calendar.get(Calendar.MONTH) + 1)
            + "/" + calendar.get(Calendar.YEAR);

    //the current counter
    // which help me in storing in the db,
    // and it's zero when we start the app and it will be zero after using reset menu
    //and there is no affect on it
    //we just use it for get the current counting since we start the app
    int mCounter;

    //the old count after reset to zero
    int exactNum;

    boolean canBePressed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        final Utils utils = Utils.getInstance(this);

        db = new DBHelper(this).getWritableDatabase();

        //getting the intent and its data
        intent = getIntent();
        zekr = intent.getExtras().getInt("zekr");
        currentTempCount = intent.getExtras().getInt("num");
        restSum = intent.getExtras().getInt("sum");
        nameInDB = intent.getExtras().getString("nameInDB");

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        frameLayout = (FrameLayout) findViewById(R.id.counter);
        count = (TextView) findViewById(R.id.count);
        utils.setFont(count);
        count.setText(utils.formatNumber(String.valueOf(currentTempCount)));

        //to get the previous exact number of the selected item in the db
        String sql = "select * from " + Contract.Tasbiha.TABLE_NAME + " where " + Contract.DATE_TASBIH + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{whereArg});
        if (cursor.moveToFirst()) {
            do {
                switch (nameInDB) {
                    case Contract.ALLAH_AKBAR:
                        exactNum = cursor.getInt(cursor.getColumnIndex(Contract.ALLAH_AKBAR));
                        max=34;
                        Log.d("exactNum", exactNum + "");
                        break;

                    case Contract.ALHAMDULELLAH:
                        exactNum = cursor.getInt(cursor.getColumnIndex(Contract.ALHAMDULELLAH));
                        max=33;
                        Log.d("exactNum", exactNum + "");
                        break;

                    case Contract.SOBHAN_ALLAH:
                        max=33;
                        exactNum = cursor.getInt(cursor.getColumnIndex(Contract.SOBHAN_ALLAH));
                        Log.d("exactNum", exactNum + "");
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        //when pressing the whole layout
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if it's available to press
                if (canBePressed) {
                    mCounter += 1;
                    currentTempCount += 1;

                    count.setText(utils.formatNumber(String.valueOf(currentTempCount)));
                }

                if (currentTempCount == max) {

                    //make it's not available any more to be pressed for the whole layout
                    canBePressed = false;

                    //if it's now 99
                    if ((currentTempCount + restSum) >= 99) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CounterActivity.this, R.style.AlertDialogTheme);
                        builder.setMessage(R.string.la_elah_ella_allah)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //adding one to the previous number in db
                                        ContentValues values = new ContentValues();
                                        values.put(Contract.LA_ELAH_ELLA_ALLAH, (la_elah_ella_allah + 1));
                                        db.update(Contract.Tasbiha.TABLE_NAME, values, Contract.DATE_TASBIH + " = ?", new String[]{whereArg});

                                        finish();
                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    } else {

                        //if it's just 33
                        AlertDialog.Builder builder = new AlertDialog.Builder(CounterActivity.this, R.style.AlertDialogTheme);
                        builder.setMessage(R.string.zekr_alert)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        finish();
                                    }
                                });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        });

    }

    //the previous number from a db
    int la_elah_ella_allah;

    @Override
    protected void onResume() {
        super.onResume();

        //get the previous number from db
        String sql = "select * from " + Contract.Tasbiha.TABLE_NAME + " where " + Contract.DATE_TASBIH + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{whereArg});
        if (cursor.moveToFirst()) {
            la_elah_ella_allah = cursor.getInt(cursor.getColumnIndex(Contract.LA_ELAH_ELLA_ALLAH));
        }
        cursor.close();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //updating the db in leaving the activity
        updateDbInLeaving(nameInDB, currentTempCount, mCounter, exactNum);

    }

    private void updateDbInLeaving(String col, int temp, int c, int n) {
        //for updating the temp db
        ContentValues values = new ContentValues();
        values.put(col, temp);
        db.update(Contract.TempTasbiha.TABLE_NAME, values, Contract.DATE_TASBIH + " = ?", new String[]{whereArg});

        //for updating the original db
        ContentValues values1 = new ContentValues();
        values1.put(col, (c + n));
        db.update(Contract.Tasbiha.TABLE_NAME, values1, Contract.DATE_TASBIH + " = ?", new String[]{whereArg});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_reset, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //finish();
                super.onBackPressed();
                break;
            case R.id.reset:

                //reset the temp db and add the current to the original db
                updateDbByResetMenu(nameInDB, mCounter, exactNum);
                mCounter = 0;
                currentTempCount = 0;
                count.setText(String.valueOf(currentTempCount));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateDbByResetMenu(String col, int c, int n) {
        //for temp db
        ContentValues values = new ContentValues();
        values.put(col, 0);
        db.update(Contract.TempTasbiha.TABLE_NAME, values, Contract.DATE_TASBIH + " = ?", new String[]{whereArg});

        //for original db
        ContentValues values1 = new ContentValues();
        values1.put(col, (c + n));
        db.update(Contract.Tasbiha.TABLE_NAME, values1, Contract.DATE_TASBIH + " = ?", new String[]{whereArg});

        exactNum += c;
    }

}
