package com.clearteam.phuotnhom.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.ui.ChangepassActivity;
import com.clearteam.phuotnhom.utils.Const;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private static SettingFragment INSTANCE;
    private View layoutSosSetting;
    private View layoutChangePassSetting;

    public static SettingFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        Mapp(view);

        layoutSosSetting.setOnClickListener(this::onClick);
        layoutChangePassSetting.setOnClickListener(this::onClick);

        return view;
    }


    private void Mapp(View v) {
        layoutSosSetting = v.findViewById(R.id.layout_sos_setting);
        layoutChangePassSetting = v.findViewById(R.id.layout_change_pass_setting);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_sos_setting:
                startActivity(new Intent(getContext(), SOSSettingActivity.class).putExtra(Const.TYPE, Const.TYPE_SOS));
                break;
            case R.id.layout_change_pass_setting:
                startActivity(new Intent(getActivity(), ChangepassActivity.class));
                break;
        }
    }
}
