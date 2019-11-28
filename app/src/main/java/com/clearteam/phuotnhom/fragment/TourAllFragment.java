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

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.TourAllAdapter;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.notification.APIService;
import com.clearteam.phuotnhom.notification.Client;
import com.clearteam.phuotnhom.notification.Data;
import com.clearteam.phuotnhom.notification.MyResponse;
import com.clearteam.phuotnhom.notification.Sender;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.TourGroupDetailActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    private String userId, keyAllUser;
    private TourMe mTourMe;
    private APIService apiService;
    boolean notify = false;
    Intent intent;

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

      //  Log.d("AAAA", userId);
        mapping(view);
        initRecyclerView();
        return view;
    }

    private void mapping(View view) {
        rcvTourAll = view.findViewById(R.id.rcvTourAll);
        TextView tvadd = view.findViewById(R.id.tv_add);
    }

    private void initRecyclerView() {
        mTourAllAdapter = new TourAllAdapter(list, getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rcvTourAll.setLayoutManager(manager);
        rcvTourAll.setAdapter(mTourAllAdapter);
        initData();
        intent = getActivity().getIntent();

        final String userid = "7rxs0ORn4QbcITYDHgiuTQxvaKj2";
        mTourAllAdapter.setClickDetailTourGroup(new TourAllAdapter.clickDetailTourGroup() {
            @Override
            public void onClickDetail(int position, TourMe response) {
                if (response.getTvAdd().equals("Nhóm của bạn")) {
                    Intent intent = new Intent(getActivity(), TourGroupDetailActivity.class);
                    intent.putExtra(Const.KEY_ID_1,keyAllUser);
                    startActivity(intent);


                } else {
                    Toast.makeText(getActivity(), response.getId(), Toast.LENGTH_SHORT).show();
                    notify = true;
                    String msg = "Muốn tham gia vào nhóm phượt của bạn";
                    if (!msg.equals("")) {
                        sendNotification(fuser.getUid(), userid, msg);
                    } else {
                        Toast.makeText(getActivity(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(getActivity(), "Đợi phê duyệt", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message) {

        //final String userid = intent.getStringExtra("userid");
        final String userid = "7rxs0ORn4QbcITYDHgiuTQxvaKj2";
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username + ": " + message, "new Messenger", userid);
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
                        if (dataSnapshot1.getKey().equals(userId)) {
                            mTourMe.setTvAdd("Nhóm của bạn");
                            keyAllUser = mTourMe.getKeyId();
                            Log.d("AAAA",keyAllUser);
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
        final String userid = intent.getStringExtra("userid");
        //  status("online");
        // final String userid = "eXHqjj7yt8f68Nj2VhQFXzVdErd2";
        currentUser(userid);
    }

    @Override
    public void onPause() {
        super.onPause();
        // reference.removeEventListener(seenListiener);
        //status("offline");
        currentUser("none");
    }
}
