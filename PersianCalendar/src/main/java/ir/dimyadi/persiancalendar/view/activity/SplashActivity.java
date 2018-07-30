package ir.dimyadi.persiancalendar.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.json.JSONParser;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.splashutils.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.widget.Toast;

import static ir.dimyadi.persiancalendar.Constants.JSON_PARSER_URL;

public class SplashActivity extends Activity {

    //Splash Length
    int SPLASH_DISPLAY_LENGTH = 1000;
    //JSON Node Names
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_SPLASH = "splash";
    private static final String TAG_ISLAMIC_OFFSET = "IslamicOffset";
    private static final String TAG_IMAGE_URL = "ImageURL";
    private Handler mHandler = new Handler();

    JSONArray settings = null;
    JSONArray splash = null;
    public Context context;

    AlphaAnimation inAnimation;
    RelativeLayout progressBarHolder;
    ProgressBar progressBarImageView;
    TextView progressBarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utils utils = Utils.getInstance(this);

        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake_spalsh_icon);
        AppCompatImageView SplashAppIcon = (AppCompatImageView) findViewById(R.id.iv_imageview);
        SplashAppIcon.setBackgroundResource(R.drawable.ic_launcher);
        SplashAppIcon.startAnimation(animShake);

        TextView appName= (TextView) findViewById(R.id.iv_appname);
        utils.setFont(appName);
        appName.setText(utils.formatNumber(getString(R.string.app_name)));

        TextView copyright= (TextView) findViewById(R.id.iv_copyright);
        utils.setFont(copyright);
        copyright.setText(utils.formatNumber(getString(R.string.copyright)));

        progressBarHolder = (RelativeLayout) findViewById(R.id.progressBarHolder);
        progressBarImageView = (ProgressBar) findViewById(R.id.progressBarImageView);
        progressBarTextView = (TextView) findViewById(R.id.progressBarTextView);

        Service();

    }

    private void Service() {
        progressBarTextView.setVisibility(View.GONE);
        progressBarHolder.setVisibility(View.GONE);
        progressBarImageView.setVisibility(View.GONE);
        //Update if Internet Connected and if Enabled From Settings
        if(isConnectedToInternet()) {
            //Run AsyncTask
            //FIX Islamic Calendar and etc, JSON Receive From Server.
            new JSONParse().execute();
            progressBarImageView.setVisibility(View.VISIBLE);
        } else {
            // Start Delay Main Activity
            startDelayActivity();
        }
    }

    //FIX Islamic Calendar and etc, JSON Receive From Server.
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        Utils utils = Utils.getInstance(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            if(utils.isUpDate()) {
                progressBarTextView.setVisibility(View.VISIBLE);
                progressBarHolder.setVisibility(View.VISIBLE);
            } else {
                progressBarTextView.setVisibility(View.GONE);
                progressBarHolder.setVisibility(View.GONE);
            }
        }

        //Read JSON Data
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            return jParser.getJSONFromUrl(JSON_PARSER_URL);
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                //if update enabled in setting by user
                if(utils.isUpDate()) {
                    // Getting JSON Array
                    settings = json.getJSONArray(TAG_SETTINGS);
                    JSONObject setting = settings.getJSONObject(0);
                    // Storing  JSON item in a Variable
                    String IslamicOffset = setting.getString(TAG_ISLAMIC_OFFSET);
                    //Set JSON Data in SharedPreferences
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("islamicOffset", IslamicOffset);
                    editor.apply(); }
                    // Getting JSON Array
                    splash = json.getJSONArray(TAG_SPLASH);
                    JSONObject sp = splash.getJSONObject(0);
                    // Storing  JSON item in a Variable
                    String ImageURL = sp.getString(TAG_IMAGE_URL);
                    //Loading JSON Data in ImageView
                    ImageView imgView = (ImageView) findViewById(R.id.iv_splash);
                    ImageLoader imgLoader = new ImageLoader(SplashActivity.this);
                    imgLoader.DisplayImage(ImageURL, imgView);
                    //Loading MainActivity Delay
                    mHandler.postDelayed(mStartMainActivity, 80);
            } catch (JSONException | NullPointerException e) {
                //If Error in Server Show Toast and Get to MainActivity
                Toast.makeText(SplashActivity.this,getString(R.string.server_error),
                        Toast.LENGTH_SHORT).show();
                //Loading MainActivity
                startActivity();
                e.printStackTrace();
            }
        }
    }

    private Runnable mStartMainActivity = new Runnable() {
        public void run() {
            startDelayActivity();
        }
    };

    //Check Internet
    public boolean isConnectedToInternet(){
        boolean available = false;
        //Getting the system's connectivity service
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //Getting active network interface  to get the network's status
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;
        //Returning the status of the network
        return available;
    }

    //Start Delay Main Activity
    private void startDelayActivity() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Start Main Activity
                startActivity();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    //Start Main Activity
    private void startActivity() {
        Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    //Disable Back Key
    @Override
    public void onBackPressed() {
    }
}