package ir.dimyadi.persiancalendar.view.testament;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;

public class TestamentSOSDisplay extends AppCompatActivity {

    TextView note;
    TextView title;
    String ftitle, fnote, position;
    int requestcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testament_note_sos);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        note = (TextView)findViewById(R.id.et_note);
        title = (TextView)findViewById(R.id.et_title);
        note.setMovementMethod(new ScrollingMovementMethod());

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);

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
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                //none action
                                break;
                            case R.id.action_item2:
                                //none action
                                break;
                            case R.id.action_item3:
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}