package ir.dimyadi.persiancalendar.view.testament;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import ir.dimyadi.persiancalendar.R;

public class DisplayTestament extends AppCompatActivity {

    EditText note;
    EditText title;
    String ftitle, fnote, position;
    int requestcode;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testament_note);

        note = (EditText)findViewById(R.id.et_note);
        title = (EditText) findViewById(R.id.et_title);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout);

        ftitle = getIntent().getStringExtra("TITLE");
        fnote = getIntent().getStringExtra("NOTE");
        if(ftitle.equals("init") && fnote.equals("init")) {
            requestcode =1;
        }
        else {
            position = getIntent().getStringExtra("POSITION");
            title.setText(ftitle);
            note.setText(fnote);
            requestcode = 2;
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        final Intent i = new Intent();
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                String inp_title = title.getText().toString();
                                if(inp_title.equals("")) {
                                    Snackbar snackbar = Snackbar.make(relativeLayout, getString(R.string.please_set_title), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                                else {
                                    i.putExtra("IsDELETED", "n");
                                    i.putExtra("NOTE", note.getText().toString());
                                    i.putExtra("TITLE", inp_title);
                                    if (requestcode == 1) {
                                        setResult(requestcode, i);
                                    } else {
                                        i.putExtra("POSITION", position);
                                        setResult(requestcode, i);
                                    }
                                    //setResult(1,i);
                                    //Toast.makeText(DisplayTestament.this, "Save", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                break;
                            case R.id.action_item2:
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString().trim());
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, title.getText().toString().trim() + "\n" + note.getText().toString().trim());
                                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                                break;
                            case R.id.action_item3:
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayTestament.this);
                                // Setting Dialog Title
                                alertDialog.setTitle("  " + getString(R.string.delete_note));
                                // Setting Dialog Message
                                alertDialog.setMessage(getString(R.string.delete_note_sure));
                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.ic_delete);
                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke YES event
                                        i.putExtra("IsDELETED","y");
                                        if(requestcode == 1){
                                            //Toast.makeText(getApplicationContext(), getString(R.string.note_not_created), Toast.LENGTH_SHORT).show();
                                            setResult(requestcode,i);
                                        }
                                        else {
                                            i.putExtra("POSITION",position);
                                            setResult(requestcode, i);
                                        }
                                        finish();
                                    }
                                });
                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke NO event
                                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });
                                // Showing Alert Message
                                alertDialog.show();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(title.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(relativeLayout, getString(R.string.please_set_title), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else {
            Intent i = new Intent();
            i.putExtra("IsDELETED", "n");
            i.putExtra("NOTE", note.getText().toString());
            i.putExtra("TITLE", title.getText().toString());
            if (requestcode == 1) {
                setResult(requestcode, i);
            } else {
                i.putExtra("POSITION", position);
                setResult(requestcode, i);
            }
            //setResult(1,i);
            //Toast.makeText(DisplayTestament.this, "Save", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}