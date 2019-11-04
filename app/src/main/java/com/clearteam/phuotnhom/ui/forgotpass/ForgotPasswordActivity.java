package com.clearteam.phuotnhom.ui.forgotpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clearteam.phuotnhom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edEmail;
    private Button btnSendEmail;
    private ImageButton imageButton;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edEmail = findViewById(R.id.edEmail);
        btnSendEmail = findViewById(R.id.btn_send_email);
        imageButton = findViewById(R.id.img_open_main);
        auth = FirebaseAuth.getInstance();
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editText =  edEmail.getText().toString();
                if (editText.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if (isEmail(edEmail) == false) {
                    edEmail.setError("Chưa đúng định dạng");
                }else{
                    auth.sendPasswordResetEmail(editText).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                View dialog = inflater.inflate(R.layout.item_forgot_pass,null);
                                //     builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                builder.setCancelable(false);
                                final TextView textView = dialog.findViewById(R.id.tv_login);
                                LinearLayout ll = dialog.findViewById(R.id.ll);
                                ll.setBackgroundResource(R.drawable.bg_forgot_pass);
                                builder.setView(dialog);

                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
