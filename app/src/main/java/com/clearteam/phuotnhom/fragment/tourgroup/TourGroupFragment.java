package com.clearteam.phuotnhom.fragment.tourgroup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.fragment.tourgroup.adapter.TourGroupAdapter;
import com.clearteam.phuotnhom.fragment.tourgroup.tourall.TourAllFragment;
import com.clearteam.phuotnhom.fragment.tourgroup.tourme.TourMeFragment;
import com.google.android.material.tabs.TabLayout;

public class TourGroupFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TourGroupAdapter adapter;
    private View view;
    MenuItem prevMenuItem;

    private static TourGroupFragment INSTANCE;

    public static TourGroupFragment getInstance() {
        if (INSTANCE == null) {

        }
        return new TourGroupFragment();
    }

    public TourGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_tour_group, container, false);

            mViewPager = view.findViewById(R.id.vp_tour_group);
//            tabLayout = view.findViewById(R.id.tab_tour_group);
////            adapter = new TourGroupAdapter(getActivity().getSupportFragmentManager());
////            mViewPager.setAdapter(adapter);
////            tabLayout.setupWithViewPager(mViewPager);
////            mViewPager.setCurrentItem(0);
            mViewPager.setCurrentItem(0);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    mViewPager.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            setupViewPager(mViewPager);
        }
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        TourGroupAdapter adapter = new TourGroupAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TourMeFragment());
        adapter.addFragment(new TourAllFragment());
        viewPager.setAdapter(adapter);
    }

}
