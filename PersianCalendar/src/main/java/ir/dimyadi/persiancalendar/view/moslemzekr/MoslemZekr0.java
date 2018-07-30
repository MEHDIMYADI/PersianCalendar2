package ir.dimyadi.persiancalendar.view.moslemzekr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import ir.dimyadi.persiancalendar.util.Utils;

import ir.dimyadi.persiancalendar.R;

import static android.content.Context.MODE_PRIVATE;

public class MoslemZekr0 extends Fragment {

    private TextView tv0;
    private EditText sc0;
    private int counter0;
    private SwitchCompat toggle0,vibtoggle0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_moslemzekr, container, false);
        final Utils utils = Utils.getInstance(getContext());

        tv0 = (TextView) view.findViewById(R.id.tv);
        utils.setFont(tv0);
        final ImageView counters0 = (ImageView) view.findViewById(R.id.count);
        ImageView reset0 = (ImageView) view.findViewById(R.id.reset);
        sc0 = (EditText) view.findViewById(R.id.setcount);
        utils.setFont(sc0);
        toggle0=(SwitchCompat) view.findViewById(R.id.toggle);
        vibtoggle0=(SwitchCompat) view.findViewById(R.id.vibtoggle);

        loadSharedPreferences();

        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Instructions");
        //builder.setMessage("In app there are two options to count \n 1. By using Count Button. \n 2. By using Volume Up Button")
        //        .setCancelable(false)
        //        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        //            public void onClick(DialogInterface dialog, int id) {
        //                dialog.dismiss();
        //            }
        //        });
        //AlertDialog alert = builder.create();
        //alert.show();

        final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.click);

        counters0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toggle0.isChecked()) {
                    mp.start();
                }

                if(vibtoggle0.isChecked()){
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(50);
                }

                int count0 = 0;
                SharedPreferences.Editor editor0 = getActivity().getPreferences(MODE_PRIVATE).edit();
                int counter0 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key0",count0);
                ++counter0;
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key0",counter0).apply();
                count0 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key0",count0);
                System.out.println("The count value is " + count0);
                editor0.apply();

                SharedPreferences userDetails0 = getActivity().getSharedPreferences("UserDetails0", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor00=userDetails0.edit();
                editor00.putString("storedSetCount0",sc0.getText().toString());
                editor00.apply();

                if (counter0 == Integer.parseInt(sc0.getText().toString())) {
                    setCount0();
                }

                if (counter0 %100 == 0){
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(2000);
                }

                tv0.setText(utils.formatNumber(String.valueOf(counter0)));
            }
        });

        reset0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count0 = 0;
                SharedPreferences.Editor editor0 = getActivity().getPreferences(MODE_PRIVATE).edit();
                counter0 = 0;
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key0",counter0).apply();
                count0 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key0",count0);
                System.out.println("The count value is " + count0);
                editor0.apply();
                tv0.setText(utils.formatNumber("0"));

                SharedPreferences userDetails0 = getActivity().getSharedPreferences("UserDetails0", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor00=userDetails0.edit();
                editor00.putString("storedSetCount0","0");
                sc0.setText("0");
                editor00.apply();

            }
        });

        toggle0.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences soundPreferences0=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor0=soundPreferences0.edit();
                editor0.putBoolean("toggle0",toggle0.isChecked());
                editor0.apply();
            }
        });

        vibtoggle0.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences vibPreferences0= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor0=vibPreferences0.edit();
                editor0.putBoolean("vibtoggle0",vibtoggle0.isChecked());
                editor0.apply();
            }
        });

        return view;
    }

    private void loadSharedPreferences(){
        SharedPreferences soundPreferences0=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        toggle0.setChecked(soundPreferences0.getBoolean("toggle0",false));
        SharedPreferences vibPreferences0=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        vibtoggle0.setChecked(vibPreferences0.getBoolean("vibtoggle0",false));
        SharedPreferences userDetails0 = getActivity().getSharedPreferences("UserDetails0", Context.MODE_PRIVATE);
        String tempSetCount0 = userDetails0.getString("storedSetCount0", "0");
        Utils utils = Utils.getInstance(getContext());
        sc0.setText(utils.formatNumber(tempSetCount0));
    }

    @Override
    public void onStart(){
        super.onStart();

        int count0 = 0;
        SharedPreferences.Editor editor0 = getActivity().getPreferences(MODE_PRIVATE).edit();
        int counter0 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key0",count0);
        getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key0",counter0).apply();
        count0 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key0",count0);
        System.out.println("The count value is " + count0);
        editor0.apply();
        Utils utils = Utils.getInstance(getContext());
        tv0.setText(utils.formatNumber(String.valueOf(count0)));

        SharedPreferences userDetails0 = getActivity().getSharedPreferences("UserDetails0", Context.MODE_PRIVATE);
        String tempSetCount0 = userDetails0.getString("storedSetCount0", "0");
        utils = Utils.getInstance(getContext());
        sc0.setText(utils.formatNumber(tempSetCount0));

    }

    public void setCount0() {

        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(1000);
        //Toast.makeText(getActivity().getApplicationContext(), "You have reached your target", Toast.LENGTH_LONG).show();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        sc0.setText("0");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        int count0 = 0;
                        SharedPreferences.Editor editor0 = getActivity().getPreferences(MODE_PRIVATE).edit();
                        counter0 = 0;
                        getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key0",counter0).apply();
                        count0 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key0",count0);
                        System.out.println("The count value is " + count0);
                        editor0.apply();
                        Utils utils = Utils.getInstance(getContext());
                        tv0.setText(utils.formatNumber("0"));

                        SharedPreferences userDetails0 = getActivity().getSharedPreferences("UserDetails0", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor00=userDetails0.edit();
                        editor00.putString("storedSetCount0","0");
                        sc0.setText("0");
                        editor00.apply();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.zekrmore)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
    }

}
