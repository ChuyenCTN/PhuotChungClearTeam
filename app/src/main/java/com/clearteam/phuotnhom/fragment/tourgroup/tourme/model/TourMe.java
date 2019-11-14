package com.clearteam.phuotnhom.fragment.tourme.model;

public class TourMe {

    private String id;
    private String name;
    private String addressStart;
    private String addressEnd;
    private String date;


    public TourMe() {
    }

    public TourMe(String id, String name, String addressStart, String addressEnd, String date) {
        this.id = id;
        this.name = name;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.date = date;
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
}
