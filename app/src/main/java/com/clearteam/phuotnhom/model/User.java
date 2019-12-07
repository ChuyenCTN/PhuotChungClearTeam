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
    private String latitude;
    private String longitude;

    private String phone1sos;
    private String phone2sos;
    private String phone3sos;
    private String name1sos;
    private String name2sos;
    private String name3sos;
    private String contentsos;


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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone1sos() {
        return phone1sos;
    }

    public void setPhone1sos(String phone1sos) {
        this.phone1sos = phone1sos;
    }

    public String getPhone2sos() {
        return phone2sos;
    }

    public void setPhone2sos(String phone2sos) {
        this.phone2sos = phone2sos;
    }

    public String getPhone3sos() {
        return phone3sos;
    }

    public void setPhone3sos(String phone3sos) {
        this.phone3sos = phone3sos;
    }

    public String getName1sos() {
        return name1sos;
    }

    public void setName1sos(String name1sos) {
        this.name1sos = name1sos;
    }

    public String getName2sos() {
        return name2sos;
    }

    public void setName2sos(String name2sos) {
        this.name2sos = name2sos;
    }

    public String getName3sos() {
        return name3sos;
    }

    public void setName3sos(String name3sos) {
        this.name3sos = name3sos;
    }

    public String getContentsos() {
        return contentsos;
    }

    public void setContentsos(String contentsos) {
        this.contentsos = contentsos;
    }
}
