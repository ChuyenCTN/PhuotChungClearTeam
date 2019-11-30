package com.clearteam.phuotnhom.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangepassActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText edPassOld, edPassNew, edPassConfi;
    private CircleImageView imgAvata;
    private TextView tvName;
    private Button btnConfirm;
    private FirebaseAuth auth;
    private ImageButton imgBack;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    private ToggleButton showpass, showpassNew, showpassConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        changeStatustBar();
        initView();
    }

    private void initView() {
        edPassOld = findViewById(R.id.ed_pass_old);
        edPassNew = findViewById(R.id.ed_pass_new);
        edPassConfi = findViewById(R.id.ed_pass_confi);
        btnConfirm = findViewById(R.id.btn_confirm);
        imgBack = findViewById(R.id.img_back);
        showpass = findViewById(R.id.toggle);
        showpassNew = findViewById(R.id.toggle_new);
        showpassConfirm = findViewById(R.id.toggle_confi);
        imgAvata = findViewById(R.id.img_avata);
        tvName = findViewById(R.id.tv_name);


        auth = FirebaseAuth.getInstance();
        imgBack.setOnClickListener(this);
        showpass.setOnClickListener(this);
        showpassNew.setOnClickListener(this);
        showpassConfirm.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        loadData();
    }

    private void loadData() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (null != getApplication()) {
                    //Load image if the fragment is currently added to its activity.
                    User user = dataSnapshot.getValue(User.class);
                    tvName.setText(user.getUsername());
                    if (user.getImageURL().equals("default")) {
                        imgAvata.setImageResource(R.drawable.avatar);
                    } else {
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(imgAvata);
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
            window.setStatusBarColor(ContextCompat.getColor(ChangepassActivity.this, R.color.bg_tab));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                changePassword();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.toggle:
                if (showpass.isChecked()) {
                    edPassOld.setInputType(InputType.TYPE_CLASS_TEXT);
                    edPassOld.setSelection(edPassOld.getText().length());
                } else {
                    edPassOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPassOld.setSelection(edPassOld.getText().length());
                }
                break;
            case R.id.toggle_confi:
                if (showpassConfirm.isChecked()) {
                    edPassConfi.setInputType(InputType.TYPE_CLASS_TEXT);
                    edPassConfi.setSelection(edPassConfi.getText().length());
                } else {
                    edPassConfi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPassConfi.setSelection(edPassConfi.getText().length());
                }
                break;
            case R.id.toggle_new:
                if (showpassNew.isChecked()) {
                    edPassNew.setInputType(InputType.TYPE_CLASS_TEXT);
                    edPassNew.setSelection(edPassNew.getText().length());
                } else {
                    edPassNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPassNew.setSelection(edPassNew.getText().length());
                }
                break;
        }
    }

    private void changePassword() {
        String password = edPassOld.getText().toString();
        final String passwordNew = edPassNew.getText().toString();
        final String passwordConfirm = edPassConfi.getText().toString();

        if (password.isEmpty() || passwordConfirm.isEmpty() || passwordNew.isEmpty()) {
            Toast.makeText(this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6 || passwordConfirm.length() < 6 || passwordNew.length() < 6) {
            Toast.makeText(this, "Pass phải trên 6 ký tự", Toast.LENGTH_SHORT).show();
        } else if (!passwordConfirm.equals(passwordNew)) {
            Toast.makeText(this, "Không trùng mật khẩu mới", Toast.LENGTH_SHORT).show();
        } else {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getEmail() != null) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), password);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangepassActivity.this, "thành công", Toast.LENGTH_SHORT).show();

                                    user.updatePassword(passwordNew)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("BBB", "Đổi thành công");
                                                        auth.signOut();
                                                        startActivity(new Intent(ChangepassActivity.this, LoginActivity.class));
                                                        finish();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(ChangepassActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}
