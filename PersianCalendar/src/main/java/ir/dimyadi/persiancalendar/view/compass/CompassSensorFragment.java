package ir.dimyadi.persiancalendar.view.compass;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.dialog.CompassDialog;
import ir.dimyadi.praytime.praytimes.Coordinate;

public class CompassSensorFragment extends Fragment {
    public QiblaCompassView compassView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener compassListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);

        Context context = getContext();
        Utils utils = Utils.getInstance(context);
        Coordinate coordinate = utils.getCoordinate();

        compassListener = new SensorEventListener() {
            /*
             * time smoothing constant for low-pass filter 0 ≤ alpha ≤ 1 ; a smaller
             * value basically means more smoothing See:
             * http://en.wikipedia.org/wiki/Low-pass_filter#Discrete-time_realization
             */
            static final float ALPHA = 0.15f;
            float azimuth;

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                // angle between the magnetic north direction
                // 0=North, 90=East, 180=South, 270=West
                azimuth = lowPass(event.values[0], azimuth);
                compassView.setBearing(azimuth);
                compassView.invalidate();
            }

            /**
             * https://en.wikipedia.org/wiki/Low-pass_filter#Algorithmic_implementation
             * http://developer.android.com/reference/android/hardware/SensorEvent.html#values
             */
            private float lowPass(float input, float output) {
                if (Math.abs(180 - input) > 170) {
                    return input;
                }
                return output + ALPHA * (input - output);
            }
        };
        compassView = (QiblaCompassView) view.findViewById(R.id.compass_view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        compassView.setScreenResolution(width, height - 2 * height / 8);

        if (coordinate != null) {
            compassView.setLongitude(coordinate.getLongitude());
            compassView.setLatitude(coordinate.getLatitude());
            compassView.initCompassView();
            compassView.invalidate();
        }

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            sensorManager.registerListener(compassListener, sensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
            if (coordinate == null) {
                utils.quickToast(getString(R.string.set_location));
            } else {
                //Compass Calibrate Dialog
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                boolean dontShowDialog = sharedPref.getBoolean("DONT_SHOW_DIALOG", false);
                if (!dontShowDialog) {
                    DialogFragment frag = new CompassDialog();
                    frag.show(getActivity().getSupportFragmentManager(), "CompassDialog");
                }
            }
        } else {
            utils.quickToast(getString(R.string.compass_not_found));
        }
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensor != null) {
            sensorManager.unregisterListener(compassListener);
        }
    }
}
