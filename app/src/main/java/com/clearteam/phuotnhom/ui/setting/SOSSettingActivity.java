package com.clearteam.phuotnhom.ui.setting;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.setting.model.InfoSOS;
import com.clearteam.phuotnhom.utils.Const;
import com.clearteam.phuotnhom.utils.PrefUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SOSSettingActivity extends AppCompatActivity {
    private Toolbar toolbarSos;
    private ImageView imgBackSetting;
    private EditText edTenlienhe1;
    private EditText edPhonelienhe1;
    private EditText edTenlienhe2;
    private EditText edPhonelienhe2;
    private EditText edTenlienhe3;
    private EditText edPhonelienhe3;
    private EditText edTinnhansos;

    private InfoSOS mInfoSOS;
    private String numberphone1;
    private String numberphone2;
    private String numberphone3;
    private String name1;
    private String name2;
    private String name3;
    private String content;

    //firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sossetting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_label_sos));
        mapping();
        genInfo();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void mapping() {
        edTenlienhe1 = (EditText) findViewById(R.id.ed_tenlienhe1);
        edPhonelienhe1 = (EditText) findViewById(R.id.ed_phonelienhe1);
        edTenlienhe2 = (EditText) findViewById(R.id.ed_tenlienhe2);
        edPhonelienhe2 = (EditText) findViewById(R.id.ed_phonelienhe2);
        edTenlienhe3 = (EditText) findViewById(R.id.ed_tenlienhe3);
        edPhonelienhe3 = (EditText) findViewById(R.id.ed_phonelienhe3);
        edTinnhansos = (EditText) findViewById(R.id.ed_tinnhansos);
    }

    private void genInfo() {
        mInfoSOS = PrefUtils.getInfoSOS(getApplicationContext());
        if (mInfoSOS != null) {
            edTinnhansos.setText(mInfoSOS.getContent() + "");
            edTenlienhe1.setText(mInfoSOS.getName1() + "");
            edTenlienhe2.setText(mInfoSOS.getName2() + "");
            edTenlienhe3.setText(mInfoSOS.getName3() + "");
            edPhonelienhe1.setText(mInfoSOS.getNumberphone1() + "");
            edPhonelienhe2.setText(mInfoSOS.getNumberphone2() + "");
            edPhonelienhe3.setText(mInfoSOS.getNumberphone3() + "");
        }


    }

    private void initfirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        updateToken(FirebaseInstanceId.getInstance().getToken());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    public void onConfirmSOS(View view) {
        validate();
    }


    private void validate() {
        name1 = edTenlienhe1.getText().toString().trim();
        name2 = edTenlienhe2.getText().toString().trim();
        name3 = edTenlienhe3.getText().toString().trim();
        numberphone1 = edPhonelienhe1.getText().toString().trim();
        numberphone2 = edPhonelienhe2.getText().toString().trim();
        numberphone3 = edPhonelienhe3.getText().toString().trim();
        content = edTinnhansos.getText().toString().trim();


        if (name1.isEmpty() || name2.isEmpty() || name3.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.txt_error_namesos), Toast.LENGTH_SHORT).show();
            return;
        }
        if (numberphone1.isEmpty() || numberphone2.isEmpty() || numberphone3.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.txt_error_phonesos1), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.PHONE.matcher(numberphone1).matches()
                || !Patterns.PHONE.matcher(numberphone2).matches() || !Patterns.PHONE.matcher(numberphone3).matches()) {
            Toast.makeText(this, getResources().getString(R.string.txt_error_phonesos2), Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.txt_hint_contentsos), Toast.LENGTH_SHORT).show();
            return;
        }
        initfirebase();
        mInfoSOS = new InfoSOS();
        mInfoSOS.setName1(name1);
        mInfoSOS.setName2(name2);
        mInfoSOS.setName3(name3);
        mInfoSOS.setNumberphone1(numberphone1);
        mInfoSOS.setNumberphone2(numberphone2);
        mInfoSOS.setNumberphone3(numberphone3);
        mInfoSOS.setContent(content);

        PrefUtils.saveInfoSOS(getApplicationContext(), mInfoSOS);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Const.NAME1_SOS, name1);
        hashMap.put(Const.NAME2_SOS, name2);
        hashMap.put(Const.NAME3_SOS, name3);
        hashMap.put(Const.PHONE1_SOS, numberphone1);
        hashMap.put(Const.PHONE2_SOS, numberphone2);
        hashMap.put(Const.PHONE3_SOS, numberphone3);
        hashMap.put(Const.CONTENT_SOS, content);
        reference.updateChildren(hashMap);
        Toast.makeText(this, getResources().getString(R.string.txt_label_success_info_sos), Toast.LENGTH_SHORT).show();

//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage("0383605881", null, "Tôi đang cần sự giúp đỡ test", null, null);

        finish();
    }




}
