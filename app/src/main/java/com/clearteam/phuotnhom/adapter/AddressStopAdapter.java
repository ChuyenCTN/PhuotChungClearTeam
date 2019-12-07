package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.AddressStop;
import com.clearteam.phuotnhom.model.User;

import java.util.List;

public class AddressStopAdapter extends RecyclerView.Adapter<AddressStopAdapter.ViewHoder> {
    private Context mContext;
    private List<AddressStop> list;
    private AddressStopAdapter.IClickItem iClickItem;

    public AddressStopAdapter(Context mContext, List<AddressStop> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_stop, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setiClickItem(AddressStopAdapter.IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }
    public List<AddressStop> getList() {
        return list;
    }
    public void setData(List<AddressStop> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        private TextView tvAddressStop;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvAddressStop = itemView.findViewById(R.id.tv_address_stop);
        }

        public void onBind(AddressStop addressStop) {
            tvAddressStop.setText(addressStop.getAddressStrop());
            tvAddressStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItem.onIClick(getAdapterPosition(),addressStop);
                }
            });

            tvAddressStop.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    iClickItem.onILongClick(getAdapterPosition(),addressStop);
                    return true;
                }
            });
        }
    }
    public interface IClickItem {
        void onIClick(int position,AddressStop addressStop);

        void onILongClick(int position, AddressStop addressStop);
    }
}