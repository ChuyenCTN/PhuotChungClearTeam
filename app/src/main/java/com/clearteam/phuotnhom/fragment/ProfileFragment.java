package com.clearteam.phuotnhom.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearteam.phuotnhom.R;


public class ProfileFragment extends Fragment {
    private static ProfileFragment INSTANCE;

    public static ProfileFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileFragment();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

}
