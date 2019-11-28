package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.listener.ClickDetailMember;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;

import java.util.List;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ViewHoder> {

    private Context mContext;
    private List<User> userList;
    private ClickDetailMember mListener;
    private boolean isChat;

    public ListUserAdapter(Context mContext, List<User> userList, ClickDetailMember mListener, boolean isChat) {
        this.mContext = mContext;
        this.userList = userList;
        this.isChat = isChat;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ListUserAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_member, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUserAdapter.ViewHoder holder, int position) {
        if (userList.size() > 0) {
            holder.bindData(userList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        private ImageView imgAvata;
        private TextView tvName;
        private TextView tvTV;
        private ImageView img_on;
        private ImageView img_off;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgAvata = itemView.findViewById(R.id.img_avata);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTV = itemView.findViewById(R.id.tv_tv);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }

        public void bindData(User user) {
            tvName.setText(user.getUsername());
            String url = user.getImageURL();
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher)
                    .into(imgAvata);
            if (isChat) {
                if (user.getStatus().equals("online")) {
                    img_on.setVisibility(View.VISIBLE);
                    img_off.setVisibility(View.GONE);
                } else {
                    img_on.setVisibility(View.GONE);
                    img_off.setVisibility(View.VISIBLE);
                }
            } else {
                img_on.setVisibility(View.GONE);
                img_off.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickDetail(getAdapterPosition(), user);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClick(getAdapterPosition(), user);
                    return true;
                }
            });
        }
    }

    public void setData(List<User> data) {
        this.userList = data;
        notifyDataSetChanged();
    }

    private void clearData() {
        this.userList.clear();
        notifyDataSetChanged();
    }

}