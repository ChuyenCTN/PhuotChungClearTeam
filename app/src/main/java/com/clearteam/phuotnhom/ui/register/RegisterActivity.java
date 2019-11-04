package com.clearteam.phuotnhom.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edName, edEmail, edNumberPhone, edPass, edConfiPass;
    private ToggleButton togglePass, toggleConfiPass;
    private Button btnRegister;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        edName = findViewById(R.id.ed_name);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPassword);
        edConfiPass = findViewById(R.id.ed_confirm_password);
        togglePass = findViewById(R.id.toggle_pass);
        toggleConfiPass = findViewById(R.id.toggle_confi_pass);
        btnRegister = findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();

        togglePass.setOnClickListener(this);
        toggleConfiPass.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_pass:
                if (togglePass.isChecked()) {
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    edPass.setSelection(edPass.getText().length());
                } else {
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPass.setSelection(edPass.getText().length());
                    edPass.setSelection(edPass.getText().length());
                }
                break;
            case R.id.toggle_confi_pass:
                if (toggleConfiPass.isChecked()) {
                    edConfiPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    edConfiPass.setSelection(edConfiPass.getText().length());
                } else {
                    edConfiPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edConfiPass.setSelection(edConfiPass.getText().length());
                }
                break;
            case R.id.btnRegister:

                String txt_name = edName.getText().toString().trim();
                String txt_email = edEmail.getText().toString().trim();
                String txt_password = edPass.getText().toString().trim();
                String txt_confirm_pass = edConfiPass.getText().toString().trim();
                if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirm_pass)) {
                    Toast.makeText(RegisterActivity.this, "Chưa nhập đủ thông tin, Mời nhập lại!", Toast.LENGTH_SHORT).show();
                } else if (isEmail(edEmail) == false) {
                    edEmail.setError("Chưa đúng định dạng");
                    Toast.makeText(RegisterActivity.this, "Chưa đúng định dạng", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    edPass.setError("password trên 6 ký tự");
                    Toast.makeText(RegisterActivity.this, "password trên 6 ký tự", Toast.LENGTH_SHORT).show();
                } else if (!txt_confirm_pass.equals(txt_password)) {
                    edConfiPass.setError("không trùng password");
                    Toast.makeText(RegisterActivity.this, "không trùng password", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_name, txt_email, txt_password);
                }
                break;
        }
    }

    private void register(final String username, final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("username", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công !", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
