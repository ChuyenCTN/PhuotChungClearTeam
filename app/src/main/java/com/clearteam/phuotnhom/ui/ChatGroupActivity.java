package com.clearteam.phuotnhom.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.MessageAdapter;
import com.clearteam.phuotnhom.model.Messenger;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.utils.Const;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName;
    private ImageView imgAvata, imgChiTiet;
    private ImageButton img_back, img_image, imgSend;
    private EditText edTextSend;
    private String nameGroup, imageG, idMember, saveCurrentTime, saveCurrentDate, idSender, imgAvataSender, idUserGroup, nameSender, idGroup;
    private String checker = "", myUrl = "";
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private MessageAdapter messageAdapter;
    private RecyclerView rcvMess;
    private TourMe tour;
    private List<Messenger> chatList;
    private List<String> myListMember = new ArrayList<>();
    private StorageTask uploadTask;
    private Uri fileUri;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);
        changeStatustBar();
        initView();

    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tour = ((TourMe) bundle.getSerializable(Const.KEY_DATA));
        nameGroup = tour.getName();
        imageG = tour.getAvataGroup();
        idGroup = tour.getId();

        idUserGroup = tour.getUserGroupId();
        idMember = tour.getKeyId();
        tvName.setText(nameGroup);
        if (imageG == null) {
            Glide.with(this).load(R.drawable.avatar).into(imgAvata);
        } else {
            Glide.with(this).load(imageG).into(imgAvata);
        }
    }

    private void initView() {
        tvName = findViewById(R.id.tv_name);
        edTextSend = findViewById(R.id.edMsg);
        imgAvata = findViewById(R.id.imgAvata);
        imgSend = findViewById(R.id.img_send);
        img_image = findViewById(R.id.img_image);
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
        img_image.setOnClickListener(this);

        loadingBar = new ProgressDialog(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        idSender = fuser.getUid();


        Log.d("AAAAAA", idSender + " / " + nameSender);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar.getTime());
        initData();


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        myListMember = Arrays.asList(idMember.split(","));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (user.getId().equals(idSender)) {
                        nameSender = user.getUsername();
                        imgAvataSender = user.getImageURL();

                    }
                    for (String temp : myListMember) {
                        if (!user.getId().equals(firebaseUser.getUid()) && temp.equals(user.getId())) {
                            Log.d("AAAAA", user.getId());
                            readMessage();
                        }
                    }
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
            case R.id.img_image:
                getSelectedFile();
                break;
            case R.id.img_send:
                String msg1 = edTextSend.getText().toString();
                if (!msg1.equals("")) {
                    sendMessage(idSender, idMember, msg1, saveCurrentDate, saveCurrentTime);
                } else {
                    Toast.makeText(ChatGroupActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                edTextSend.setText("");

                break;
        }
    }

    private void getSelectedFile() {
        CharSequence option[] = new CharSequence[]{"Images", "Location"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatGroupActivity.this);
        builder.setTitle("Chọn tập tin");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    checker = "image";
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Chọn ảnh"), 438);
                }
                if (i == 1) {
                    checker = "location";
                }
            }
        });
        builder.show();
    }


    private void sendMessage(String sender, final String receiver, String message, String date, String hour) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child(idUserGroup).child(idGroup);
        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("id", time);
        hashMap.put("nameSender", nameSender);
        hashMap.put("imgAvataSender", imgAvataSender);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", date);
        hashMap.put("type", "text");
        hashMap.put("hour", hour);
        hashMap.put("isseen", false);

        reference.child("ChatGroup").push().setValue(hashMap);


//        final DatabaseReference chatR = FirebaseDatabase.getInstance().getReference("Chatlist")
//                .child(fuser.getUid())
//                .child(idGroup);
//        chatR.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//                    chatR.child("id").setValue(idGroup);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void readMessage() {
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child(idUserGroup).child(idGroup).child("ChatGroup");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Messenger chat = snapshot.getValue(Messenger.class);
                    chatList.add(chat);
                    messageAdapter = new MessageAdapter(ChatGroupActivity.this, chatList);
                    rcvMess.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 438) {
            loadingBar.setTitle("Gửi ảnh");
            loadingBar.setMessage("Xin vui lòng chờ, chúng tôi đang gửi ảnh...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            fileUri = data.getData();
            if (!checker.equals("image")) {

            } else if (checker.equals("image")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image File");


                String imageName = System.currentTimeMillis() + ".png";
                StorageReference filePath = storageReference.child("Image File" + imageName);
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child(idUserGroup).child(idGroup);
                            HashMap<String, Object> hashMap = new HashMap<>();
//
                            hashMap.put("nameSender", nameSender);
                            hashMap.put("imgAvataSender", imgAvataSender);
                            hashMap.put("sender", idSender);
                            hashMap.put("receiver", idMember);
                            hashMap.put("message", myUrl);
                            hashMap.put("name", fileUri.getLastPathSegment());
                            hashMap.put("type", checker);
                            hashMap.put("date", saveCurrentDate);
                            hashMap.put("hour", saveCurrentTime);
                            hashMap.put("isseen", false);

                            reference.child("ChatGroup").push().setValue(hashMap);
                            loadingBar.dismiss();
                        }
                    }
                });

            } else {
                loadingBar.dismiss();
                Toast.makeText(this, "Không có ảnh được chọn, Lỗi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
