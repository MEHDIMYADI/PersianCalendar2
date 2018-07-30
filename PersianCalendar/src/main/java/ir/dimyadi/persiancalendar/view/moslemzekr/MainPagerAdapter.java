package ir.dimyadi.persiancalendar.view.moslemzekr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                return new MoslemZekr0();
            case 1:
                return new MoslemZekr1();
            case 2:
                return new MoslemZekr2();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
