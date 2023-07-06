package edu.birzeit.zamilihotal.adabter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.birzeit.zamilihotal.fragment.Booking_Fragment;


public class FragmentPagerAdapterP extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Active", "Past"};
    private Context context;

    public FragmentPagerAdapterP(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Booking_Fragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
