package com.example.wolf_z.bookingroom.Menu_Nevigator.Profile_Setting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class Profile_Setting_PagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs;
    private Profile_Setting_Activity profile_setting_activity;

    Profile_Setting_PagerAdapter(FragmentManager fm, int NumOfTabs, Profile_Setting_Activity profile_setting_activity) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.profile_setting_activity = profile_setting_activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return profile_setting_activity.getProfile_change_password_fragment();
            case 1:
                return profile_setting_activity.getProfile_change_displayname_fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
