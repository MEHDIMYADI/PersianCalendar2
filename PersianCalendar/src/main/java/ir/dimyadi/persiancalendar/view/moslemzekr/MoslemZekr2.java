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

public class MoslemZekr2 extends Fragment {

    private TextView tv2;
    private EditText sc2;
    private int counter2;
    private SwitchCompat toggle2,vibtoggle2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_moslemzekr, container, false);
        final Utils utils = Utils.getInstance(getContext());

        tv2 = (TextView) view.findViewById(R.id.tv);
        utils.setFont(tv2);
        final ImageView counters2 = (ImageView) view.findViewById(R.id.count);
        ImageView reset2 = (ImageView) view.findViewById(R.id.reset);
        sc2 = (EditText) view.findViewById(R.id.setcount);
        utils.setFont(sc2);
        toggle2=(SwitchCompat) view.findViewById(R.id.toggle);
        vibtoggle2=(SwitchCompat) view.findViewById(R.id.vibtoggle);

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

        counters2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(toggle2.isChecked()) {
                    mp.start();
                }

                if(vibtoggle2.isChecked()){
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(50);
                }

                int count2 = 0;
                SharedPreferences.Editor editor2 = getActivity().getPreferences(MODE_PRIVATE).edit();
                int counter2 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key2",count2);
                ++counter2;
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key2",counter2).apply();
                count2 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key2",count2);
                System.out.println("The count value is " + count2);
                editor2.apply();

                SharedPreferences userDetails2 = getActivity().getSharedPreferences("UserDetails2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor02=userDetails2.edit();
                editor02.putString("storedSetCount2",sc2.getText().toString());
                editor02.apply();

                if (counter2 == Integer.parseInt(sc2.getText().toString())) {
                    setCount2();
                }

                if (counter2 %100 == 0){
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(2000);
                }

                tv2.setText(utils.formatNumber(String.valueOf(counter2)));
            }
        });

        reset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count2 = 0;
                SharedPreferences.Editor editor2 = getActivity().getPreferences(MODE_PRIVATE).edit();
                counter2 = 0;
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key2",counter2).apply();
                count2 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key2",count2);
                System.out.println("The count value is " + count2);
                editor2.apply();
                tv2.setText(utils.formatNumber("0"));

                SharedPreferences userDetails2 = getActivity().getSharedPreferences("UserDetails2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor02=userDetails2.edit();
                editor02.putString("storedSetCount2","0");
                sc2.setText("0");
                editor02.apply();

            }
        });

        toggle2.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences soundPreferences2=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor2=soundPreferences2.edit();
                editor2.putBoolean("toggle2",toggle2.isChecked());
                editor2.apply();
            }
        });

        vibtoggle2.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences vibPreferences2= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor2=vibPreferences2.edit();
                editor2.putBoolean("vibtoggle2",vibtoggle2.isChecked());
                editor2.apply();
            }
        });

        return view;
    }

    private void loadSharedPreferences(){
        SharedPreferences soundPreferences2=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        toggle2.setChecked(soundPreferences2.getBoolean("toggle2",false));
        SharedPreferences vibPreferences2=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        vibtoggle2.setChecked(vibPreferences2.getBoolean("vibtoggle2",false));
        SharedPreferences userDetails2 = getActivity().getSharedPreferences("UserDetails2", Context.MODE_PRIVATE);
        String tempSetCount2 = userDetails2.getString("storedSetCount2", "SetCount2");
        Utils utils = Utils.getInstance(getContext());
        sc2.setText(utils.formatNumber(tempSetCount2));
    }

    @Override
    public void onStart(){
        super.onStart();

        int count2 = 0;
        SharedPreferences.Editor editor2 = getActivity().getPreferences(MODE_PRIVATE).edit();
        int counter2 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key2",count2);
        getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key2",counter2).apply();
        count2 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key2",count2);
        System.out.println("The count value is " + count2);
        editor2.apply();
        Utils utils = Utils.getInstance(getContext());
        tv2.setText(utils.formatNumber(String.valueOf(count2)));

        SharedPreferences userDetails2 = getActivity().getSharedPreferences("UserDetails2", Context.MODE_PRIVATE);
        String tempSetCount2 = userDetails2.getString("storedSetCount2", "0");
        utils = Utils.getInstance(getContext());
        sc2.setText(utils.formatNumber(tempSetCount2));

    }

    public void setCount2() {

        Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(1000);
        //Toast.makeText(getActivity().getApplicationContext(), "You have reached your target", Toast.LENGTH_LONG).show();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        sc2.setText("0");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        int count2 = 0;
                        SharedPreferences.Editor editor2 = getActivity().getPreferences(MODE_PRIVATE).edit();
                        counter2 = 0;
                        getActivity().getPreferences(MODE_PRIVATE).edit().putInt("count_key2",counter2).apply();
                        count2 = getActivity().getPreferences(MODE_PRIVATE).getInt("count_key2",count2);
                        System.out.println("The count value is " + count2);
                        editor2.apply();
                        Utils utils = Utils.getInstance(getContext());
                        tv2.setText(utils.formatNumber("0"));

                        SharedPreferences userDetails2 = getActivity().getSharedPreferences("UserDetails2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor02=userDetails2.edit();
                        editor02.putString("storedSetCount2","0");
                        sc2.setText("0");
                        editor02.apply();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.zekrmore)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
    }

}
