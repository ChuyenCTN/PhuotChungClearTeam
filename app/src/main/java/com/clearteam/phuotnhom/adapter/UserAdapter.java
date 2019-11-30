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
import com.clearteam.phuotnhom.model.Messenger;
import com.clearteam.phuotnhom.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHoder> {
    private Context mContext;
    private List<User> list;
    private boolean isChat;
    private String theLastMessage;

    public UserAdapter(Context mContext, List<User> list, boolean isChat) {
        this.mContext = mContext;
        this.list = list;
        this.isChat = isChat;
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

        if (isChat) {
            if (list.get(position).getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
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
        private ImageView imgAvata;
        private TextView tvName;
        private CheckBox checkBox;
        private ImageView img_on;
        private ImageView img_off;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            imgAvata = itemView.findViewById(R.id.img_avata);
            tvName = itemView.findViewById(R.id.tv_name);
            checkBox = itemView.findViewById(R.id.check);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);

        }

    }

    private void lastMessage(final String userid, final TextView last_msg) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Messenger chat = snapshot.getValue(Messenger.class);
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();

                        }
                    }
                    switch (theLastMessage) {
                        case "default":
                            last_msg.setText("No Message");
                            break;
                        default:
                            last_msg.setText(theLastMessage);
                            break;
                    }
                    theLastMessage = "default";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}