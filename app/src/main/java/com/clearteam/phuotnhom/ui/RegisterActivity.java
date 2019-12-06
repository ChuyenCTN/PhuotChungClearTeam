package com.clearteam.phuotnhom.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.clearteam.phuotnhom.MainActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.utils.CommonUtils;
import com.clearteam.phuotnhom.utils.Const;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private EditText edName, edEmail, edNumberPhone, edPass, edConfiPass;
    private ToggleButton togglePass, toggleConfiPass;
    private Button btnRegister;
    private ImageButton btnRow;
    private FirebaseAuth auth;
    private DatabaseReference mReference;
    private StorageReference mStorageReference;
    private FirebaseUser firebaseUser;
    private ImageView imgAvata;
    private Uri imageUri;
    private StorageTask uploadTask;

    private LocationManager locationManager;
    protected double latitude;
    protected double longitude;
    protected boolean gps_enabled, network_enabled;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatustBar();
        initView();
        initLocation();
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


    private void initView() {
        edName = findViewById(R.id.ed_name);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPassword);
        edConfiPass = findViewById(R.id.ed_confirm_password);
        togglePass = findViewById(R.id.toggle_pass);
        toggleConfiPass = findViewById(R.id.toggle_confi_pass);
        btnRegister = findViewById(R.id.btnRegister);
        btnRow = findViewById(R.id.img_open_main1);

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");

        togglePass.setOnClickListener(this);
        toggleConfiPass.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRow.setOnClickListener(this);
//        imgAvata.setOnClickListener(this);

    }

    private void changeStatustBar() {
        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(RegisterActivity.this, R.color.bg_tab));
        }
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
            case R.id.img_open_main1:
                finish();
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
        CommonUtils.showLoading(RegisterActivity.this);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CommonUtils.hideLoading();
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();
                            mReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");
                            hashMap.put("address", "chưa có thông tin");
                            hashMap.put("numberPhone", "chưa có thông tin");
                            hashMap.put("numberPhoneRelatives", "chưa có thông tin");
                            hashMap.put("sex", "chưa có thông tin");
                            hashMap.put("status", "offline");
                            hashMap.put("isMember", String.valueOf(false));
                            hashMap.put("search", username.toLowerCase());
                            hashMap.put("latitude", String.valueOf(latitude));
                            hashMap.put("longitude", String.valueOf(longitude));

                            mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Const.IMAGE_REQUSET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.IMAGE_REQUSET && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(RegisterActivity.this, "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploafImage();
            }
        }
    }

    private void uploafImage() {
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//
//                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                final String downloadUrl = uri.toString();
//                                mReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("imageURL");
//                                mReference.setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            progressDialog.dismiss();
//                                            Toast.makeText(RegisterActivity.this, "Your picture Saved successfully", Toast.LENGTH_SHORT).show();
//
//                                        } else {
//                                            Toast.makeText(RegisterActivity.this, "Problem occurred while tryng to save your picture..", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
