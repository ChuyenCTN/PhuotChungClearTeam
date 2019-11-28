package com.clearteam.phuotnhom.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.MessageAdapter;
import com.clearteam.phuotnhom.model.Messenger;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName;
    private ImageView imgAvata, imgSend, imgChiTiet, img_back;
    private EditText edTextSend;
    private String nameGroup, imageG, idGroup, saveCurrentTime, saveCurrentDate, msg;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private MessageAdapter messageAdapter;
    private RecyclerView rcvMess;
    private List<Messenger> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
        changeStatustBar();
        initView();

    }

    private void initView() {
        tvName = findViewById(R.id.tv_name);
        edTextSend = findViewById(R.id.edMsg);
        imgAvata = findViewById(R.id.imgAvata);
        imgSend = findViewById(R.id.img_send);
        imgChiTiet = findViewById(R.id.img_chitiet);
        img_back = findViewById(R.id.img_back);

        rcvMess = findViewById(R.id.rcv_message);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        rcvMess.setHasFixedSize(true);
        rcvMess.setLayoutManager(manager);

        img_back.setOnClickListener(this);
        imgChiTiet.setOnClickListener(this);
        imgSend.setOnClickListener(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        initData();

        readMessage(fuser.getUid(), idGroup, imageG);
//        reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child(idGroup);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                tvName.setText(user.getUsername());
//                if (user.getImageURL().equals("default")) {
//                    imgAvata.setImageResource(R.mipmap.ic_launcher);
//                } else {
//                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(imgAvata);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void initData() {
        Intent intent = getIntent();
        nameGroup = intent.getStringExtra(Const.KEY_NAME_GROUP);
        imageG = intent.getStringExtra(Const.KEY_IMAGE_GROUP);
        idGroup = intent.getStringExtra(Const.KEY_ID);

        tvName.setText(nameGroup);
        if (imageG == null) {
            Glide.with(this).load(R.drawable.avatar).into(imgAvata);
        } else {
            Glide.with(this).load(imageG).into(imgAvata);
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
            window.setStatusBarColor(ContextCompat.getColor(ChatGroupActivity.this, R.color.bg_tab));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_chitiet:

                break;
            case R.id.img_send:
                String msg1 = edTextSend.getText().toString();
                if (!msg1.equals("")) {
                    sendMessage(fuser.getUid(), idGroup, msg1, saveCurrentDate, saveCurrentTime);
                } else {
                    Toast.makeText(ChatGroupActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                edTextSend.setText("");

                break;
        }
    }

    private void sendMessage(String sender, final String receiver, String message, String date, String hour) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("hour", hour);
        hashMap.put("date", date);
        hashMap.put("isseen", false);

        reference.child(Const.KEY_TOUR).child("chatGroup").child(idGroup).push().setValue(hashMap);


        final DatabaseReference chatR = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(idGroup);
        chatR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatR.child("id").setValue(idGroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        final String msg = message;
//        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if (notify) {
//
//                    sendNotification(receiver, user.getUsername(), msg);
//                }
//                notify = false;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void readMessage(final String myid, final String userid, final String imageurl) {
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child("chatGroup").child(idGroup);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Messenger chat = snapshot.getValue(Messenger.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        chatList.add(chat);

                    }
                    messageAdapter = new MessageAdapter(ChatGroupActivity.this, chatList, imageurl);
                    rcvMess.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
