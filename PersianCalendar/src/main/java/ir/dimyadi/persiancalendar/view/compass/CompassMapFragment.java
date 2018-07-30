package ir.dimyadi.persiancalendar.view.compass;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.dimyadi.persiancalendar.BuildConfig;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.dialog.GPSNetworkDialog;
import ir.dimyadi.praytime.praytimes.Coordinate;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class CompassMapFragment extends Fragment implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap googleMap;

    //  private float currentDegree = 0f;
    private SensorManager mSensorManager;

    private float[] mRotationMatrix = new float[16];
    private float[] mValues = new float[3];

    private boolean mRegistered = false;

    public static CompassMapFragment newInstance(Location location) {
        CompassMapFragment fragment = new CompassMapFragment();
        Bundle args = new Bundle();
        args.putParcelable("last_location", location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kaaba_map, container, false);
        // Try to obtain the map from the SupportMapFragment.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return view;
    }

    public void hideMap() {
        unregisterRotationListener();
    }

    public void setLocation(Location location) {
        initMap();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        Context context = getContext();
        Utils utils = Utils.getInstance(context);
        Coordinate coordinate = utils.getCoordinate();
        if (coordinate == null) {
            utils.quickToast(getString(R.string.set_location));
        } else {
            //Show Map Loc
            initMap();

            //check whether gps provider and network providers are enabled or not
            LocationManager gps = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            NetworkInfo info = ((ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            boolean gps_enabled = false;

            try {
                assert gps != null;
                gps_enabled = gps.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ignored) {}

            if(!gps_enabled || info == null) {
                // Custom Android Alert Dialog Title
                DialogFragment frag = new GPSNetworkDialog();
                frag.show(getActivity().getSupportFragmentManager(), "GPSNetworkDialog");
           }
        }
    }
    private void initMap() {
        if (googleMap == null) {
            Log.w("KabaaLocatorFragment", "Ignoring since mMap or mLastLocation is null");
            return;
        }

        registerRotationListener();

        Context context = getContext();
        Utils utils = Utils.getInstance(context);
        Coordinate coordinate = utils.getCoordinate();

        LatLng startPosition = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
        //21.4224698,39.8262066
        LatLng kaaba = new LatLng(21.4224698, 39.8262066);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        //googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 13));

        googleMap.clear();

        googleMap.addMarker(new MarkerOptions()
                .title(getString(R.string.kaaba))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_kaaba))
                .position(kaaba));

        googleMap.addMarker(new MarkerOptions()
                .title(getString(R.string.you_location))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_praying))
                .position(startPosition));

        // Polylines are useful for marking paths and routes on the map.
        googleMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(startPosition)  // user position
                .add(kaaba)
                .color(Color.RED).width(15));  // Kaabah
    }

    @Override
    public void onResume() {
        super.onResume();
        registerRotationListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterRotationListener();
    }

    private void registerRotationListener() {
        if (googleMap != null && !mRegistered) {
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI);
            mRegistered = true;
        }
    }

    private void unregisterRotationListener() {
        if (mRegistered) {
            mSensorManager.unregisterListener(this);
            mRegistered = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
        SensorManager.getOrientation(mRotationMatrix, mValues);

    /*
    if (BuildConfig.DEBUG) {
      System.out.println("sensorChanged (" + Math.toDegrees(mValues[0]) + ", " + Math.toDegrees(mValues[1]) + ", " + Math.toDegrees(mValues[2]) + ")");
    }
    */

        float bearing = 0f;

        if (googleMap != null) {
            bearing = Double.valueOf(googleMap.getCameraPosition().bearing).floatValue();
        }

        // get the angle around the z-axis rotated
        float degree = Double.valueOf(Math.toDegrees(mValues[0])).floatValue();

        if (Math.round(bearing) == Math.round(degree)) {
            System.out.println("bearing and degrees are the same.");
            return;
        }

        if (BuildConfig.DEBUG) {
            System.out.println("degrees " + degree + ", bearing " + bearing);
        }

        //tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        CameraPosition cameraPosition = googleMap.getCameraPosition();
        CameraPosition newPosition = new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, degree);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(newPosition));
        // create a rotation animation (reverse turn degree degrees)
    /*
    RotateAnimation ra = new RotateAnimation(
            currentDegree,
            -degree-180,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f);
    // how long the animation will take place
    ra.setDuration(210);
    // set the animation after the end of the reservation status
    ra.setFillAfter(true);
    // Start the animation
    mCompass.startAnimation(ra);
    currentDegree = -degree-180;
    */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}