package com.apper.sarwar.tmsapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.apper.sarwar.tmsapp.Fragment.SignInFragment;
import com.apper.sarwar.tmsapp.Fragment.SignUpFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static int TAB_COUNT=2;

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SignInFragment.newInstance();
            case 1:
                return SignUpFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return SignInFragment.TITLE;

            case 1:
                return SignUpFragment.TITLE;

        }
        return super.getPageTitle(position);
    }
}
