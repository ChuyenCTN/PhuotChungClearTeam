package com.clearteam.phuotnhom.listener;

import com.clearteam.phuotnhom.model.User;

public interface ClickDetailMember {
    void onClickDetail(int position, User user);

    void onLongClick(int adapterPosition, User user);
}