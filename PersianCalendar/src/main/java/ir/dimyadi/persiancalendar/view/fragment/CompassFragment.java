package ir.dimyadi.persiancalendar.view.fragment;

import android.content.Context;
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
import ir.dimyadi.persiancalendar.view.compass.MainPagerAdapter;
import ir.dimyadi.praytime.praytimes.Coordinate;

public class CompassFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_layout, container, false);

        Context context = getContext();
        Utils utils = Utils.getInstance(context);
        Coordinate coordinate = utils.getCoordinate();
        if (coordinate == null) {
            utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.compass), "");
        } else {
            utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.qibla_compass),
                    utils.getCityName(true));
        }

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        //to add the name of two tabs
        tabLayout.addTab(tabLayout.newTab().setText(R.string.with_sensor));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.with_map));

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
