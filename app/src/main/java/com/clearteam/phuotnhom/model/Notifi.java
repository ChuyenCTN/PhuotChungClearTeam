package com.clearteam.phuotnhom.model;

import java.io.Serializable;

public class Notifi implements Serializable {
    private String id;
    private String idGroup;
    private String sender;
    private String receiver;
    private String message;
    private String date;
    private String hour;
    private String nameSender;
    private String status;




    public Notifi() {
    }

    public Notifi(String id, String idGroup, String sender, String receiver, String message, String date, String hour, String nameSender, String status) {
        this.id = id;
        this.idGroup = idGroup;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
        this.hour = hour;
        this.nameSender = nameSender;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }
}
