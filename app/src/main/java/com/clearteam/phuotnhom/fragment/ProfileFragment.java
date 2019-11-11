package com.clearteam.phuotnhom.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.ui.login.LoginActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {
    private static ProfileFragment INSTANCE;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private List<User> userList;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvNumberPhone;
    private TextView tvAddress;
    private ImageView imgAvata;
    private SharedPreferences mSharedPreferences;


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
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvAddress = view.findViewById(R.id.tv_address);
        tvNumberPhone = view.findViewById(R.id.tv_number_phone);
        imgAvata = view.findViewById(R.id.img_avata);
        auth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//         boolean isLogin = mSharedPreferences.getBoolean(Const.IS_LOGIN_TK, false);
//        if (isLogin == true) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (isAdded()) {
                        User user = dataSnapshot.getValue(User.class);

                        tvName.setText(user.getUsername());
                        tvEmail.setText(user.getEmail());
                        if (user.getImageURL().equals("default")) {
                            imgAvata.setImageResource(R.drawable.avatar);
                        } else {
                            Glide.with(getContext()).load(user.getImageURL()).into(imgAvata);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//        } else {
//           mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//                @Override
//                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    if (user != null) {
//                        tvName.setText(user.getDisplayName());
//                        tvEmail.setText(user.getEmail());
//                        Glide.with(getContext()).load(user.getPhotoUrl()).into(imgAvata);
//                    }
//                }
//            };
//        }
        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(mAuthStateListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthStateListener != null) {
//            auth.removeAuthStateListener(mAuthStateListener);
//        }
//    }
}