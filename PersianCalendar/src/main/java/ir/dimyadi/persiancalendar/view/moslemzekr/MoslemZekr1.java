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

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;

import static android.content.Context.MODE_PRIVATE;

public class MoslemZekr1 extends Fragment {

    private TextView tv1;
    private EditText sc1;
    private int counter1;
    private SwitchCompat toggle1,vibtoggle1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_moslemzekr, container, false);
        final Utils utils = Utils.getInstance(getContext());

        tv1 = (TextView) view.findViewById(R.id.tv);
        utils.setFont(tv1);
        final ImageView counters1 = (ImageView) view.findViewById(R.id.count);
        ImageView reset1 = (ImageView) view.findViewById(R.id.reset);
        sc1 = (EditText) view.findViewById(R.id.setcount);
        utils.setFont(sc1);
        toggle1=(SwitchCompat) view.findViewById(R.id.toggle);
        vibtoggle1=(SwitchCompat) view.findViewById(R.id.vibtoggle);

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

        counters1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toggle1.isChecked()) {
                    mp.start();
                }

                if(vibtoggle1.isChecked()){
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(50);
                }

                int count1 = 0;
                SharedPreferences.Editor editor = getActivity().getPreferences(MODE_PRIVATE).edit();
                int counter1 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key1",count1);
                ++counter1;
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key1",counter1).apply();
                count1 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key1",count1);
                System.out.println("The count value is " + count1);
                editor.apply();

                SharedPreferences userDetails1 = getActivity().getSharedPreferences("UserDetails1", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1=userDetails1.edit();
                editor1.putString("storedSetCount1",sc1.getText().toString());
                editor1.apply();

                if (counter1 == Integer.parseInt(sc1.getText().toString())) {
                    setCount1();
                }

                if (counter1 %100 == 0){
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(2000);
                }

                tv1.setText(utils.formatNumber(String.valueOf(counter1)));
            }
        });

        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count1 = 0;
                SharedPreferences.Editor editor1 = getActivity().getPreferences(MODE_PRIVATE).edit();
                counter1 = 0;
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key1",counter1).apply();
                count1 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key1",count1);
                System.out.println("The count value is " + count1);
                editor1.apply();
                tv1.setText(utils.formatNumber("0"));

                SharedPreferences userDetails1 = getActivity().getSharedPreferences("UserDetails1", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor01=userDetails1.edit();
                editor01.putString("storedSetCount1","0");
                sc1.setText("0");
                editor01.apply();

            }
        });

        toggle1.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences soundPreferences1=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor1=soundPreferences1.edit();
                editor1.putBoolean("toggle1",toggle1.isChecked());
                editor1.apply();
            }
        });

        vibtoggle1.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences vibPreferences1= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor1=vibPreferences1.edit();
                editor1.putBoolean("vibtoggle1",vibtoggle1.isChecked());
                editor1.apply();
            }
        });

        return view;
    }

    private void loadSharedPreferences(){
        SharedPreferences soundPreferences1=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        toggle1.setChecked(soundPreferences1.getBoolean("toggle1",false));
        SharedPreferences vibPreferences1=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        vibtoggle1.setChecked(vibPreferences1.getBoolean("vibtoggle1",false));
        SharedPreferences userDetails1 = getActivity().getSharedPreferences("UserDetails1", Context.MODE_PRIVATE);
        String tempSetCount1 = userDetails1.getString("storedSetCount1", "0");
        Utils utils = Utils.getInstance(getContext());
        sc1.setText(utils.formatNumber(tempSetCount1));
    }

    @Override
    public void onStart(){
        super.onStart();

        int count1 = 0;
        SharedPreferences.Editor editor1 = getActivity().getPreferences(MODE_PRIVATE).edit();
        int counter1 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key1",count1);
        getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key1",counter1).apply();
        count1 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key1",count1);
        System.out.println("The count value is " + count1);
        editor1.apply();
        Utils utils = Utils.getInstance(getContext());
        tv1.setText(utils.formatNumber(String.valueOf(count1)));

        SharedPreferences userDetails1 = getActivity().getSharedPreferences("UserDetails1", Context.MODE_PRIVATE);
        String tempSetCount1 = userDetails1.getString("storedSetCount1", "0");
        utils = Utils.getInstance(getContext());
        sc1.setText(utils.formatNumber(tempSetCount1));

    }

    public void setCount1() {

        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(1000);
        //Toast.makeText(getActivity().getApplicationContext(), "You have reached your target", Toast.LENGTH_LONG).show();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        sc1.setText("0");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        int count1 = 0;
                        SharedPreferences.Editor editor1 = getActivity().getPreferences(MODE_PRIVATE).edit();
                        counter1 = 0;
                        getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key1",counter1).apply();
                        count1 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key1",count1);
                        System.out.println("The count value is " + count1);
                        editor1.apply();
                        Utils utils = Utils.getInstance(getContext());
                        tv1.setText(utils.formatNumber("0"));

                        SharedPreferences userDetails1 = getActivity().getSharedPreferences("UserDetails1", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor01=userDetails1.edit();
                        editor01.putString("storedSetCount1","0");
                        sc1.setText("0");
                        editor01.apply();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.zekrmore)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
    }

}
