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
import com.clearteam.phuotnhom.model.User;

import java.util.List;

public class ListStatusUserAdapter extends RecyclerView.Adapter<ListStatusUserAdapter.ViewHoder> {
    private Context mContext;
    private List<User> list;
    private IClickItem iClickItem;
    private boolean isChat;


    public ListStatusUserAdapter(Context mContext, List<User> list, boolean isChat) {
        this.mContext = mContext;
        this.list = list;
        this.isChat = isChat;
    }

    public void setiClickItem(IClickItem iClickItem) {
        this.iClickItem = iClickItem;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_status, parent, false);
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
        private ImageView imgAvata;
        private TextView tvName;
        private ImageView img_on;
        private ImageView img_off;
        private ImageView imgMessage;
        private ImageView imgLocation;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgAvata = itemView.findViewById(R.id.profile_image1);
            tvName = itemView.findViewById(R.id.tv_username);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            imgLocation = itemView.findViewById(R.id.img_location);
            imgMessage = itemView.findViewById(R.id.img_message);
        }

        public void onBind(User user) {
            tvName.setText(user.getUsername());
            String url = user.getImageURL();
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
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
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItem.onMessageClick(getAdapterPosition(), user);
                }
            });
            imgLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickItem.onLocationClick(getAdapterPosition(), user);
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    mListener.onClickDetail(getAdapterPosition(), user);
////                }
////            });
////            itemView.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    mListener.onLongClick(getAdapterPosition(), user);
////                    return true;
////                }
////            });
        }
    }

    public interface IClickItem {
        void onLocationClick(int position, User user);

        void onMessageClick(int position, User user);
    }
}
