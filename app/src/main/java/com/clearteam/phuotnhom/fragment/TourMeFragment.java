package com.clearteam.phuotnhom.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.TourMeAdapter;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.ui.TourGroupDetailActivity;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TourMeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    private RecyclerView rcvTourMe;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    EditText edName, edAddressStart, edAddressEnd, edDateStart;
    private static DatePickerDialog.OnDateSetListener onDateSetListener1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String id, time;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<TourMe> list;
    private TourMe mTourMe;
    private TourMeAdapter tourMeAdapter;
    private TextView tvCheck;
    private View view;
    private static TourMeFragment INSTANCE;


    public static TourMeFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TourMeFragment();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_tour_me, container, false);
            onDateSetListener1 = this;

            auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getCurrentUser();
            assert firebaseUser != null;
            id = firebaseUser.getUid();
            mapping(view);
            initRecyclerView();
        }
        return view;
    }

    private void mapping(View view) {
        rcvTourMe = view.findViewById(R.id.rcv_tour_me);
        tvCheck = view.findViewById(R.id.tv_check);
    }

    private void initRecyclerView() {
        tourMeAdapter = new TourMeAdapter(list, getActivity());
        initData();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvTourMe.setLayoutManager(manager);
        rcvTourMe.setHasFixedSize(true);
        rcvTourMe.setAdapter(tourMeAdapter);
        tourMeAdapter.setClickDetailTourGroup(new TourMeAdapter.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, TourMe tourMe) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Const.KEY_DATA, tourMe);
                Intent intent = new Intent(getActivity(), TourGroupDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                startActivity(intent);
            }

            @Override
            public void onLongClick(int adapterPosition, TourMe response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa nhóm");
                builder.setMessage("Bạn có muốn xóa không ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child(response.getId()).removeValue();
                        tourMeAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
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

        });
    }

    private void initData() {
        reference = database.getReference(Const.KEY_TOUR).child(id);
        list = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mTourMe = dataSnapshot1.getValue(TourMe.class);
                    list.add(mTourMe);
                }
                if (list.size() == 0) {
                    tvCheck.setVisibility(View.VISIBLE);
                } else {
                    tvCheck.setVisibility(View.GONE);
                }
                tourMeAdapter.setData(list);
                tourMeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_group:
                showDialogCreat();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogCreat() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tạo nhóm");
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.item_dialog_add_tour_me, null);
        builder.setView(dialogView);

        edName = dialogView.findViewById(R.id.edNameGroup);
        edAddressStart = dialogView.findViewById(R.id.edStart);
        edAddressEnd = dialogView.findViewById(R.id.edEnd);
        edDateStart = dialogView.findViewById(R.id.edDate);
        ImageView img = dialogView.findViewById(R.id.imgDate);
        final Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final Button btnOk = dialogView.findViewById(R.id.btnOk);
        final AlertDialog alertDialog = builder.create();

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
                String nameGoup = edName.getText().toString().trim();
                String addressStart = edAddressStart.getText().toString().trim();
                String addressEnd = edAddressEnd.getText().toString().trim();
                String dateStart = edDateStart.getText().toString().trim();

                Calendar c = Calendar.getInstance();
                String year = String.valueOf(c.get(Calendar.YEAR));
                String month = String.valueOf(c.get(Calendar.MONTH + 1));
                String date = String.valueOf(c.get(Calendar.DATE));
                String hour = String.valueOf(c.get(Calendar.HOUR));
                String minute = String.valueOf(c.get(Calendar.MINUTE));
                String second = String.valueOf(c.get(Calendar.SECOND));
                time = "" + year + "" + month + "" + date + "" + hour + "" + minute + "" + second + "";


                if (nameGoup.isEmpty() || addressEnd.isEmpty() || addressStart.isEmpty() || addressStart.isEmpty() || dateStart.isEmpty()) {
                    Toast.makeText(getActivity(), "Chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    createGroup(nameGoup, addressStart, addressEnd, dateStart);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }

    private void createGroup(String nameGoup, String addressStart, String addressEnd, String dateStart) {

        TourMe tourMe = new TourMe();
        tourMe.setId(time);
        tourMe.setName(nameGoup);
        tourMe.setAddressStart(addressStart);
        tourMe.setAddressEnd(addressEnd);
        tourMe.setDate(dateStart);
        tourMe.setToken(FirebaseInstanceId.getInstance().getToken());
        tourMe.setKeyId("");
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Groups").child(id).child(time).setValue(tourMe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        TourMeFragment.DatePickerFragment fragment1 = new DatePickerFragment();
        fragment1.show(getFragmentManager(), "date");
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

}
