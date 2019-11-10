package com.clearteam.phuotnhom.ui.tourgroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.ui.tourgroup.adapter.AdapterTourGroup;
import com.clearteam.phuotnhom.ui.tourgroup.model.TourGroupResponse;
import com.clearteam.phuotnhom.utils.CommonUtils;

public class TourGroupFragment extends Fragment {
    private static TourGroupFragment INSTANCE;

    private TextView tvNoDataTourGroup;
    private SwipeRefreshLayout swipeTourGroup;
    private RecyclerView rcvTourGroup;

    public static TourGroupFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TourGroupFragment();
        }
        return INSTANCE;
    }

    private AdapterTourGroup adapterTourGroup;

    private boolean checkNew = true;  // true: show dialog; false: none


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_group, container, false);
        mapping(view);
        initRecyclerView();
        checkGroup();
        return view;
    }

    private void mapping(View view) {
        tvNoDataTourGroup = (TextView) view.findViewById(R.id.tv_no_data_tour_group);
        swipeTourGroup = (SwipeRefreshLayout) view.findViewById(R.id.swipe_tour_group);
        rcvTourGroup = (RecyclerView) view.findViewById(R.id.rcv_tour_group);

    }

    private void initRecyclerView() {
        adapterTourGroup = new AdapterTourGroup();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rcvTourGroup.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcvTourGroup.getContext(),
                linearLayoutManager.getOrientation());
        rcvTourGroup.addItemDecoration(dividerItemDecoration);
        rcvTourGroup.setAdapter(adapterTourGroup);

        adapterTourGroup.setClickDetailTourGroup(new AdapterTourGroup.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, TourGroupResponse response) {

            }

            @Override
            public void onMoreClick(int position, TourGroupResponse response) {

            }
        });
    }

    private void checkGroup() {
        if (checkNew) {
            MaterialDialog.SingleButtonCallback handleOK = ((dialog, which) -> {

            });

            MaterialDialog.SingleButtonCallback handleCancel = (dialog, which) -> {
                dialog.dismiss();
            };

            CommonUtils.showDialog(getContext(), getString(R.string.txt_alert_notifi), getString(R.string.txt_warning_no_tour_group), handleOK, handleCancel);
        }
    }
}
