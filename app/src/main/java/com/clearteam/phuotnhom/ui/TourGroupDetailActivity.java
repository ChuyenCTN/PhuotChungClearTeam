package com.clearteam.phuotnhom.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.ListUserAdapter;
import com.clearteam.phuotnhom.listener.ClickDetailMember;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.notification.APIService;
import com.clearteam.phuotnhom.notification.Client;
import com.clearteam.phuotnhom.notification.Data;
import com.clearteam.phuotnhom.notification.MyResponse;
import com.clearteam.phuotnhom.notification.Sender;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourGroupDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ClickDetailMember, View.OnClickListener {

    private TextView tvNameGroup;
    private ImageView imgAvataGroup, imgMenu, imgBack, imgMessage;
    //<<<<<<< HEAD
//    private String nameGroup, imageG, addressStart, addressEnd, dateStart, keyID, keyRemove, title, content;
//=======
    private String nameGroup, imageG, addressStart, addressEnd, dateStart;
    private String keyID, keyRemove, title, content, idUserGroup, time, nameSender, saveCurrentDate, saveCurrentTime;

    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private String id, id2;
    private LinearLayout llAddMember, llDelete, llNotify, llEdit, ll_location;
    private EditText edName, edAddressStart, edAddressEnd, edDateStart, edTitle, edContent;
    private static DatePickerDialog.OnDateSetListener onDateSetListener1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private APIService apiService;
    private AlertDialog alertDialog;
    private ListUserAdapter adapter;
    private List<User> userList;
    boolean notify = false;
    private TourMe tour;
    private RecyclerView mRecyclerView;
    private List<String> keyRemoveMember = new ArrayList<>();
    private List<String> myListMember = new ArrayList<>();
    private List<String> myListMember1 = new ArrayList<>();
    private List<String> listUpdateMember = new ArrayList<>();
    private List<String> listUserIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_group_detail);
        changeStatustBar();
        initView();
        initData();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        id = firebaseUser.getUid();
        reference = database.getReference(Const.KEY_TOUR).child(id);
    }

    private void initData() {
        mRecyclerView = findViewById(R.id.rcv_list_user);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        userList = new ArrayList<>();
        mRecyclerView.setAdapter(adapter);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tour = ((TourMe) bundle.getSerializable(Const.KEY_DATA));
        nameGroup = tour.getName();
        id2 = tour.getId();
        idUserGroup = tour.getUserGroupId();

        listUpdateMember = Arrays.asList(tour.getKeyId().split(","));
        if (listUpdateMember.contains(idUserGroup)) {
            keyID = tour.getKeyId();
        } else {
            keyID = tour.getKeyId() + "," + tour.getUserGroupId();
        }

        addressStart = tour.getAddressStart();
        addressEnd = tour.getAddressEnd();
        dateStart = tour.getDate();
        imageG = tour.getAvataGroup();

        if (imageG == null) {
            Glide.with(this).load(R.drawable.logo_group).into(imgAvataGroup);
        } else {
            Glide.with(this).load(imageG).into(imgAvataGroup);
        }
        tvNameGroup.setText(nameGroup);
        readUsers(keyID);
    }

    public void readUsers(String key) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        myListMember = Arrays.asList(key.split(","));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;

                    for (String temp : myListMember) {
                        if (!user.getId().equals(firebaseUser.getUid()) && temp.equals(user.getId())) {
                            userList.add(user);
                        }
                    }
                }
                adapter = new ListUserAdapter(TourGroupDetailActivity.this, userList, TourGroupDetailActivity.this, true, tour.getUserGroupId());
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {

        imgBack = findViewById(R.id.img_back);
        imgMessage = findViewById(R.id.img_message);
        tvNameGroup = findViewById(R.id.tv_name_group);
        llAddMember = findViewById(R.id.ll_add_member);
        llDelete = findViewById(R.id.ll_delete);
        llNotify = findViewById(R.id.ll_notify);
        ll_location = findViewById(R.id.ll_location);
        llEdit = findViewById(R.id.ll_edit);
        imgAvataGroup = findViewById(R.id.img_avata_group);
        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        onDateSetListener1 = this;
        imgBack.setOnClickListener(this);
        imgMessage.setOnClickListener(this);
        llEdit.setOnClickListener(this);
        llNotify.setOnClickListener(this);
        ll_location.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llAddMember.setOnClickListener(this);
        getData();
        creatDateHourNotify();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_message:
                Intent intent = new Intent(TourGroupDetailActivity.this, ChatGroupActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra(Const.TYPE, Const.TYPE_CHAT);
                bundle.putSerializable(Const.KEY_DATA, tour);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_add_member:
                Intent intent1 = new Intent(TourGroupDetailActivity.this, AddMemberActivity.class);
                intent1.putExtra(Const.KEY_ID, id2);
                intent1.putExtra(Const.KEY_ID_1, keyID);
                startActivityForResult(intent1, Const.REQUEST_CODE);
                break;
            case R.id.ll_notify:
                if (id.equals(tour.getUserGroupId())) {
                    pushNotifyForMember();
                } else {
                    Toast.makeText(this, "Bạn không có quyền sửa", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_edit:
                if (id.equals(tour.getUserGroupId())) {
                    updateGroup();
                } else {
                    Toast.makeText(this, "Bạn không có quyền sửa", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_location:
                if (userList != null) {
                    EventBus.getDefault().postSticky(userList);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                break;
            case R.id.ll_delete:
                if (id.equals(tour.getUserGroupId())) {
                    deleteGroup();
                } else {
                    Toast.makeText(this, "Bạn không có xóa", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void creatDateHourNotify() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }

    private void pushNotifyForMember() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo cho các thành viên");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.item_dialog_push_notify, null);
        builder.setView(dialogView);

        edTitle = dialogView.findViewById(R.id.ed_title);
        edContent = dialogView.findViewById(R.id.ed_content);
        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final Button btnOk = dialogView.findViewById(R.id.btnOk);
        alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNotify();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void SendNotify() {
        title = edTitle.getText().toString().trim();
        content = edContent.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Tiêu đề đang trống", Toast.LENGTH_SHORT).show();
        } else if (content.isEmpty()) {
            Toast.makeText(this, "Nội dung đang trống", Toast.LENGTH_SHORT).show();
        } else {

            sendMessage(id, keyID, content);
        }
    }

    public void sendMessage(String sender, final String receiver, String message) {
        // DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Notify").child(id2);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id2);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);


        myListMember1 = Arrays.asList(keyID.split("," + tour.getUserGroupId()));
        for (String temp : myListMember1) {
            listUserIds = Arrays.asList(temp.split(","));
            for (String idMember : listUserIds) {
                notify = true;
                sendMessage(auth.getUid(), idMember, content, title);
            }

        }

    }


    public void sendMessage(String sender, String receiver, String message, String title) {

        final DatabaseReference chatR = FirebaseDatabase.getInstance().getReference("ListNotify")
                .child(auth.getUid())
                .child(receiver);

        chatR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatR.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                sendNotification(user.getUsername(), receiver, msg, title);
                nameSender = user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pushNotify(sender, receiver, message, title);
    }

    public JSONObject getDataJson() {
        try {
            JSONObject user = new JSONObject();
            user.put("title", title);
            user.put("content", content);
            user.put("nameSender", nameSender);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void pushNotify(String sender, String receiver, String message, String title) {
        time = String.valueOf(new Random().nextInt(1000000000));

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Notify").child(time);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", time);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("hour", saveCurrentTime);
        hashMap.put("nameSender", nameSender);
        hashMap.put("status", title);

        mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(TourGroupDetailActivity.this, "Thông báo thành công !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendNotification(final String username, String receiver,
                                  final String message, String title) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(id, R.drawable.logo_group, username + ": " + message, title, receiver, getDataJson().toString());
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotifycation(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(TourGroupDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
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
            window.setStatusBarColor(ContextCompat.getColor(TourGroupDetailActivity.this, R.color.bg_tab));
        }
    }


    private void updateGroup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tạo nhóm");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.item_dialog_add_tour_me, null);
        builder.setView(dialogView);

        edName = dialogView.findViewById(R.id.edNameGroup);
        edAddressStart = dialogView.findViewById(R.id.edStart);
        edAddressEnd = dialogView.findViewById(R.id.edEnd);
        edDateStart = dialogView.findViewById(R.id.edDate);
        ImageView img = dialogView.findViewById(R.id.imgDate);

        edName.setText(nameGroup);
        edAddressStart.setText(addressStart);
        edAddressEnd.setText(addressEnd);
        edDateStart.setText(dateStart);

        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final Button btnOk = dialogView.findViewById(R.id.btnOk);
        alertDialog = builder.create();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(v);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        setDate(cal);
    }

    private void setDate(final Calendar calendar) {
        edDateStart.setText(sdf.format(calendar.getTime()));
    }

    public void datePicker(View view) {
        DatePickerFragment fragment1 = new DatePickerFragment();
        fragment1.show(getSupportFragmentManager(), "date");
    }

    @Override
    public void onClickDetail(int position, User user) {
        String keyUserId = user.getId();
        Intent intent = new Intent(TourGroupDetailActivity.this, InformationMemberActivity.class);
        intent.putExtra(Const.KEY_USER_ID, keyUserId);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int adapterPosition, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa nhóm");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                keyRemoveMember = Arrays.asList(keyID.split(user.getId()));
                keyRemove = TextUtils.join(",", keyRemoveMember);
                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Groups").child(id).child(id2).child("keyId").setValue(keyRemove).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            readUsers(keyRemove);
                            Toast.makeText(TourGroupDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }


    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),
                    onDateSetListener1, year, month, day);
        }
    }

    private void updateData() {
        final String name1 = edName.getText().toString();
        final String addressStart = edAddressStart.getText().toString();
        final String addressEnd = edAddressEnd.getText().toString();
        final String date = edDateStart.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Const.KEY_TOUR);
        Query query = reference.child(id).orderByChild("name").equalTo(name1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    dataSnapshot1.getRef().child("name").setValue(name1);
                    dataSnapshot1.getRef().child("addressStart").setValue(addressStart);
                    dataSnapshot1.getRef().child("addressEnd").setValue(addressEnd);
                    dataSnapshot1.getRef().child("date").setValue(date);
                }
                Toast.makeText(TourGroupDetailActivity.this, "sửa thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TourGroupDetailActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa nhóm");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child(id).child(id2);
                reference.removeValue();
                finish();
                Toast.makeText(TourGroupDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String key = data.getData().toString();
                readUsers(key);
                adapter.notifyDataSetChanged();

            }
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            if (alertDialog != null) {
                alertDialog.dismiss();
                alertDialog = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}


