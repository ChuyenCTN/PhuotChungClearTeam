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

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.ListStatusUserAdapter;
import com.clearteam.phuotnhom.model.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DialogServiceAroundMemberOnline extends BottomSheetDialogFragment {
    private List<User> userList;
    private ListStatusUserAdapter adapter;
    private RecyclerView mRecyclerView;
    private IChoose iChoose;
    private boolean isChat;
    private TextView tv_status;
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

    public DialogServiceAroundMemberOnline(List<User> userList, boolean isChat, IChoose iChoose) {
        this.userList = userList;
        this.iChoose = iChoose;
        this.isChat = isChat;
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
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_member, null);
        dialog.setContentView(contentView);

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundResource(R.drawable.bg_bottom_sheet);

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetCallback);
        }

        initData();
       tv_status = contentView.findViewById(R.id.tv_status);
        mRecyclerView = contentView.findViewById(R.id.rcv_service_around);
        LinearLayoutManager layoutManager = new LinearLayoutManager(contentView.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ListStatusUserAdapter(getContext(), userList, true);

        adapter.setiClickItem(new ListStatusUserAdapter.IClickItem() {
            @Override
            public void onLocationClick(int position, User user) {
                iChoose.onLocationClick(user);
                DialogServiceAroundMemberOnline.this.dismiss();
            }

            @Override
            public void onMessageClick(int position, User user) {
                iChoose.onMessageClick(user);
                DialogServiceAroundMemberOnline.this.dismiss();
            }
        });


        mRecyclerView.setAdapter(adapter);

    }

    private void initData() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getId().equals(firebaseUser.getUid()) && user.getStatus().equals("online")) {
                        userList.add(user);
                    }
                }
                adapter = new ListStatusUserAdapter(getContext(), userList, true);
                adapter.setiClickItem(new ListStatusUserAdapter.IClickItem() {
                    @Override
                    public void onLocationClick(int position, User user) {
                        iChoose.onLocationClick(user);
                        DialogServiceAroundMemberOnline.this.dismiss();
                    }

                    @Override
                    public void onMessageClick(int position, User user) {
                        iChoose.onMessageClick(user);
                        DialogServiceAroundMemberOnline.this.dismiss();
                    }
                });
                if (userList.size()==0){
                    tv_status.setVisibility(View.VISIBLE);
                }else {
                    tv_status.setVisibility(View.GONE);
                }
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public interface IChoose {
        void onLocationClick(User user);

        void onMessageClick(User user);


    }
}
