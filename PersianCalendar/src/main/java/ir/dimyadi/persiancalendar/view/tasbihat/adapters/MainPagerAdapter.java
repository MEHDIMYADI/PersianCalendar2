package ir.dimyadi.persiancalendar.view.tasbihat.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ir.dimyadi.persiancalendar.view.tasbihat.fragments.Ehsaa;
import ir.dimyadi.persiancalendar.view.tasbihat.fragments.BaadAlsalahFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private int num;

    public MainPagerAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BaadAlsalahFragment();
            case 1:
                return new Ehsaa();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
