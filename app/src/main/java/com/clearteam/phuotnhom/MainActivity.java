package com.clearteam.phuotnhom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.fragment.IntroductFragment;
import com.clearteam.phuotnhom.fragment.NotifyFragment;
import com.clearteam.phuotnhom.fragment.ProfileFragment;
import com.clearteam.phuotnhom.fragment.TourGroupFragment;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.EditInformationActivity;
import com.clearteam.phuotnhom.ui.LoginActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.facebook.login.LoginManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.fragment.IntroductFragment;
import com.clearteam.phuotnhom.fragment.NotifyFragment;
import com.clearteam.phuotnhom.fragment.ProfileFragment;
import com.clearteam.phuotnhom.fragment.TourGroupFragment;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.notification.Token;
import com.clearteam.phuotnhom.ui.LoginActivity;
import com.clearteam.phuotnhom.ui.map.MapFragment;
import com.clearteam.phuotnhom.ui.schedule.SchedulerFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout;

    private FragmentManager mFragmentManager;
    private TourGroupFragment mtourGroupFragment;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private CircleImageView imgAvata;
    private TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        intDataUser();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.drawable.color_gadient);
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

    private void intDataUser() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        imgAvata = view.findViewById(R.id.img_avata);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (null != MainActivity.this) {
                    User user = dataSnapshot.getValue(User.class);
                    tvName.setText(user.getUsername());
                    tvEmail.setText(user.getEmail());
                    if (user.getImageURL() != null) {
                        if (user.getImageURL().equals("default")) {
                            imgAvata.setImageResource(R.drawable.avatar);
                        } else {
                            Glide.with(getApplicationContext()).load(user.getImageURL()).into(imgAvata);
                        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sos:
                //startActivity(new Intent(MainActivity.this, EditInformationActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

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
                setTitle("Nhóm Phượt");
                if (mtourGroupFragment == null) {
                    mtourGroupFragment = new TourGroupFragment();
                }
                replaceFragment(mtourGroupFragment, mFragmentManager);
                break;
            case R.id.nav_schedule:
                setTitle("Lịch trình");
                replaceFragment(SchedulerFragment.getInstance(), mFragmentManager);
                break;
            case R.id.nav_user:
                setTitle("Thông tin cá nhân");
                replaceFragment(ProfileFragment.getInstance(), mFragmentManager);
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_notifi:
                setTitle("Thông báo");
                replaceFragment(NotifyFragment.getInstance(), mFragmentManager);
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        status("offline");
//    }
}
