package com.clearteam.phuotnhom.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.TourGroupAdapter;
import com.clearteam.phuotnhom.ui.TourGroupDetailActivity;
import com.google.android.material.tabs.TabLayout;

public class TourGroupFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TourGroupAdapter adapter;
    private View view;

    private static TourGroupFragment INSTANCE;

    public static TourGroupFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TourGroupFragment();
        }
        return INSTANCE;
    }

    public TourGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatustBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_tour_group, container, false);

            mViewPager = view.findViewById(R.id.vp_tour_group);
            tabLayout = view.findViewById(R.id.tab_tour_group);
            adapter = new TourGroupAdapter(getActivity().getSupportFragmentManager());
            mViewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(mViewPager);

            // mViewPager.setCurrentItem(0);

        }
        return view;
    }

    private void changeStatustBar() {
        Window window = getActivity().getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.bg_tab));
        }
    }
}
