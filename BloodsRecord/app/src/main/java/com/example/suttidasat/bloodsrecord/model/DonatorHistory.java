package com.example.suttidasat.bloodsrecord.model;

public class DonatorHistory  {
        private String date;
//    private int num;

    public DonatorHistory() {
    }

    public  DonatorHistory(String date){

        this.date = date;
//        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public int getNum() {
//        return num;
//    }
//
//    public void setNum(int num) {
//        this.num = num;
//    }
}