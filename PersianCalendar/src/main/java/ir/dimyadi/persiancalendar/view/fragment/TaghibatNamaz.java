package ir.dimyadi.persiancalendar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.taghibat.MainPagerAdapter;

public class TaghibatNamaz extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_layout, container, false);
        Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.taghibatnamaz), "");

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        //to add the name of two tabs
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sobh));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.zohr));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.asr));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.maghreb));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.esha));

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(new MainPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount()));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }
}
