package ir.dimyadi.persiancalendar.view.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.R;

import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.praytime.praytimes.PrayTime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AthanActivity extends AppCompatActivity implements  View.OnClickListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = AthanActivity.class.getName();
    private TextView textAlarmName, textTaghib, titleTaghib;
    private AppCompatImageView athanIconView;
    private MediaPlayer mediaPlayer;
    private Utils utils;

    //Silent if Ringing Device
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                stop();
                finish();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                play();
            }
        }
    };

    //Silent if Ringing Device
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                mOnAudioFocusChangeListener.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String prayerKey = getIntent().getStringExtra(Constants.KEY_EXTRA_PRAYER_KEY);
        utils = Utils.getInstance(getApplicationContext());

        utils.changeAppLanguage(this);
        utils.loadLanguageResource();

        setContentView(R.layout.activity_athan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textAlarmName = (TextView) findViewById(R.id.athan_name);
        titleTaghib = (TextView) findViewById(R.id.title_taghib);
        Typeface type1 = Typeface.createFromAsset(getAssets(), Constants.FONT_TAGHIBAT);
        titleTaghib.setTypeface(type1);
        titleTaghib.setOnClickListener(null);
        textTaghib = (TextView) findViewById(R.id.athan_taghib);
        Typeface type2 = Typeface.createFromAsset(getAssets(), Constants.FONT_TAGHIBAT);
        textTaghib.setTypeface(type2);
        textTaghib.setOnClickListener(null);
        textTaghib.setMovementMethod(new ScrollingMovementMethod());
        TextView textCityName = (TextView) findViewById(R.id.place);
        athanIconView = (AppCompatImageView) findViewById(R.id.background_image);
        athanIconView.setOnClickListener(this);
        FloatingActionButton athanCancelView = (FloatingActionButton) findViewById(R.id.athan_cancel);
        athanCancelView.setOnClickListener(this);

        setPrayerView(prayerKey);

        play();

        textCityName.setText(getString(R.string.in_city_time) + " " + utils.getCityName(true));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, TimeUnit.SECONDS.toMillis(257));

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void setPrayerView(String key) {
        if (!TextUtils.isEmpty(key)) {
            PrayTime prayTime = PrayTime.valueOf(key);

            switch (prayTime) {
                case FAJR:
                    titleTaghib.setText(getString(R.string.t_sobh));
                    textTaghib.setText(getString(R.string.tfajr));
                    textAlarmName.setText(getString(R.string.azan1));
                    athanIconView.setImageResource(R.drawable.fajr);
                    break;

                case DHUHR:
                    titleTaghib.setText(getString(R.string.t_zohr));
                    textTaghib.setText(getString(R.string.tdhuhr));
                    textAlarmName.setText(getString(R.string.azan2));
                    athanIconView.setImageResource(R.drawable.zohr);
                    break;

                case ASR:
                    titleTaghib.setText(getString(R.string.t_asr));
                    textTaghib.setText(getString(R.string.tasr));
                    textAlarmName.setText(getString(R.string.azan3));
                    athanIconView.setImageResource(R.drawable.asr);
                    break;

                case MAGHRIB:
                    titleTaghib.setText(getString(R.string.t_maghreb));
                    textTaghib.setText(getString(R.string.tmaghrib));
                    textAlarmName.setText(getString(R.string.azan4));
                    athanIconView.setImageResource(R.drawable.maghrib);
                    break;

                case ISHA:
                    titleTaghib.setText(getString(R.string.t_esha));
                    textTaghib.setText(getString(R.string.tisha));
                    textAlarmName.setText(getString(R.string.azan5));
                    athanIconView.setImageResource(R.drawable.isha);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        stop();
        finish();
    }

    @Override
    public void onBackPressed() {
        //Do nothing since we want to force the user
        //to click the alarm button.
    }

    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
    }

    //Stop Athan if press volume keys
    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)
    //    {
    //        stop();
    //    }
    //    if(keyCode== KeyEvent.KEYCODE_VOLUME_DOWN)
    //    {
    //        stop();
    //    }
    //    return false;
    //}

    private void play() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setDataSource(this, utils.getAthanUri());
            //Volume Type Alarm For Fix Problem in older Version With Media Volume.
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.prepare();
            mediaPlayer.start();
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, utils.getAthanVolume(), 0);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void stop() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (IllegalStateException ignored) {
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
    }
}
