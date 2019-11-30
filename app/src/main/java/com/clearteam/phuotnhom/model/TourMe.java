package com.clearteam.phuotnhom.model;

import java.io.Serializable;

public class TourMe implements Serializable {

    private String id;
    private String name;
    private String addressStart;
    private String addressEnd;
    private String date;
    private String tvAdd;
    private boolean isMyTour = true;
    private String avataGroup;
    private String keyId;
    private String userGroupId;
    private String token;

    public TourMe() {
    }

    public TourMe(String name, String addressStart, String addressEnd, String date) {
        this.name = name;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.date = date;
    }

    public TourMe(String id, String name, String addressStart, String addressEnd, String date, String tvAdd, boolean isMyTour, String avataGroup) {
        this.id = id;
        this.name = name;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.date = date;
        this.tvAdd = tvAdd;
        this.isMyTour = isMyTour;
        this.avataGroup = avataGroup;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressStart() {
        return addressStart;
    }

    public void setAddressStart(String addressStart) {
        this.addressStart = addressStart;
    }

    public String getAddressEnd() {
        return addressEnd;
    }

    public void setAddressEnd(String addressEnd) {
        this.addressEnd = addressEnd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTvAdd() {
        return tvAdd;
    }

    public void setTvAdd(String tvAdd) {
        this.tvAdd = tvAdd;
    }

    public boolean isMyTour() {
        return isMyTour;
    }

    public void setMyTour(boolean myTour) {
        isMyTour = myTour;
    }

    public String getAvataGroup() {
        return avataGroup;
    }

    public void setAvataGroup(String avataGroup) {
        this.avataGroup = avataGroup;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
