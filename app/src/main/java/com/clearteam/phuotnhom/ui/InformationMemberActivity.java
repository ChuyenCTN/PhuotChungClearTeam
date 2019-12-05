package com.clearteam.phuotnhom.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName, tvEmaill, tvAddress, tvNumberPhone;
    private CircleImageView imgAvata;
    private ImageView imgBack;
    private LinearLayout llLocation, llMessage;
    private String keyUserId;
    private DatabaseReference reference;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_member);
        changeStatustBar();
        initView();
        initData();
    }

    private void initView() {
        tvName = findViewById(R.id.tv_name);
        tvEmaill = findViewById(R.id.tv_email);
        tvAddress = findViewById(R.id.tv_address);
        tvNumberPhone = findViewById(R.id.tv_number_phone);
        imgAvata = findViewById(R.id.img_avata);
        llLocation = findViewById(R.id.ll_location);
        llMessage = findViewById(R.id.ll_message);
        imgBack = findViewById(R.id.img_back);

        imgBack.setOnClickListener(this);
        llLocation.setOnClickListener(this);
        llMessage.setOnClickListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        keyUserId = intent.getStringExtra(Const.KEY_USER_ID);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(keyUserId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                mUser = user;
                tvName.setText(user.getUsername());
                tvEmaill.setText(user.getEmail());
                tvAddress.setText(user.getAddress());
                tvNumberPhone.setText(user.getNumberPhone());
                if (user.getImageURL().equals("default")) {
                    Glide.with(getApplicationContext()).load(R.drawable.avatar).into(imgAvata);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(imgAvata);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void changeStatustBar() {
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(InformationMemberActivity.this, R.color.bg_tab));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_location:
                EventBus.getDefault().postSticky(mUser);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.ll_message:
                Toast.makeText(this, "show message", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
