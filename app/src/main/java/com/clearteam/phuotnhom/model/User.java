package com.clearteam.phuotnhom.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String username;
    private String email;
    private String address;
    private String numberPhone;
    private String status;
    private String imageURL;
    private String search;
    private boolean isSelected;
    private String isMember;
    private String numberPhoneRelatives;
    private String sex;


    public User() {
    }

    public User(String username, String address, String numberPhone, String status, String imageURL, String search, String email) {

        this.username = username;
        this.address = address;
        this.numberPhone = numberPhone;
        this.status = status;
        this.imageURL = imageURL;
        this.search = search;
        this.email = email;
    }

    public User(String mName, String mEmail, String mAddress, String mNumberPhone, String toString, String search) {
        this.username = mName;
        this.address = mAddress;
        this.numberPhone = mNumberPhone;
        this.imageURL = toString;
        this.search = search;
        this.email = mEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

    public String getNumberPhoneRelatives() {
        return numberPhoneRelatives;
    }

    public void setNumberPhoneRelatives(String numberPhoneRelatives) {
        this.numberPhoneRelatives = numberPhoneRelatives;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
