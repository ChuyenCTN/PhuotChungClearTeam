package com.clearteam.phuotnhom.model;

public class ListMember {
    private int id;
    private String keyId;

    public ListMember() {
    }

    public ListMember(int id, String keyId) {
        this.id = id;
        this.keyId = keyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
