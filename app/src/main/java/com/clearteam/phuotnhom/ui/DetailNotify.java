package com.clearteam.phuotnhom.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.NotifyAdapter;
import com.clearteam.phuotnhom.model.Notifi;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
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

public class DetailNotify extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private TextView tvName, tvMessage, tvDate, tvStatus;
    private ImageButton imgBack;
    private Button btnConfirm, btnRefuse;
    FirebaseUser firebaseUser;
    private String userId;

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
        initData();

        getData();

    }

    private void iniView() {
        tvName = findViewById(R.id.tvNameNotify);
        tvMessage = findViewById(R.id.tv_message);
        tvDate = findViewById(R.id.tv_date);
        tvStatus = findViewById(R.id.tv_status);
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
            Notifi notifi = ((Notifi) intent.getSerializableExtra(Const.KEY_NOTIFYCATION));
            tvName.setText(notifi.getNameSender());
            tvMessage.setText(notifi.getMessage());
            tvDate.setText(notifi.getDate());
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

        reference = database.getReference().child("Notify");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Notifi notifi = dataSnapshot1.getValue(Notifi.class);

                    if (notifi.getReceiver().equals(userId)) {
                        tvName.setText(notifi.getNameSender());
                        tvMessage.setText(notifi.getMessage());
                        tvDate.setText(notifi.getDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_confirm:
                tvStatus.setText("Yêu cầu đã được đồng chối");

                break;
            case R.id.btn_refuse:
                 tvStatus.setText("Yêu cầu đã được từ chối");
                 setResult(RESULT_CANCELED);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
