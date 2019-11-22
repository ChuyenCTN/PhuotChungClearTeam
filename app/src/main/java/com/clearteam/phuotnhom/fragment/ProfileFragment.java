package com.clearteam.phuotnhom.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.ui.ChangepassActivity;
import com.clearteam.phuotnhom.ui.EditInformationActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


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
    public Intent intent;


    public static ProfileFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProfileFragment();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    User user = dataSnapshot.getValue(User.class);
                    intent = new Intent(getActivity(), EditInformationActivity.class);
                    tvName.setText(user.getUsername());
                    tvEmail.setText(user.getEmail());
//                    tvAddress.setText(user.getAddress());
//                    tvNumberPhone.setText(user.getNumberPhone());
//                    if (user.getAddress().equals("chưa có thông tin")||user.getNumberPhone().equals("chưa có thông tin")){
//                        tvAddress.setText("");
//                        tvNumberPhone.setText("");
//                    }else{
                        tvAddress.setText(user.getAddress());
                        tvNumberPhone.setText(user.getNumberPhone());

//                    }
                    if (user.getImageURL().equals("default")) {
                        imgAvata.setImageResource(R.drawable.avatar);
                    } else {
                        Glide.with(getContext()).load(user.getImageURL()).into(imgAvata);
                    }

                    String name = tvName.getText().toString();
                    String email = tvEmail.getText().toString();
                    String address = tvAddress.getText().toString();
                    String number = tvNumberPhone.getText().toString();
                    String img = user.getImageURL();

                    intent.putExtra(Const.KEY_NAME, name);
                    intent.putExtra(Const.KEY_EMAIL, email);
                    intent.putExtra(Const.KEY_ADDRESS, address);
                    intent.putExtra(Const.KEY_NUMBER, number);
                    intent.putExtra(Const.KEY_IMAGER, img);

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

//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        property.clear();
//        List<String> propertyKeys = new ArrayList<>();
//        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
//            for (DataSnapshot propertySnapshot : userSnapshot.getChildren()){
//                propertyKeys.add(propertySnapshot.getKey());
//                Advertise advertise = propertySnapshot.getValue(Advertise.class);
//                property.add(advertise);
//            }
//        }
//        dataStatus.DataIsLoaded(property, propertyKeys);
//    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_user:
                startActivity(intent);
                break;
            case R.id.change_pass:
                startActivity(new Intent(getActivity(), ChangepassActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
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
