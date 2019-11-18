package com.clearteam.phuotnhom.ui.detailtourgroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.utils.Const;

public class TourGroupDetailActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ImageButton imgBack;
    private TextView tvNameGroup;
    private ImageView imgAvataGroup,imgMenu;
    private String nameGroup, imageG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_group_detail);
        changeStatustBar();
        initView();
    }

    private void initView() {

        imgBack = findViewById(R.id.img_back);
        tvNameGroup = findViewById(R.id.tv_name_group);
        imgAvataGroup = findViewById(R.id.img_avata_group);
        imgMenu = findViewById(R.id.img_menu_group);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TourGroupDetailActivity.this,v);
                popupMenu.setOnMenuItemClickListener(TourGroupDetailActivity.this);
                popupMenu.inflate(R.menu.menu_tour_group);
                popupMenu.show();
            }
        });
        getData();
    }
    private void changeStatustBar() {
        Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(TourGroupDetailActivity.this, R.color.bg_tab));
        }
    }

    private void getData() {
        Intent intent = getIntent();
        nameGroup = intent.getStringExtra(Const.KEY_NAME_GROUP);
        imageG = intent.getStringExtra(Const.KEY_IMAGE_GROUP);
        if (imageG == null) {
            Glide.with(this).load(R.drawable.avatar).into(imgAvataGroup);
        } else {
            Glide.with(this).load(imageG).into(imgAvataGroup);
        }
        tvNameGroup.setText(nameGroup);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_user:
                Toast.makeText(this, "adđ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_group:
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_group:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
