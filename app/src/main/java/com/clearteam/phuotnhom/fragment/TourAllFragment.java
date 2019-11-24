package com.clearteam.phuotnhom.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.TourAllAdapter;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.ui.TourGroupDetailActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TourAllFragment extends Fragment {
    private RecyclerView rcvTourAll;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<TourMe> list;
    private TourAllAdapter mTourAllAdapter;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private static TourAllFragment INSTANCE;
    private View view;
    private String userId;
    private TourMe mTourMe;

    public static TourAllFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TourAllFragment();
        }
        return INSTANCE;
    }

    public TourAllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tour_all, container, false);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        mapping(view);
        initRecyclerView();
        return view;
    }

    private void mapping(View view) {
        rcvTourAll = view.findViewById(R.id.rcvTourAll);
        TextView tvadd = view.findViewById(R.id.tv_add);
        //tvCheck = view.findViewById(R.id.tv_check);
    }

    private void initRecyclerView() {
        //  SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //  id1 = sharedPref.getString(Const.KEY_ID, "");

        mTourAllAdapter = new TourAllAdapter(list, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvTourAll.setLayoutManager(manager);
        rcvTourAll.setAdapter(mTourAllAdapter);
        initData();
        mTourAllAdapter.setClickDetailTourGroup(new TourAllAdapter.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, TourMe response) {
                if (response.getTvAdd().equals("Nhóm của bạn")){
                    startActivity(new Intent(getActivity(), TourGroupDetailActivity.class));
                 //   Toast.makeText(getActivity(), "nhóm của bạn", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Đợi phê duyệt", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        if (dataSnapshot1.getKey().equals(userId)) {
                            mTourMe.setTvAdd("Nhóm của bạn");
                            mTourMe.setMyTour(true);
                        }else {
                            mTourMe.setTvAdd("Đăng ký tham gia");
                            mTourMe.setMyTour(false);
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
