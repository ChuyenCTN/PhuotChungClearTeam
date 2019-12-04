package com.clearteam.phuotnhom.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.NotifyAdapter;
import com.clearteam.phuotnhom.model.Notifi;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.ui.DetailNotify;
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


public class NotifyFragment extends Fragment {
    private List<Notifi> list;
    private NotifyAdapter adapter;
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private Notifi notifi;
    FirebaseUser firebaseUser;
    private String userId;
    private static NotifyFragment INSTANCE;

    public static NotifyFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NotifyFragment();
        }
        return INSTANCE;
    }

    public NotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        list = new ArrayList<>();
        adapter = new NotifyAdapter(list, getContext());
        recyclerView = view.findViewById(R.id.rcv_list_notify);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();

        initData();
        adapter.setClickDetailTourGroup(new NotifyAdapter.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, Notifi response) {
                Intent intent = new Intent(getActivity(), DetailNotify.class);
                intent.putExtra(Const.KEY_NOTIFYCATION,response);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initData() {

        reference = database.getReference().child("Notify");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    notifi = dataSnapshot1.getValue(Notifi.class);

                    if (notifi.getReceiver().equals(userId)) {
                        list.add(notifi);
                    }
                }
                adapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
