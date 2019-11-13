package com.clearteam.phuotnhom.ui.tourgroup.newgroup;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.clearteam.phuotnhom.R;

public class NewGroupActivity extends AppCompatActivity {
    private EditText edNameNewGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        Mapping();
        setTitle(R.string.txt_title_new_group);


    }

    private void Mapping() {
        edNameNewGroup = (EditText) findViewById(R.id.ed_name_new_group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                break;

        }
        return true;
    }
}
