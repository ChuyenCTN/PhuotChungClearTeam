 package com.clearteam.phuotnhom.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.utils.Const;

 public class DetailNotify extends AppCompatActivity {
     private TextView tv_user_sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notify);

        tv_user_sender = findViewById(R.id.tv_user_sender);
        Intent intent = getIntent();
        tv_user_sender.setText(intent.getStringExtra("userid"));
    }
}
