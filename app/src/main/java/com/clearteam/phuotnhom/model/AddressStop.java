package com.clearteam.phuotnhom.model;

public class AddressStop {
    private String id;
    private String idGroup;
    private String addressStrop;
    private String idUserGroup;


    public AddressStop() {
    }

    public AddressStop(String id, String idGroup, String addressStrop, String idUserGroup) {
        this.id = id;
        this.idGroup = idGroup;
        this.addressStrop = addressStrop;
        this.idUserGroup = idUserGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getAddressStrop() {
        return addressStrop;
    }

    public void setAddressStrop(String addressStrop) {
        this.addressStrop = addressStrop;
    }

    public String getIdUserGroup() {
        return idUserGroup;
    }

    public void setIdUserGroup(String idUserGroup) {
        this.idUserGroup = idUserGroup;
    }
}
