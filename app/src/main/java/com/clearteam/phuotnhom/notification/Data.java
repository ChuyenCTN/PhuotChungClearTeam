package com.clearteam.phuotnhom.notification;

import java.io.Serializable;

public class Data implements Serializable {
    private String user;
    private int icon;
    private String body;
    private String title;
    private String sented;
    private String nameSender;
    private String content;
    private String dataJson;

    public Data() {
    }

    public Data(String user, int icon, String body, String title, String sented, String dataJson) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.dataJson = dataJson;
    }

    public Data(String title, String nameSender, String content) {
        this.content = content;
        this.title = title;
        this.nameSender = nameSender;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
