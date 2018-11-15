package com.example.suttidasat.bloodsrecord.model;

import android.widget.EditText;
import android.widget.TextView;

public class News {

    private String title;
    private String detail;
    private String date;
    private String link;

    public News(){}

    public News(String title, String date_now, String detail, String link) {
        this.title = title;
        date_now = date;
        this.detail = detail;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
