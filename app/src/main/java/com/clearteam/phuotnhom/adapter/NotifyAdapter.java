package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.Notifi;
import com.clearteam.phuotnhom.model.TourMe;
import com.clearteam.phuotnhom.model.User;
import com.clearteam.phuotnhom.ui.EditInformationActivity;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {

    private List<Notifi> notifiList;
    private Context mContext;
    private FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String tvName;

    private clickDetailTourGroup clickDetailTourGroup;

    public NotifyAdapter(List<Notifi> notifiList, Context mContext) {
        this.notifiList = notifiList;
        this.mContext = mContext;
    }

    public void setClickDetailTourGroup(NotifyAdapter.clickDetailTourGroup clickDetailTourGroup) {
        this.clickDetailTourGroup = clickDetailTourGroup;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_notify, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (notifiList.size() > 0) {
            holder.bindData(notifiList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return notifiList == null ? 0 : notifiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameSender;
        private TextView tvMessage;
        private TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSender = itemView.findViewById(R.id.tvNameSender);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvDate = itemView.findViewById(R.id.tv_date);


        }

        void bindData(final Notifi notifi) {
            tvNameSender.setText(notifi.getNameSender());
            tvMessage.setText(notifi.getMessage());
            tvDate.setText(notifi.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDetailTourGroup.onClickDetail(getAdapterPosition(), notifi);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickDetailTourGroup.onLongClick(getAdapterPosition(), notifi);
                    return true;
                }
            });
        }
    }

    public void setData(List<Notifi> data) {
        this.notifiList = data;
        notifyDataSetChanged();
    }

    private void clearData() {
        this.notifiList.clear();
        notifyDataSetChanged();
    }

    public interface clickDetailTourGroup {
        void onClickDetail(int position, Notifi response);

        void onLongClick(int adapterPosition, Notifi response);
    }


}
