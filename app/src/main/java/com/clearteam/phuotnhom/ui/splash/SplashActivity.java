package com.clearteam.phuotnhom.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.clearteam.phuotnhom.ui.login.LoginActivity;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.util.Const;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread time = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(Const.SPLASH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        time.start();
    }
}
