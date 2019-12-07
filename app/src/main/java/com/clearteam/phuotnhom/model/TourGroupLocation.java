package com.clearteam.phuotnhom.model;

import java.util.List;

public class TourGroupLocation {

    private List<User> userList;

    private String key;

    public TourGroupLocation(List<User> userList, String key) {
        this.userList = userList;
        this.key = key;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
