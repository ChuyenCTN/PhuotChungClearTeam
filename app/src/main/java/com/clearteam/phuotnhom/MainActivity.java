package com.clearteam.phuotnhom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.clearteam.phuotnhom.fragment.IntroductFragment;
import com.clearteam.phuotnhom.fragment.MapFragment;
import com.clearteam.phuotnhom.fragment.ProfileFragment;
import com.clearteam.phuotnhom.fragment.TourGroupFragment;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.LoginActivity;
import com.facebook.login.LoginManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout, fameToolBar;

    private FragmentManager mFragmentManager;
    private TourGroupFragment mtourGroupFragment;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        frameLayout = findViewById(R.id.contentContainer);
        mFragmentManager = getSupportFragmentManager();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(MapFragment.getInstance(), mFragmentManager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////        if (id == R.id.action_settings) {
////
////            return true;
////        }
//        switch (item.getItemId()) {
//            case R.id.edit_user:
//                startActivity(new Intent(MainActivity.this, EditInformationActivity.class));
//                break;
//            case R.id.change_pass:
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                setTitle("Home");
                replaceFragment(MapFragment.getInstance(), mFragmentManager);
                break;
            case R.id.nav_group:
                setTitle("Nhóm tour");
                if (mtourGroupFragment == null) {
                    mtourGroupFragment = new TourGroupFragment();
                }
                replaceFragment(mtourGroupFragment, mFragmentManager);
                break;
            case R.id.nav_schedule:
                break;
            case R.id.nav_user:
                setTitle("Thông tin cá nhân");
                replaceFragment(ProfileFragment.getInstance(), mFragmentManager);
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_notifi:
                break;
            case R.id.nav_introduction:
                setTitle("Giới thiệu");
                replaceFragment(IntroductFragment.getInstance(), mFragmentManager);
                break;
            case R.id.nav_out:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
