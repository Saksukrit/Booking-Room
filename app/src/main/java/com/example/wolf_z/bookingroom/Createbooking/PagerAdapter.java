package com.example.wolf_z.bookingroom.Createbooking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class PagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs;
    private Createbooking createbooking;

    PagerAdapter(FragmentManager fm, int NumOfTabs, Createbooking createbooking) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.createbooking = createbooking;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return createbooking.getSubjectFragment();
            case 1:
                return createbooking.getParticipantFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
