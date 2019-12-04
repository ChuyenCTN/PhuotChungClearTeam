package com.clearteam.phuotnhom.model;

public class Messenger {
    private String nameSender;
    private String imgAvataSender;
    private String sender;
    private String receiver;
    private String message;
    private String hour;
    private String type;
    private String date;
    private boolean isseen;


    public Messenger() {
    }

    public Messenger(String nameSender, String imgAvataSender, String sender, String receiver, String message, String hour, String type, String date, boolean isseen) {
        this.nameSender = nameSender;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.hour = hour;
        this.date = date;
        this.type = type;
        this.imgAvataSender = imgAvataSender;
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public String getNameSender() {
        return nameSender;
    }

    public String getImgAvataSender() {
        return imgAvataSender;
    }

    public void setImgAvataSender(String imgAvataSender) {
        this.imgAvataSender = imgAvataSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
