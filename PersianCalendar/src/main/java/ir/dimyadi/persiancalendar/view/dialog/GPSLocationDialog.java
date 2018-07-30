package ir.dimyadi.persiancalendar.view.dialog;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.praytime.praytimes.Coordinate;

public class GPSLocationDialog extends DialogFragment {

    LocationManager locationManager;
    Context context;
    Utils utils;
    private ProgressDialog mProgressDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        context = getContext();
        utils = Utils.getInstance(context);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        tryRetrieveLocation();
        LocalBroadcastManager.getInstance(context).registerReceiver(permissionGrantReceiver,
                new IntentFilter(Constants.LOCATION_PERMISSION_RESULT));

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIconAttribute(android.R.attr.alertDialogIcon);
        mProgressDialog.setTitle(getString(R.string.location_finder));
        mProgressDialog.setMessage(getString(R.string.pleasewaitgpsDialog));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LocalBroadcastManager.getInstance(context).unregisterReceiver(permissionGrantReceiver);

                        if (latitude != null && longitude != null) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Constants.PREF_LATITUDE, latitude);
                            editor.putString(Constants.PREF_LONGITUDE, longitude);
                            if (cityName != null) {
                                editor.putString(Constants.PREF_GEOCODED_CITYNAME, cityName);
                            } else {
                                editor.putString(Constants.PREF_GEOCODED_CITYNAME, "");
                            }
                            editor.putString(Constants.PREF_SELECTED_LOCATION, Constants.DEFAULT_CITY);
                            editor.apply();
                        }

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager.removeUpdates(locationListener);
                        }

                        //getActivity().recreate();
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);
                        dismiss();
                    }
                });
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dismiss();
                    }
                });
        return mProgressDialog;

        //return builder.create();
    }

    BroadcastReceiver permissionGrantReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tryRetrieveLocation();
        }
    };

    // Just ask for permission once, if we couldn't get it, nvm
    public boolean first = true;

    public void tryRetrieveLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        } else if (first) {
            first = false;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    Constants.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            dismiss();
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                showLocation(location);
                mProgressDialog.setProgress(100);
            } catch (Exception e) {
                // This will catch any exception, because they are all descended from Exception
                System.out.println("Error Location" + e.getMessage());
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            final Handler mHandler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(mProgressDialog.getProgress() < 81){
                            Thread.sleep(100);
                            mHandler.post(new Runnable() {
                                public void run() {
                                    mProgressDialog.setProgress( (mProgressDialog.getProgress()+1));
                                    //if(mProgressDialog.getProgress()>=mProgressDialog.getMax())
                                    //    mProgressDialog.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                    }
                    catch(Exception ignored){
                    }
                }
            }).start();
        }
    };

    String latitude;
    String longitude;
    String cityName;

    public void showLocation(Location location) {
        latitude = String.format(Locale.ENGLISH, "%f", location.getLatitude());
        longitude = String.format(Locale.ENGLISH, "%f", location.getLongitude());
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = "";
        if (cityName != null) {
            result = cityName + "\n\n";
        }
        // this time, with native digits
        result += utils.formatCoordinate(
                new Coordinate(location.getLatitude(), location.getLongitude()), "\n") + "\n\n"+ getString(R.string.gps_location_is_found_dialog) + "\n";
        //textView.setText(utils.shape(result));
        mProgressDialog.setMessage(utils.shape(result));
    }

}
