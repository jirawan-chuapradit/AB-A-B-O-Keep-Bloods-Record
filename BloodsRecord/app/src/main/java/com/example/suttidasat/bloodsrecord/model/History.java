package com.example.suttidasat.bloodsrecord.model;

import android.widget.EditText;
import android.widget.TextView;

public class History {

    private String num;
    private String place;
    private String date;
    private String sign;

    public History(){}

    public History(String num, String date, String place, String sign) {
        this.num = num;
        this.date = date;
        this.place = place;
        this.sign = sign;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
