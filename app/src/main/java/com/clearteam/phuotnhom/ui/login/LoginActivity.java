package com.clearteam.phuotnhom.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvRegister;
    private EditText edEmail, edPass;
    private Button btnLogin, btnLoginFB;
    private ToggleButton toggleButton;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        tvRegister = findViewById(R.id.textView);
        edEmail = findViewById(R.id.etEmail);
        edPass = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnServerLogin);
        btnLoginFB = findViewById(R.id.btnLoginFB);
        toggleButton = findViewById(R.id.toggle);
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        btnLoginFB.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.btnLoginFB:
                break;
            case R.id.btnServerLogin:
                login();
                break;
            case R.id.toggle:
                if (toggleButton.isChecked()) {
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT);
                    edPass.setSelection(edPass.getText().length());
                } else {
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPass.setSelection(edPass.getText().length());
                }
                break;
        }
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    private void login() {
        final String email = edEmail.getText().toString();
        String password = edPass.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if (isEmail(edEmail) == false) {
            edEmail.setError("Chưa đúng định dạng");
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // showActivity(MainActivity.class);
                                Toast.makeText(LoginActivity.this, "Xin chào" + email, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
//
//        if (mLoginViewModel.isEmailAndPasswordValid(email, password)) {
//            SharedPreferences.Editor editor = mSharedPreferences.edit();
//            editor.putBoolean(Const.KEY_IS_LOGIN, true);
//            editor.putString(Const.KEY_EMAIL, email);
//            editor.putString(Const.KEY_PASSWORD, password);
//            editor.apply();
//            hideKeyboard();
//            mLoginViewModel.login(email, password);
//        } else {
//            Toast.makeText(this, "Email or password invalid", Toast.LENGTH_SHORT).show();
//        }
    }
}
