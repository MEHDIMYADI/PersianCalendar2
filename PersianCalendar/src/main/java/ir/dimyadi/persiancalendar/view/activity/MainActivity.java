package ir.dimyadi.persiancalendar.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.adapter.DrawerAdapter;
import ir.dimyadi.persiancalendar.service.ApplicationService;
import ir.dimyadi.persiancalendar.util.UpdateUtils;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.fragment.AboutFragment;
import ir.dimyadi.persiancalendar.view.fragment.GhazaFragment;
import ir.dimyadi.persiancalendar.view.fragment.HelpFragment;
import ir.dimyadi.persiancalendar.view.fragment.Names;
import ir.dimyadi.persiancalendar.view.fragment.PrayerFragments;
import ir.dimyadi.persiancalendar.view.fragment.TaghibatNamaz;
import ir.dimyadi.persiancalendar.view.fragment.TasbihatFragment;
import ir.dimyadi.persiancalendar.view.fragment.MoslemZekr;
import ir.dimyadi.persiancalendar.view.fragment.ApplicationPreferenceFragment;
import ir.dimyadi.persiancalendar.view.fragment.CalendarFragment;
import ir.dimyadi.persiancalendar.view.fragment.CompassFragment;
import ir.dimyadi.persiancalendar.view.fragment.ConverterFragment;
import ir.dimyadi.persiancalendar.view.fragment.TodayDua;

public class MainActivity extends AppCompatActivity {

    //Whats New
    private static final String PRIVATE_PREF = "ir.dimyadi.persiancalendar.WN";
    private static final String VERSION_KEY = "version_number";

    private final String TAG = MainActivity.class.getName();
    private Utils utils;
    private UpdateUtils updateUtils;

    private DrawerLayout drawerLayout;
    private DrawerAdapter adapter;

    private Class<?>[] fragments = {
            null,
            CalendarFragment.class,
            ConverterFragment.class,
            CompassFragment.class,
            PrayerFragments.class,
            ApplicationPreferenceFragment.class,
            HelpFragment.class,
            AboutFragment.class
    };

    private static final int CALENDAR = 1;
    private static final int CONVERTER = 2;
    private static final int COMPASS = 3;
    private static final int PRAYER = 4;
    private static final int PREFERENCE = 5;
    private static final int HELP = 6;
    private static final int ABOUT = 7;
    private static final int EXIT = 8;

    // Default selected fragment
    private static final int DEFAULT = CALENDAR;

    private int menuPosition = 0; // it should be zero otherwise #selectItem won't be called

    private String lastLocale;
    private String lastTheme;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utils = Utils.getInstance(getApplicationContext());
        utils.setTheme(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        utils.changeAppLanguage(this);
        utils.loadLanguageResource();
        lastLocale = utils.getAppLanguage();
        lastTheme = utils.getTheme();
        updateUtils = UpdateUtils.getInstance(getApplicationContext());

        if (!Utils.getInstance(this).isServiceRunning(ApplicationService.class)) {
            startService(new Intent(getBaseContext(), ApplicationService.class));
        }

        updateUtils.update(true);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else {
            toolbar.setPadding(0, 0, 0, 0);
        }

        RecyclerView navigation = (RecyclerView) findViewById(R.id.navigation_view);
        navigation.setHasFixedSize(true);
        adapter = new DrawerAdapter(this);
        navigation.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        navigation.setLayoutManager(layoutManager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final View appMainView = findViewById(R.id.app_main_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            int slidingDirection = +1;

            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (isRTL())
                        slidingDirection = -1;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                slidingAnimation(drawerView, slideOffset);
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            private void slidingAnimation(View drawerView, float slideOffset) {
                appMainView.setTranslationX(slideOffset * drawerView.getWidth() * slidingDirection);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        selectItem(DEFAULT);

        LocalBroadcastManager.getInstance(this).registerReceiver(dayPassedReceiver,
                new IntentFilter(Constants.LOCAL_INTENT_DAY_PASSED));

        //multiple permission requests if updated from old version without request permission to new version
        checkAndRequestPermissions();

        //Whats New
        WhatsNew();
    }


    //multiple permission requests
    private  boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    //multiple permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "phone & location & storage services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                            showDialogOK(getString(R.string.phone_location_required),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, R.string.toast_go_to_settings, Toast.LENGTH_LONG).show();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, okListener)
                .create()
                .show();
    }

    private void WhatsNew() {
        SharedPreferences sharedPref = getSharedPreferences(PRIVATE_PREF, Context.MODE_PRIVATE);
        int currentVersionNumber = 0;
        int savedVersionNumber = sharedPref.getInt(VERSION_KEY, 0);
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionNumber = pi.versionCode;
        } catch (Exception ignored) {}
        if (currentVersionNumber > savedVersionNumber) {
            //multiple permission requests if updated from old version without request permission to new version
            checkAndRequestPermissions();
            //show whats new change
            showWhatsNewDialog();
            SharedPreferences.Editor editor	= sharedPref.edit();
            editor.putInt(VERSION_KEY, currentVersionNumber);
            editor.apply();
        }
    }

    private void showWhatsNewDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_whatsnew, null);
        AppCompatImageView AppIcon = (AppCompatImageView) view.findViewById(R.id.ic_app);
        AppIcon.setBackgroundResource(R.drawable.ic_launcher);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setTitle(null)
                .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isRTL() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        utils.changeAppLanguage(this);
        View v = findViewById(R.id.drawer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setLayoutDirection(isRTL() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public boolean dayIsPassed = false;

    private BroadcastReceiver dayPassedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dayIsPassed = true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (dayIsPassed) {
            dayIsPassed = false;
            restartActivity();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dayPassedReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        } else if (menuPosition != DEFAULT) {
            selectItem(DEFAULT);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Checking for the "menu" key
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void beforeMenuChange(int position) {
        if (position != menuPosition) {
            // reset app lang on menu changes, ugly hack but it seems is needed
            utils.changeAppLanguage(this);
        }

        // only if we are returning from preferences
        if (menuPosition != PREFERENCE)
            return;

        utils.updateStoredPreference();
        updateUtils.update(true);

        boolean needsActivityRestart = false;

        String locale = utils.getAppLanguage();
        if (!locale.equals(lastLocale)) {
            lastLocale = locale;
            utils.changeAppLanguage(this);
            utils.loadLanguageResource();
            needsActivityRestart = true;
        }

        if (!lastTheme.equals(utils.getTheme())) {
            needsActivityRestart = true;
            lastTheme = utils.getTheme();
        }

        if (needsActivityRestart)
            restartActivity();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void selectItem(int item) {
        if (item == EXIT) {
            finish();
            return;
        }

        beforeMenuChange(item);
        if (menuPosition != item) {
            try {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.fragment_holder,
                                (Fragment) fragments[item].newInstance(),
                                fragments[item].getName()
                        ).commit();
                menuPosition = item;
            } catch (Exception e) {
                Log.e(TAG, item + " is selected as an index", e);
            }
        }

        adapter.setSelectedItem(menuPosition);

        drawerLayout.closeDrawers();
    }

    //@Override
    //public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //    if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE)
    //        LocalBroadcastManager.getInstance(this).sendBroadcast(
    //                new Intent(Constants.LOCATION_PERMISSION_RESULT));
    //}
}
