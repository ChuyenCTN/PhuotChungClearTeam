package com.clearteam.phuotnhom.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.ui.ChangepassActivity;
import com.clearteam.phuotnhom.ui.EditInformationActivity;
import com.clearteam.phuotnhom.ui.TourGroupDetailActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static ProfileFragment INSTANCE;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private List<User> userList;
    private TextView tvName, tvEmail, tvNumberPhone, tvAddress, tv_number_phone_relatives, sex;
    private ImageView imgAvata;
    public Intent intent;
    private LinearLayout llChangePass, llEdit;


    public static ProfileFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileFragment();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(view);
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    User user = dataSnapshot.getValue(User.class);
                    intent = new Intent(getActivity(), EditInformationActivity.class);
                    tvName.setText(user.getUsername());
                    tvEmail.setText(user.getEmail());
                    tvAddress.setText(user.getAddress());
                    tvNumberPhone.setText(user.getNumberPhone());
                    tv_number_phone_relatives.setText(user.getNumberPhoneRelatives());
                    sex.setText(user.getSex());

                    if (user.getImageURL().equals("default")) {
                        imgAvata.setImageResource(R.drawable.avatar);
                    } else {
                        Glide.with(getContext()).load(user.getImageURL()).into(imgAvata);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Const.KEY_USER, user);
                    intent.putExtras(bundle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void initView(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvAddress = view.findViewById(R.id.tv_address);
        tvNumberPhone = view.findViewById(R.id.tv_number_phone);
        tv_number_phone_relatives = view.findViewById(R.id.tv_number_phone_relatives);
        sex = view.findViewById(R.id.sex);
        imgAvata = view.findViewById(R.id.img_avata);
        llChangePass = view.findViewById(R.id.ll_change_pass);
        llEdit = view.findViewById(R.id.ll_edit);

        llChangePass.setOnClickListener(this);
        llEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_pass:
                startActivity(new Intent(getActivity(), ChangepassActivity.class));
                break;
            case R.id.ll_edit:
                startActivity(intent);
                break;
        }
    }
}
