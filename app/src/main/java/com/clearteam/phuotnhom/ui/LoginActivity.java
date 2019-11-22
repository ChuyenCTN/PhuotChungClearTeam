package com.clearteam.phuotnhom.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.utils.Const;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
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
    private TextView tvRegister, tvForgotPass;
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

        changeStatustBar();

        tvRegister = findViewById(R.id.tv_register);
        tvForgotPass = findViewById(R.id.tv_forgot_pass);
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
        tvForgotPass.setOnClickListener(this);

        loginFB();


    }

    private void changeStatustBar() {
        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.bg_tab));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forgot_pass:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.btnLoginFB:
                //loginButton.performClick();
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
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Log.v("LoginActivity", response.toString());
                                String id = null;
                                try {
                                    id = object.getString("id");
                                    String first_name = object.getString("first_name");
                                    String last_name = object.getString("last_name");
                                    String gender = object.getString("gender");
                                    String birthday = object.getString("birthday");
                                    //   String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";
                                    String profileURL = "";
                                    if (Profile.getCurrentProfile() != null) {
                                        profileURL = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().getId(), 400, 400).toString();
                                    }
                                    Log.d("first_name", last_name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
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
                            //FirebaseUser user = auth.getCurrentUser();
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putBoolean(Const.IS_LOGIN_FACEBOOK, true);
                            editor.apply();

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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putBoolean(Const.IS_LOGIN_TK,true);
                                editor.apply();
                                updateUI();

                            } else {
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

///*
//        final String email = edEmail.getText().toString();
//        String password = edPass.getText().toString();
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//        } else if (isEmail(edEmail) == false) {
//            edEmail.setError("Chưa đúng định dạng");
//        } else if (password.length() < 6) {
//            edPass.setError("password trên 6 ký tự");
//            Toast.makeText(LoginActivity.this, "password trên 6 ký tự", Toast.LENGTH_SHORT).show();
//        } else {
//            auth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                SharedPreferences.Editor editor = mSharedPreferences.edit();
//                                editor.putBoolean(Const.IS_LOGIN_TK,true);
//                                editor.apply();
//                                updateUI();
//
//                            } else {
//                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
/*
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
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putBoolean(Const.IS_LOGIN_TK,true);
                                editor.apply();
                                updateUI();

                            } else {
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
<<<<<<< HEAD
        }*/






