package com.clearteam.phuotnhom.ui.detailtourgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.fragment.TourMeFragment;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.ui.addmember.AddMemberActivity;
import com.clearteam.phuotnhom.ui.infomation.EditInformationActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TourGroupDetailActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener {

    private ImageButton imgBack;
    private TextView tvNameGroup;
    private ImageView imgAvataGroup, imgMenu;
    private String nameGroup, imageG, addressStart, addressEnd, dateStart;
    private DatabaseReference reference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private String id, id2;
    private EditText edName, edAddressStart, edAddressEnd, edDateStart;
    private static DatePickerDialog.OnDateSetListener onDateSetListener1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_group_detail);
        changeStatustBar();
        initView();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        id = firebaseUser.getUid();
        reference = database.getReference(Const.KEY_TOUR).child(id);
    }

    private void initView() {

        imgBack = findViewById(R.id.img_back);
        tvNameGroup = findViewById(R.id.tv_name_group);
        imgAvataGroup = findViewById(R.id.img_avata_group);
        imgMenu = findViewById(R.id.img_menu_group);
        onDateSetListener1 = this;
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TourGroupDetailActivity.this, v);
                popupMenu.setOnMenuItemClickListener(TourGroupDetailActivity.this);
                popupMenu.inflate(R.menu.menu_tour_group);
                popupMenu.show();
            }
        });
        getData();
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

    private void getData() {
        Intent intent = getIntent();
        nameGroup = intent.getStringExtra(Const.KEY_NAME_GROUP);
        id2 = intent.getStringExtra(Const.KEY_ID);
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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_user:
                startActivity(new Intent(TourGroupDetailActivity.this, AddMemberActivity.class));
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
    protected void onPause() {
        super.onPause();
        if(isFinishing()){
            if (alertDialog!= null) {
                alertDialog.dismiss();
                alertDialog= null;
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
