package com.clearteam.phuotnhom.ui.setting.model;

public class InfoSOS {
    private String numberphone1;
    private String numberphone2;
    private String numberphone3;
    private String name1;
    private String name2;
    private String name3;
    private String content;

    public String getNumberphone1() {
        return numberphone1;
    }

    public void setNumberphone1(String numberphone1) {
        this.numberphone1 = numberphone1;
    }

    public String getNumberphone2() {
        return numberphone2;
    }

    public void setNumberphone2(String numberphone2) {
        this.numberphone2 = numberphone2;
    }

    public String getNumberphone3() {
        return numberphone3;
    }

    public void setNumberphone3(String numberphone3) {
        this.numberphone3 = numberphone3;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public InfoSOS() {
    }

    public InfoSOS(String numberphone1, String numberphone2, String numberphone3, String name1, String name2, String name3, String content) {
        this.numberphone1 = numberphone1;
        this.numberphone2 = numberphone2;
        this.numberphone3 = numberphone3;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.content = content;
    }
}
