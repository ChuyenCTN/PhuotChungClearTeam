package com.clearteam.phuotnhom.fragment.tourgroup.tourall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.fragment.tourgroup.tourme.TourMeFragment;
import com.clearteam.phuotnhom.fragment.tourgroup.tourme.adapter.TourMeAdapter;
import com.clearteam.phuotnhom.fragment.tourgroup.tourme.model.TourMe;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TourAllFragment extends Fragment {
    private RecyclerView rcvTourMe;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<TourMe> list;
    private TourMeAdapter tourMeAdapter;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private static TourAllFragment INSTANCE;

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
        View view = inflater.inflate(R.layout.fragment_tour_all, container, false);
        auth = FirebaseAuth.getInstance();

        mapping(view);
        initRecyclerView();
        return view;
    }

    private void mapping(View view) {
        rcvTourMe = view.findViewById(R.id.rcvTourAll);
        //tvCheck = view.findViewById(R.id.tv_check);
    }

    private void initRecyclerView() {
      //  SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
      //  id1 = sharedPref.getString(Const.KEY_ID, "");

        tourMeAdapter = new TourMeAdapter(list, getActivity());
        initData();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvTourMe.setLayoutManager(manager);
        rcvTourMe.setAdapter(tourMeAdapter);
        tourMeAdapter.setClickDetailTourGroup(new TourMeAdapter.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, TourMe response) {
                Toast.makeText(getContext(), "ôjojjsnadf", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(int adapterPosition, TourMe response) {

            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        reference = database.getReference(Const.KEY_TOUR);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    TourMe response = dataSnapshot1.getValue(TourMe.class);
                    list.add(response);
                }
//                if (list.size() == 0) {
//                    tvCheck.setVisibility(View.VISIBLE);
//                } else {
//                    tvCheck.setVisibility(View.GONE);
//                }
                tourMeAdapter.setData(list);
                tourMeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
