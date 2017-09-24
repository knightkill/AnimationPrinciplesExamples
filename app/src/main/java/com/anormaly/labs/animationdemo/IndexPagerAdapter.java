package com.anormaly.labs.animationdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ichigo on 24/09/17.
 */

public class IndexPagerAdapter extends FragmentStatePagerAdapter
{

    int TOTOL_PAGES = 2;

    public IndexPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0: return new SquashAndStretchFragment();
            case 1: return new AnticipationFragment();
            default: return new SquashAndStretchFragment();
        }
    }

    @Override
    public int getCount()
    {
        return TOTOL_PAGES;
    }
}
