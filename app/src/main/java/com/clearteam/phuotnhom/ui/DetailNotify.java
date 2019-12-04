package com.clearteam.phuotnhom.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.NotifyAdapter;
import com.clearteam.phuotnhom.model.Notifi;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DetailNotify extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private TextView tvName, tvMessage, tvDate, tvStatus, tvHour;
    private ImageButton imgBack;
    private Button btnConfirm, btnRefuse;
    FirebaseUser firebaseUser;
    private String userId, idNotify, keyIDUser, id3, statusNotify;
    private Notifi notifi;
    private TourMe mTourMe;
    private List<String> userIds = new ArrayList<>();
    private List<String> keyMembers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notify);
        changeStatustBar();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        iniView();
        getData();
        initData();

    }

    private void iniView() {

        tvName = findViewById(R.id.tvNameNotify);
        tvMessage = findViewById(R.id.tv_message);
        tvDate = findViewById(R.id.tv_date);
        tvStatus = findViewById(R.id.tv_status);
        tvHour = findViewById(R.id.tv_hour);

        imgBack = findViewById(R.id.img_back);
        btnRefuse = findViewById(R.id.btn_refuse);
        btnConfirm = findViewById(R.id.btn_confirm);


        imgBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnRefuse.setOnClickListener(this);

    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            notifi = ((Notifi) intent.getSerializableExtra(Const.KEY_NOTIFYCATION));
            tvName.setText(notifi.getNameSender());
            tvMessage.setText(notifi.getMessage());
            tvDate.setText(notifi.getDate());
            tvStatus.setText(notifi.getStatus());
            tvHour.setText(notifi.getHour());
            idNotify = notifi.getId();
            statusNotify = notifi.getStatus();

            if (statusNotify.equals("Chưa phê duyệt")) {
                btnConfirm.setVisibility(View.VISIBLE);
                btnRefuse.setVisibility(View.VISIBLE);
            } else {
                btnConfirm.setVisibility(View.GONE);
                btnRefuse.setVisibility(View.GONE);
            }
        }
    }

    private void changeStatustBar() {
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(DetailNotify.this, R.color.bg_tab));
        }
    }

    private void initData() {
        reference = database.getReference(Const.KEY_TOUR).child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mTourMe = dataSnapshot1.getValue(TourMe.class);
                    keyIDUser = mTourMe.getKeyId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        reference = database.getReference().child("Notify");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Notifi notifi = dataSnapshot1.getValue(Notifi.class);
//                    if (keyUserSender.equals(notifi.getId())) {
//
//                        // reference.child("status").setValue(R.string.confirm_status);
//                    }
////                    if (notifi.getReceiver().equals(userId)) {
////                        tvName.setText(notifi.getNameSender());
////                        tvMessage.setText(notifi.getMessage());
////                        tvDate.setText(notifi.getDate());
////                        tvStatus.setText(notifi.getStatus());
////                        tvHour.setText(notifi.getHour());
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_confirm:
                clickConfirm();
                break;
            case R.id.btn_refuse:
                clickRefuse();
                 tvStatus.setText("Yêu cầu đã được từ chối");
                 setResult(RESULT_CANCELED);
                break;
        }
    }

    private void clickConfirm() {
        tvStatus.setText(R.string.confirm_status);
        btnConfirm.setVisibility(View.GONE);
        btnRefuse.setVisibility(View.GONE);
        getDataConfirm();
        updateDataConfirm();
        finish();
    }

    private void updateDataConfirm() {
        reference = database.getReference();
        reference.child("Notify").child(idNotify).child("status").setValue("Đồng ý yêu cầu");
    }

    private void getDataConfirm() {
        keyMembers = Arrays.asList(keyIDUser.split(","));
        userIds.add(notifi.getSender());
        userIds.addAll(keyMembers);
        id3 = TextUtils.join(",", userIds);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Groups").child(userId).child(notifi.getIdGroup()).child("keyId").setValue(id3).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(DetailNotify.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void clickRefuse() {
        tvStatus.setText(R.string.refuse_status);
        updateDataRefuse();
        btnConfirm.setVisibility(View.GONE);
        btnRefuse.setVisibility(View.GONE);
        finish();
    }

    private void updateDataRefuse() {
        reference = database.getReference();
        reference.child("Notify").child(idNotify).child("status").setValue("Từ chối yêu cầu");
    }

    private void status(String status) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

}
