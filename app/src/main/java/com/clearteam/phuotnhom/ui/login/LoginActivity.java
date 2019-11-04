package com.clearteam.phuotnhom.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.ui.register.RegisterActivity;
import com.clearteam.phuotnhom.util.Const;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvRegister;
    private EditText edEmail, edPass;
    private Button btnLogin, btnLoginFB;
    private ToggleButton toggleButton;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private CallbackManager mCallbackManager;
    private SharedPreferences mSharedPreferences;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        tvRegister = findViewById(R.id.tv_register);
        edEmail = findViewById(R.id.etEmail);
        edPass = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnServerLogin);
        btnLoginFB = findViewById(R.id.btnLoginFB);
        toggleButton = findViewById(R.id.toggle);
        loginButton = findViewById(R.id.login_button);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        mSharedPreferences = getSharedPreferences(Const.KEY_IS_PRER, MODE_PRIVATE);
        final boolean isLogin = mSharedPreferences.getBoolean(Const.KEY_IS_LOGIN, false);
        if (isLogin == true) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        btnLogin.setOnClickListener(this);
        btnLoginFB.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        loginFB();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.btnLoginFB:
                loginButton.performClick();
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

    private void loginFB() {
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                // ...
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("AAA", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AAA", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AAA", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateUI() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
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
        } else if (password.length() < 6) {
            edPass.setError("password trên 6 ký tự");
            Toast.makeText(LoginActivity.this, "password trên 6 ký tự", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}
