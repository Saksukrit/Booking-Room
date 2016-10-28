package com.example.wolf_z.bookingroom.Menu.Profile_Setting;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.wolf_z.bookingroom.R;

public class Profile_Setting_Activity extends AppCompatActivity {

    private Profile_Change_Password_Fragment profile_change_password_fragment = new Profile_Change_Password_Fragment();
    private Profile_Change_Displayname_Fragment profile_change_displayname_fragment = new Profile_Change_Displayname_Fragment();
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /** Tab */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_setting_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Change Password"));
        tabLayout.addTab(tabLayout.newTab().setText("Change Displayname"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /** ViewPager */
        final ViewPager viewPager = (ViewPager) findViewById(R.id.profile_setting_pager);
        final Profile_Setting_PagerAdapter adapter = new Profile_Setting_PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), this);

        viewPager.setAdapter(adapter);
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

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    public Profile_Change_Password_Fragment getProfile_change_password_fragment() {
        return profile_change_password_fragment;
    }

    public Profile_Change_Displayname_Fragment getProfile_change_displayname_fragment() {
        return profile_change_displayname_fragment;
    }
}
