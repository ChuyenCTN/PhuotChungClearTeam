package com.clearteam.phuotnhom.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.ServiceAroundAdapter;
import com.clearteam.phuotnhom.model.ServiceAround;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DialogServiceAround extends BottomSheetDialogFragment implements View.OnClickListener {
    private List<ServiceAround> mServiceAroundList;
    private ServiceAroundAdapter adapter;
    private RecyclerView mRecyclerView;
    private IChoose iChoose;

    private LinearLayout layoutRestaurentBts;
    private LinearLayout layoutHotelBts;
    private LinearLayout layoutPharmaciesBts;
    private LinearLayout layoutHospitalBts;
    private LinearLayout layoutAtmBts;
    private LinearLayout layoutPetroleumBts;
    private LinearLayout layoutGroceryBts;
    private LinearLayout layoutTouriesBts;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
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

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet, null);
        dialog.setContentView(contentView);

        layoutRestaurentBts = (LinearLayout) contentView.findViewById(R.id.layout_restaurent_bts);
        layoutHotelBts = (LinearLayout) contentView.findViewById(R.id.layout_hotel_bts);
        layoutPharmaciesBts = (LinearLayout) contentView.findViewById(R.id.layout_pharmacies_bts);
        layoutHospitalBts = (LinearLayout) contentView.findViewById(R.id.layout_hospital_bts);
        layoutAtmBts = (LinearLayout) contentView.findViewById(R.id.layout_atm_bts);
        layoutPetroleumBts = (LinearLayout) contentView.findViewById(R.id.layout_petroleum_bts);
        layoutGroceryBts = (LinearLayout) contentView.findViewById(R.id.layout_grocery_bts);
        layoutTouriesBts = (LinearLayout) contentView.findViewById(R.id.layout_touries_bts);


        layoutAtmBts.setOnClickListener(this::onClick);
        layoutGroceryBts.setOnClickListener(this::onClick);
        layoutHospitalBts.setOnClickListener(this::onClick);
        layoutHotelBts.setOnClickListener(this::onClick);
        layoutPetroleumBts.setOnClickListener(this::onClick);
        layoutPharmaciesBts.setOnClickListener(this::onClick);
        layoutRestaurentBts.setOnClickListener(this::onClick);
        layoutTouriesBts.setOnClickListener(this::onClick);

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundResource(R.drawable.bg_bottom_sheet);

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallback);
        }

        initData();
        RecyclerView recyclerView = contentView.findViewById(R.id.rcv_service_around);
        LinearLayoutManager layoutManager = new LinearLayoutManager(contentView.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ServiceAroundAdapter(getContext(), mServiceAroundList, new ServiceAroundAdapter.IClickItem() {
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
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://q-cf.bstatic.com/images/hotel/max500/186/186515203.jpg", "khách sạn"));
        mServiceAroundList.add(new ServiceAround("https://pix10.agoda.net/hotelImages/564/5642208/5642208_18081615590067496477.jpg?s=1024x768", "khách sạn"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_restaurent_bts:
                iChoose.onClick("food", getString(R.string.txt_label_restaurant));
                break;
            case R.id.layout_hotel_bts:
                iChoose.onClick("hotel", getString(R.string.txt_label_hotel));
                break;
            case R.id.layout_pharmacies_bts:
                iChoose.onClick("pharmacies", getString(R.string.txt_label_pharmacies));
                break;
            case R.id.layout_hospital_bts:
                iChoose.onClick("hospital", getString(R.string.txt_label_hospital));
                break;
            case R.id.layout_atm_bts:
                iChoose.onClick("atm", getString(R.string.txt_label_ATM));
                break;
            case R.id.layout_petroleum_bts:
                iChoose.onClick("petroleums", getString(R.string.txt_label_petroleum));
                break;
            case R.id.layout_grocery_bts:
                iChoose.onClick("grocery", getString(R.string.txt_label_grocery));
                break;
            case R.id.layout_touries_bts:
                iChoose.onClick("touries", getString(R.string.txt_label_tourist_destination));
                break;
        }
        dismiss();
    }

    public interface IChoose {
        void onChoose(ServiceAround serviceAround);

        void onClick(String nameService, String title);
    }


}
