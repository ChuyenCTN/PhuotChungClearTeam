 package com.clearteam.phuotnhom.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

 public class DetailNotify extends AppCompatActivity {
     private DatabaseReference reference;
     private FirebaseDatabase database = FirebaseDatabase.getInstance();
     private FirebaseAuth auth;
     private TextView tvName,tvMessage,tvDate;
     private Button btnConfirm,btnRefuse;
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

     private void getData() {
         Intent intent = getIntent();
         Bundle bundle = intent.getExtras();
         TourMe tour = ((TourMe) bundle.getSerializable(Const.KEY_NOTIFY));

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
     private void iniView() {
        tvName = findViewById(R.id.tv_name);
        tvMessage = findViewById(R.id.tv_message);
        tvDate = findViewById(R.id.tv_date);
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
}
