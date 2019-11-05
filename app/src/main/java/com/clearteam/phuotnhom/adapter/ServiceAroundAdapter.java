package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.listener.IClickItem;
import com.clearteam.phuotnhom.model.ServiceAround;

import java.util.List;

public class ServiceAroundAdapter extends RecyclerView.Adapter<ServiceAroundAdapter.ViewHoder> {
    private Context mContext;
    private List<ServiceAround> list;
    private IClickItem iClickItem;


    public ServiceAroundAdapter(Context mContext, List<ServiceAround> list,IClickItem iClickItem ) {
        this.mContext = mContext;
        this.list = list;
        this.iClickItem = iClickItem;
    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_around,parent,false);
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

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNameService;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
            tvNameService = itemView.findViewById(R.id.tv_name_service);
        }

        public void onBind(ServiceAround serviceAround) {
            tvNameService.setText(serviceAround.getTvName());
            String ur2 = serviceAround.getImageView();
            Glide.with(mContext)
                    // sua thanh ur2
                    .load(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);

        }
    }

    public interface IClickItem {
        void onItemClick(ServiceAround serviceAround);
    }
}
