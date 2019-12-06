package com.clearteam.phuotnhom.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.TourAllAdapter;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.notification.APIService;
import com.clearteam.phuotnhom.notification.Client;
import com.clearteam.phuotnhom.notification.Data;
import com.clearteam.phuotnhom.notification.MyResponse;
import com.clearteam.phuotnhom.notification.Sender;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.RegisterActivity;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class TourAllFragment extends Fragment {
    private RecyclerView rcvTourAll;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<TourMe> list;
    private TourAllAdapter mTourAllAdapter;
    private FirebaseAuth auth;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private static TourAllFragment INSTANCE;
    private View view;
    private String userId, keyAllUser, keyUserGroupId, saveCurrentDate, nameSender, saveCurrentTime, time, keyIDGroup,msg;
    private TourMe mTourMe;
    private APIService apiService;
    boolean notify = false;
    Intent intent;
    List<String> listUserIds;

    public static TourAllFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TourAllFragment();
        }
        return INSTANCE;
    }

    public TourAllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tour_all, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        mapping(view);
        initRecyclerView();
        getNameSender();
        return view;
    }

    private void mapping(View view) {
        rcvTourAll = view.findViewById(R.id.rcvTourAll);

    }

    private void initRecyclerView() {
        mTourAllAdapter = new TourAllAdapter(list, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvTourAll.setLayoutManager(manager);
        rcvTourAll.setAdapter(mTourAllAdapter);
        initData();
        creatKeyIDNotify();
        creatDateHourNotify();

        //final String userid = "7rxs0ORn4QbcITYDHgiuTQxvaKj2";
        mTourAllAdapter.setClickDetailTourGroup(new TourAllAdapter.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, TourMe response) {

                if (response.getTvAdd().equals("Nhóm của bạn") || response.getTvAdd().equals("Bạn đã tham gia")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Const.KEY_DATA, response);
                    Intent intent = new Intent(getActivity(), TourGroupDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {
                    notify = true;
                    msg = "Muốn tham gia vào nhóm phượt của bạn";

                    keyUserGroupId = response.getUserGroupId();
                    keyIDGroup = response.getId();
                    if (!msg.equals("")) {
                        sendMessage(fuser.getUid(), keyUserGroupId, msg);
                    } else {
                        Toast.makeText(getActivity(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(), "Đợi phê duyệt", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void creatDateHourNotify() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar.getTime());
        intent = getActivity().getIntent();
    }

    private void creatKeyIDNotify() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH + 1));
        String date = String.valueOf(c.get(Calendar.DATE));
        String hour = String.valueOf(c.get(Calendar.HOUR));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String second = String.valueOf(c.get(Calendar.SECOND));
        time = "" + year + "" + month + "" + date + "" + hour + "" + minute + "" + second + "";
    }

    private void getNameSender() {
        auth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                nameSender = user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public JSONObject getDataJson() {

        try {
            JSONObject user = new JSONObject();
            user.put("id", time);
            user.put("idGroup", keyIDGroup);
            user.put("sender", fuser.getUid());
            user.put("receiver", keyUserGroupId);
            user.put("message", msg);
            user.put("date", saveCurrentDate);
            user.put("hour", saveCurrentTime);
            user.put("nameSender", nameSender);
            user.put("status", "Chưa phê duyệt");

            return user;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(String sender, final String receiver, String message) {
        // DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Notify").child(time);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", time);
        hashMap.put("idGroup", keyIDGroup);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("hour", saveCurrentTime);
        hashMap.put("nameSender", nameSender);
        hashMap.put("status", "Chưa phê duyệt");




        //mReference.child("Notify").push().setValue(hashMap);
        mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //  Toast.makeText(getActivity(), "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final DatabaseReference chatR = FirebaseDatabase.getInstance().getReference("ListNotify")
                .child(fuser.getUid())
                .child(keyUserGroupId);

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
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {

                    sendNotification(user.getUsername(), receiver, msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String username, String receiver, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.drawable.logo_group, username + ": " + message, "Thông báo mới",keyUserGroupId,getDataJson().toString());
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotifycation(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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

    private void initData() {
        list = new ArrayList<>();
        reference = database.getReference().child(Const.KEY_TOUR);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dta1 : dataSnapshot1.getChildren()) {
                        mTourMe = dta1.getValue(TourMe.class);
                        list.add(mTourMe);
                        keyAllUser = mTourMe.getKeyId();

                        if (keyAllUser != null) {
                            listUserIds = new ArrayList<>();
                            listUserIds = Arrays.asList(keyAllUser.split(","));
                        }
                        if (dataSnapshot1.getKey().equals(userId)) {
                            mTourMe.setTvAdd("Nhóm của bạn");
                            mTourMe.setMyTour(true);

                        } else if (listUserIds.contains(userId) == true) {
                            mTourMe.setTvAdd("Bạn đã tham gia");
                            mTourMe.setMyTour(true);

                        } else {
                            mTourMe.setTvAdd("Đăng ký tham gia");
                            mTourMe.setMyTour(false);
                        }
                    }
                }
                mTourAllAdapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }


    @Override
    public void onResume() {
        super.onResume();
        currentUser(keyUserGroupId);
    }

    @Override
    public void onPause() {
        super.onPause();
        // reference.removeEventListener(seenListiener);
        currentUser("none");
    }
}
