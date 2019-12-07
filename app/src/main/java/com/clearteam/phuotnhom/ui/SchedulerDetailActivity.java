package com.clearteam.phuotnhom.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.AddressStopAdapter;
import com.clearteam.phuotnhom.model.AddressStop;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.utils.CommonUtils;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SchedulerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TourMe tour;
    private String nameGroup, addressStart, addressEnd, dateStart, imageG, keyIdGroup, id, idAddress;
    private TextView tvNameGroup, tvAddressStrart, tvAdressEnd, tvDate;
    private FirebaseAuth auth;
    private ImageButton imgBack;
    private Button btnAddStop;
    private EditText edStop;
    private RecyclerView recyclerView;
    private AddressStopAdapter adapter;
    private List<AddressStop> list;
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler_detail);
        initView();
        changeStatustBar();
        getData();
    }

    private void initView() {
        tvNameGroup = findViewById(R.id.tvNameGroup);
        tvAddressStrart = findViewById(R.id.tv_address_start);
        tvAdressEnd = findViewById(R.id.tv_address_end);
        tvDate = findViewById(R.id.tv_date);
        imgBack = findViewById(R.id.img_back);
        btnAddStop = findViewById(R.id.btn_add_stop);
        initData();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_list_address);
        adapter = new AddressStopAdapter(this, list);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        imgBack.setOnClickListener(this);
        btnAddStop.setOnClickListener(this);
        itemClick();
    }

    private void initData() {
        list = new ArrayList<>();
        reference = database.getReference().child("AddressStop");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    keyIdGroup = dataSnapshot1.getKey();
                    for (DataSnapshot dta1 : dataSnapshot1.getChildren()) {
                        AddressStop addressStop = dta1.getValue(AddressStop.class);
                        if (addressStop.getIdGroup().equals(tour.getId())) {
                            list.add(addressStop);
                        }
                    }
                }
                adapter.setData(list);
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
            window.setStatusBarColor(ContextCompat.getColor(SchedulerDetailActivity.this, R.color.bg_tab));
        }
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tour = ((TourMe) bundle.getSerializable(Const.KEY_DATA));
        nameGroup = tour.getName();
        idAddress = tour.getUserGroupId();
        addressStart = tour.getAddressStart();
        addressEnd = tour.getAddressEnd();
        dateStart = tour.getDate();
        imageG = tour.getAvataGroup();

        tvNameGroup.setText(nameGroup);
        tvAddressStrart.setText(addressStart);
        tvAdressEnd.setText(addressEnd);
        tvDate.setText(dateStart);


        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        id = firebaseUser.getUid();
    }

    private void itemClick() {
        adapter.setiClickItem(new AddressStopAdapter.IClickItem() {
            @Override
            public void onIClick(int position, AddressStop addressStop) {
                if (id.equals(tour.getUserGroupId())) {
                    editAddressStop(addressStop);
                } else {
                    Toast.makeText(SchedulerDetailActivity.this, "Bạn không có quyền được xóa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onILongClick(int position, AddressStop addressStop) {
                if (id.equals(tour.getUserGroupId())) {
                    deleteItem(addressStop);
                } else {
                    Toast.makeText(SchedulerDetailActivity.this, "Bạn không có quyền được xóa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteItem(AddressStop addressStop) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.item_dialog_delete, null);
        builder.setView(dialogView);
        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        TextView tv_status = dialogView.findViewById(R.id.tv_status);
        tv_title.setText("Xóa địa điểm dừng chân");
        tv_status.setText("Bạn có muốn xóa địa điểm này không ?");
        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final Button btnOk = dialogView.findViewById(R.id.btnOk);
        final AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AddressStop").child(tour.getUserGroupId()).child(addressStop.getId());
                reference.removeValue();
                Toast.makeText(SchedulerDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void editAddressStop(AddressStop addressStop) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.item_dialog_push_notify, null);
        builder.setView(dialogView);

        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        edStop = dialogView.findViewById(R.id.ed_title);
        EditText edContent = dialogView.findViewById(R.id.ed_content);
        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final Button btnOk = dialogView.findViewById(R.id.btnOk);

        edStop.setText(addressStop.getAddressStrop());
        tv_title.setText("Thay đổi địa chỉ dừng chân");
        edContent.setVisibility(View.GONE);
        edStop.setHint("Địa chỉ dừng chân");
        btnOk.setText("Lưu");
        AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(addressStop);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void updateData(AddressStop addressStop) {
        String address = edStop.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        mDatabaseRef.child("AddressStop").child(idAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (addressStop.getId().equals(dataSnapshot1.getKey())) {
                        dataSnapshot1.getRef().child("addressStrop").setValue(address);
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
            case R.id.btn_add_stop:
                if (id.equals(tour.getUserGroupId())) {
                    addStopPoint();
                } else {
                    Toast.makeText(this, "Bạn không có quyền thêm", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void addStopPoint() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.item_dialog_push_notify, null);
        builder.setView(dialogView);

        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        EditText edTitle = dialogView.findViewById(R.id.ed_title);
        EditText edContent = dialogView.findViewById(R.id.ed_content);
        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final Button btnOk = dialogView.findViewById(R.id.btnOk);

        tv_title.setText("Thêm điểm dừng chân");
        edContent.setVisibility(View.GONE);
        edTitle.setHint("Địa chỉ dừng chân");
        btnOk.setText("Thêm");
        AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressStop = edTitle.getText().toString().trim();
                if (addressStop.isEmpty()) {
                    Toast.makeText(SchedulerDetailActivity.this, "Địa chỉ dừng chân đang trống", Toast.LENGTH_SHORT).show();
                } else {
                    addData(addressStop);
                    alertDialog.dismiss();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void addData(String addressStop) {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH + 1));
        String date = String.valueOf(c.get(Calendar.DATE));
        String hour = String.valueOf(c.get(Calendar.HOUR));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String second = String.valueOf(c.get(Calendar.SECOND));
        String time = "" + year + "" + month + "" + date + "" + hour + "" + minute + "" + second + "";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AddressStop").child(tour.getUserGroupId()).child(time);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", time);
        hashMap.put("idGroup", tour.getId());
        hashMap.put("idUserGroup", tour.getUserGroupId());
        hashMap.put("addressStrop", addressStop);

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SchedulerDetailActivity.this, "Thêm thành công !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
