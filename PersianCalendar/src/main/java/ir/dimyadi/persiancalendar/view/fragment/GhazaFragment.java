package ir.dimyadi.persiancalendar.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;


public class GhazaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ghaza, container, false);

        Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.ghaza), "");

        ViewGroup vg = (ViewGroup) view.findViewById(R.id.main);

        int[] ids = {
                R.string.namazsobh, R.string.namazzuhr, R.string.namazasr, R.string.namazmaghrib, R.string.namazishaa,
                R.string.nafelesobh, R.string.nafelezohr, R.string.nafeleasr, R.string.nafelemaghreb, R.string.nafeleesha, R.string.nafeleshab,
                R.string.ghazaroze,};
        for (int i = 0; i < 12; i++) {
            View v = vg.getChildAt(i);
            TextView name = (TextView) v.findViewById(R.id.text);
            final EditText nr = (EditText) v.findViewById(R.id.nr);
            ImageView plus = (ImageView) v.findViewById(R.id.plus);
            ImageView minus = (ImageView) v.findViewById(R.id.minus);

            name.setText(ids[i]);

            plus.setOnClickListener(new View.OnClickListener() {
                Utils utils = Utils.getInstance(getContext());
                @Override
                public void onClick(View arg0) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(nr.getText().toString());
                    } finally {
                        i++;
                    }
                    nr.setText(utils.formatNumber(i + ""));

                }
            });

            minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Utils utils = Utils.getInstance(getContext());
                    int i = 0;
                    try {
                        i = Integer.parseInt(nr.getText().toString());
                    } finally {
                        i--;
                    }

                    nr.setText(utils.formatNumber(i + ""));
                }
            });

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup vg = (ViewGroup) getView().findViewById(R.id.main);
        SharedPreferences prefs = getActivity().getSharedPreferences("kaza", 0);
        for (int i = 0; i < 12; i++) {
            View v = vg.getChildAt(i);
            EditText nr = (EditText) v.findViewById(R.id.nr);
            nr.setText(prefs.getString("count" + i, "0"));

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ViewGroup vg = (ViewGroup) getView().findViewById(R.id.main);
        SharedPreferences.Editor edit = getActivity().getSharedPreferences("kaza", 0).edit();
        for (int i = 0; i < 12; i++) {
            View v = vg.getChildAt(i);
            EditText nr = (EditText) v.findViewById(R.id.nr);
            edit.putString("count" + i, nr.getText().toString());

        }

        edit.apply();
    }
}
