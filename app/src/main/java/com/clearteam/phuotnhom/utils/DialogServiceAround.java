package com.clearteam.phuotnhom.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.ServiceAroundAdapter;
import com.clearteam.phuotnhom.model.ServiceAround;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DialogServiceAround extends BottomSheetDialogFragment {
    private List<ServiceAround> mServiceAroundList;
    private ServiceAroundAdapter adapter;
    private RecyclerView mRecyclerView;
    private IChoose iChoose;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN){
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    };

    public DialogServiceAround(List<ServiceAround> mServiceAroundList, IChoose iChoose) {
        this.mServiceAroundList = mServiceAroundList;
        this.iChoose = iChoose;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(this);
//        dialog.setContentView(dialogView);
//       // dialog.show();
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) dialogView.getParent())
//                .getLayoutParams();
//        CoordinatorLayout.Behavior behavior = params.getBehavior();
//        ((View) dialogView.getParent()).setBackgroundResource(R.drawable.bg_bottom_sheet);
//
//        if (behavior !=null && behavior instanceof BottomSheetBehavior){
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallback);
//        }
//        mRecyclerView = view.findViewById(R.id.rcv_service_around);
//
//        initData();
//        adapter = new ServiceAroundAdapter(getContext(),mServiceAroundList);
//
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(RecyclerView.HORIZONTAL);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setAdapter(adapter);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet, null);
        dialog.setContentView(contentView);

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallback);
        }

//        TextView tvTitle = contentView.findViewById(R.id.tv_name_service);
//        tvTitle.setText("Hotel");
        initData();
        RecyclerView recyclerView = contentView.findViewById(R.id.rcv_service_around);
        LinearLayoutManager layoutManager = new LinearLayoutManager(contentView.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ServiceAroundAdapter(getContext(),mServiceAroundList, new ServiceAroundAdapter.IClickItem() {
            @Override
            public void onItemClick(ServiceAround serviceAround) {
                iChoose.onChoose(serviceAround);
                DialogServiceAround.this.dismiss();
            }
        });

        recyclerView.setAdapter(adapter);

    }
    private void initData() {
        mServiceAroundList = new ArrayList<>();
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg","khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768","khách sạn"));
    }
    public interface IChoose{
        void onChoose(ServiceAround serviceAround);
    }
}
