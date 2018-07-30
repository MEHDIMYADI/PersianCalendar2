package ir.dimyadi.persiancalendar.view.onefive;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private int num;

    private AddCategoryFragment tab0;
    private AddOneFiveFragment tab1;
    private ShowResultByCategory tab2;

    public MainPagerAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                tab0 = new AddCategoryFragment();
                return tab0;
            case 1:
                tab1 = new AddOneFiveFragment();
                return tab1;
            case 2:
                tab2 = new ShowResultByCategory();
                return tab2;
            default:
                return null;
        }
    }

    public void refreshFragment(int position) {
        switch (position) {
            case 0:
                tab0.refreshApi();
                break;
            case 1:
                tab1.refreshApi();
                break;
            case 2:
                tab2.refreshApi();
                break;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
