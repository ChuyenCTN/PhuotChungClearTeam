package com.clearteam.phuotnhom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.TourAllAdapter;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchedulerFragment extends Fragment {
    private RecyclerView rcvTourMe;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String keyAllUser, id;
    private TextView tvCheck;
    private List<TourMe> list;
    private List<String> listUserIds;
    private TourMe mTourMe;
    private TourAllAdapter mTourAllAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
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

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        id = firebaseUser.getUid();
        mapping(view);
        initRecyclerView();
        return view;
    }
    private void initRecyclerView() {
        mTourAllAdapter = new TourAllAdapter(list, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvTourMe.setLayoutManager(manager);
        rcvTourMe.setAdapter(mTourAllAdapter);
        initData();
    }

    private void mapping(View view) {
        rcvTourMe = view.findViewById(R.id.rcv_tour_me);
        tvCheck = view.findViewById(R.id.tv_check);
    }

    private void initData() {
        list = new ArrayList<>();
        reference = database.getReference().child(Const.KEY_TOUR);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dta1 : dataSnapshot1.getChildren()) {
                        mTourMe = dta1.getValue(TourMe.class);
                        list.add(mTourMe);
                        keyAllUser = mTourMe.getKeyId();
                        if (keyAllUser != null) {
                            listUserIds = new ArrayList<>();
                            listUserIds = Arrays.asList(keyAllUser.split(","));
                        }
                        if (dataSnapshot1.getKey().equals(id)) {
                            mTourMe.setTvAdd("Nhóm của bạn");
                            mTourMe.setMyTour(true);

                        } else if (listUserIds.contains(id) == true) {
                            mTourMe.setTvAdd("Bạn đã tham gia");
                            mTourMe.setMyTour(true);
                        }else {
                           list.remove(mTourMe);
                        }
                    }
                }
                mTourAllAdapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
