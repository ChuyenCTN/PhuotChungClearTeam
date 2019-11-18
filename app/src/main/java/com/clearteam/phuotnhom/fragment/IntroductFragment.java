package com.clearteam.phuotnhom.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearteam.phuotnhom.R;

public class IntroductFragment extends Fragment {
    private static IntroductFragment INSTANCE;
    public static IntroductFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IntroductFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduct, container, false);
        return view;
    }

}
