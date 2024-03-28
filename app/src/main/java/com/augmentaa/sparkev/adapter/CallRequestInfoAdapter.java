package com.augmentaa.sparkev.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.augmentaa.sparkev.ui.fragment.CallHistoryFragment;
import com.augmentaa.sparkev.ui.fragment.UpcomingCallFragment;

public class CallRequestInfoAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    int position;

    public CallRequestInfoAdapter(Context context, FragmentManager fm, int totalTabs, int position) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.position=position;

    }

    // this is for fragment tabs  
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UpcomingCallFragment sportFragment = new UpcomingCallFragment();
                return sportFragment;

            case 1:
                CallHistoryFragment homeFragment = new CallHistoryFragment();
                return homeFragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}  