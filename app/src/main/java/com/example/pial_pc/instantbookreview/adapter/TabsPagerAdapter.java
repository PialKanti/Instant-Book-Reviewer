package com.example.pial_pc.instantbookreview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.example.pial_pc.instantbookreview.BookFragment;
import com.example.pial_pc.instantbookreview.MapFragment;
import com.example.pial_pc.instantbookreview.OnlineBuyFragment;
import com.example.pial_pc.instantbookreview.ReviewFragment;


/**
 * Created by Pial-PC on 2/5/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new BookFragment();
            case 1:
                return new ReviewFragment();
            case 2:
                return  new MapFragment();
            case 3:
                return  new OnlineBuyFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
}
