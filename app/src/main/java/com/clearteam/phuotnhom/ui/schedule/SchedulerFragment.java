package com.clearteam.phuotnhom.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clearteam.phuotnhom.R;

public class SchedulerFragment extends Fragment {
    private static SchedulerFragment INSTANCE;

    public static SchedulerFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduler, container, false);


        return view;
    }
}
