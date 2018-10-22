package com.example.suttidasat.bloodsrecord.model;

public class NotifyManange {
    private String date, text,time;



    public NotifyManange(String date, String text, String time) {
        this.date = date;
        this.text = text;
        this.time = time;
    }

    public NotifyManange() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
