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
import com.clearteam.phuotnhom.model.Messenger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Messenger> mChat;
    private String imageURL;

    private FirebaseUser firebaseUser;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<Messenger> mChat) {
        this.context = context;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Messenger chat = mChat.get(position);

        String fromMessageType = chat.getType();

        holder.tvNameSender.setText(chat.getNameSender());
        holder.show_message.setText(chat.getMessage());
        holder.hour.setText(chat.getHour());
        String url = chat.getImgAvataSender();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher)
                .into(holder.profile_image);
        if (fromMessageType.equals("text")){
            holder.show_message.setText(chat.getMessage());

        }else if (fromMessageType.equals("image")){
            holder.show_message.setVisibility(View.GONE);
            holder.show_message_image.setVisibility(View.VISIBLE);
            String url1 = chat.getMessage();
            Glide.with(context)
                    .load(url1)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.show_message_image);
        }

//        if (imageURL.equals("default")){
//            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            Glide.with(context).load(imageURL).into(holder.profile_image);
//        }
//        if (position == mChat.size()-1){
//            if (chat.isIsseen()){
//                holder.txt_seen.setText("Seen");
//            } else {
//                holder.txt_seen.setText("Delivered");
//            }
//        }else {
////            holder.txt_seen.setVisibility(View.GONE);
////        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;
        public ImageView show_message_image;
        public TextView txt_seen;
        public TextView hour;
        public TextView tvNameSender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            show_message_image = itemView.findViewById(R.id.show_message_img);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            hour = itemView.findViewById(R.id.tv_hour);
            tvNameSender = itemView.findViewById(R.id.tv_name_sender);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
