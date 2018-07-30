package ir.dimyadi.persiancalendar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;

import android.widget.RelativeLayout;

public class PrayerFragments extends Fragment {


    RelativeLayout mNames, mTaghibat, mDoua, mAdayeGhaza, mTasbibat, mZekrShomar, mOneFive, mTestament;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prayer, container, false);
        Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.prayer), "");

        //Names
        mNames = (RelativeLayout) view.findViewById(R.id.names);
        mNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new Names();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Taghibat Namaz
        mTaghibat = (RelativeLayout) view.findViewById(R.id.taghibat_namaz);
        mTaghibat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new TaghibatNamaz();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Doua Emrooz
        mDoua = (RelativeLayout) view.findViewById(R.id.doua_emroz);
        mDoua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new TodayDua();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Adaye Ghaza
        mAdayeGhaza = (RelativeLayout) view.findViewById(R.id.adaye_ghaza);
        mAdayeGhaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new GhazaFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Tasbihat
        mTasbibat = (RelativeLayout) view.findViewById(R.id.tasbihat);
        mTasbibat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new TasbihatFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Zekr Shomar
        mZekrShomar = (RelativeLayout) view.findViewById(R.id.zekr_shomar);
        mZekrShomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new MoslemZekr();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //OneFive Calculator
        mOneFive = (RelativeLayout) view.findViewById(R.id.one_five);
        mOneFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new OneFiveFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Testament Note
        mTestament = (RelativeLayout) view.findViewById(R.id.testament);
        mTestament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Fragment someFragment = new TestamentFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, someFragment )
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

}