package com.clearteam.phuotnhom.model;

import android.widget.ImageView;
import android.widget.TextView;

public class ServiceAround {
    private String imageView;
    private String tvName;

    public ServiceAround() {
    }

    public ServiceAround(String imageView, String tvName) {
        this.imageView = imageView;
        this.tvName = tvName;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }
}
