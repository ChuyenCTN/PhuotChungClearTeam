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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.ListUserAdapter;
import com.clearteam.phuotnhom.listener.ClickDetailMember;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TourGroupDetailActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener, ClickDetailMember, View.OnClickListener {



    private TextView tvNameGroup;
    private ImageView imgAvataGroup, imgMenu,imgBack,imgMessage;
    private String nameGroup, imageG, addressStart, addressEnd, dateStart, keyID, keyRemove;
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private String id, id2;
    private EditText edName, edAddressStart, edAddressEnd, edDateStart;
    private static DatePickerDialog.OnDateSetListener onDateSetListener1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private AlertDialog alertDialog;
    private ListUserAdapter adapter;
    private List<User> userList;
    private RecyclerView mRecyclerView;
    private List<String> keyRemoveMember = new ArrayList<>();
    private List<String> myListMember = new ArrayList<>();
    private List<String> userIds = new ArrayList<>();

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
        nameGroup = intent.getStringExtra(Const.KEY_NAME_GROUP);
        id2 = intent.getStringExtra(Const.KEY_ID);
        keyID = intent.getStringExtra(Const.KEY_ID_1);
        addressStart = intent.getStringExtra(Const.KEY_ADDRESS_START_GROUP);
        addressEnd = intent.getStringExtra(Const.KEY_ADDRESS_END_GROUP);
        dateStart = intent.getStringExtra(Const.KEY_DATE_GROUP);
        imageG = intent.getStringExtra(Const.KEY_IMAGE_GROUP);


        if (imageG == null) {
            Glide.with(this).load(R.drawable.avatar).into(imgAvataGroup);
        } else {
            Glide.with(this).load(imageG).into(imgAvataGroup);
        }
        tvNameGroup.setText(nameGroup);
        readUsers(keyID);
    }

    public void readUsers(String key) {
        keyID = key;
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
                adapter = new ListUserAdapter(TourGroupDetailActivity.this, userList, TourGroupDetailActivity.this);
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
        imgAvataGroup = findViewById(R.id.img_avata_group);
        imgMenu = findViewById(R.id.img_menu_group);

        onDateSetListener1 = this;
        imgBack.setOnClickListener(this);
        imgMessage.setOnClickListener(this);
        imgMenu.setOnClickListener(this);

        getData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_message:
                Toast.makeText(this, "show message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_menu_group:
                PopupMenu popupMenu = new PopupMenu(TourGroupDetailActivity.this, v);
                popupMenu.setOnMenuItemClickListener(TourGroupDetailActivity.this);
                popupMenu.inflate(R.menu.menu_tour_group);
                popupMenu.show();
                break;
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
            window.setStatusBarColor(ContextCompat.getColor(TourGroupDetailActivity.this, R.color.bg_tab));
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_user:
                Intent intent = new Intent(TourGroupDetailActivity.this, AddMemberActivity.class);
                intent.putExtra(Const.KEY_ID, id2);
                intent.putExtra(Const.KEY_ID_1, keyID);
                startActivityForResult(intent, Const.REQUEST_CODE);
                break;
            case R.id.edit_group:
                updateGroup();
                break;
            case R.id.delete_group:
                deleteGroup();
                break;
        }
        return true;
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
        intent.putExtra(Const.KEY_USER_ID,keyUserId);
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

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Const.KEY_TOUR).child(id).child(id2)
//        TourMe tourMe = new TourMe(name1,addressStart,addressEnd,date);
//        reference.setValue(tourMe);
//        notifyAll();
//        Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
//        finish();

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