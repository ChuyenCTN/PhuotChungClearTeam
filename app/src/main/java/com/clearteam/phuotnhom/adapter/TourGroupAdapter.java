package com.clearteam.phuotnhom.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.clearteam.phuotnhom.fragment.TourAllFragment;
import com.clearteam.phuotnhom.fragment.TourMeFragment;

public class TourGroupAdapter extends FragmentPagerAdapter {

    private String[] title = new String[]{"Tour của bạn","Tất cả tuor"};
    public TourGroupAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TourMeFragment.getInstance();
            case 1:
                return TourAllFragment.getInstance();
        }
        return null;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }
}
