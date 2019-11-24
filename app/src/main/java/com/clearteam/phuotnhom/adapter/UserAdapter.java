package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHoder> {
    private Context mContext;
    private List<User> list;

    public UserAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        int pos = position;
        holder.tvName.setText(list.get(position).getUsername());
        String url = list.get(position).getImageURL();
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgAvata);

        holder.checkBox.setChecked(list.get(position).isSelected());
        holder.checkBox.setTag(list.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                User contact = (User) cb.getTag();
                contact.setSelected(cb.isChecked());
                list.get(pos).setSelected(cb.isChecked());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<User> getList() {
        return list;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imgAvata;
        TextView tvName;
        CheckBox checkBox;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgAvata = itemView.findViewById(R.id.img_avata);
            tvName = itemView.findViewById(R.id.tv_name);
            checkBox = itemView.findViewById(R.id.check);

        }

    }

}