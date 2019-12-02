package com.clearteam.phuotnhom.model;

import java.io.Serializable;

public class Notifi implements Serializable {
    private String sender;
    private String receiver;
    private String message;
    private String date;
    private String nameSender;

    public Notifi() {
    }

    public Notifi(String sender, String receiver, String message, String date, String nameSender) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
        this.nameSender = nameSender;
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

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }
}
