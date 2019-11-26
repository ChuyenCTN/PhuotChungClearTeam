
package com.clearteam.phuotnhom.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.adapter.UserAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddMemberActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<User> userList;
    private EditText edSeach;
    private UserAdapter adapter;
    private User user;
    private TextView tvOk;
    private ImageView imgBack;
    private String id, id2, id3, memberKeyIds;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private List<String> userIds = new ArrayList<>();
    private List<String> keyMembers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        changeStatustBar();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        id = firebaseUser.getUid();
        Intent intent = getIntent();
        id2 = intent.getStringExtra(Const.KEY_ID);
        memberKeyIds = intent.getStringExtra(Const.KEY_ID_1);
        initView();

    }

    private void initView() {
        edSeach = findViewById(R.id.ed_seach_user);
        tvOk = findViewById(R.id.tvOK);
        imgBack = findViewById(R.id.img_back);
        mRecyclerView = findViewById(R.id.rcv_member);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        userList = new ArrayList<>();
        readUsers();
        keyMembers = Arrays.asList(memberKeyIds.split(","));
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<User> stList = ((UserAdapter) adapter)
                        .getList();

                for (int i = 0; i < stList.size(); i++) {
                    User user2 = stList.get(i);

                    if (user2.isSelected() == true) {
                        userIds.add(user2.getId());
                    }
                }

                userIds.addAll(keyMembers);
                id3 = TextUtils.join(",", userIds);

                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Groups").child(id).child(id2).child("keyId").setValue(id3).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent data = new Intent();
                            data.setData(Uri.parse(id3));
                            setResult(RESULT_OK, data);
                            finish();
                            Toast.makeText(AddMemberActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void searchUsers(final String toString) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search").
                startAt(toString)
                .endAt(toString + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);

                    if (!user.getId().equals(firebaseUser.getUid())) {
                        userList.add(user);
                    }
                }
                adapter = new UserAdapter(AddMemberActivity.this, userList,true);
                mRecyclerView.setAdapter(adapter);
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
            window.setStatusBarColor(ContextCompat.getColor(AddMemberActivity.this, R.color.colorPrimaryDark));
        }
    }

    public void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (edSeach.getText().toString().equals("")) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        assert firebaseUser != null;
                        if (!user.getId().equals(firebaseUser.getUid()) && !keyMembers.contains(user.getId())) {
                            userList.add(user);
                        }
                    }
                    adapter = new UserAdapter(AddMemberActivity.this, userList,true);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
