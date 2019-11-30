package com.clearteam.phuotnhom.ui.schedule;

import androidx.fragment.app.Fragment;

public class SchedulerFragment extends Fragment {
    private static SchedulerFragment INSTANCE;

    public static SchedulerFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerFragment();
        }
        return INSTANCE;
    }

    
}
