package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;

import java.util.List;

public class UserHorizontalAdapter extends RecyclerView.Adapter<UserHorizontalAdapter.ViewHoder> {
    private Context mContext;
    private List<User> list;

    public UserHorizontalAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_h,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
