package com.example.suttidasat.bloodsrecord.model;

public class UpdateNotify {


    public void setCount(int count) {
        this.count = count;
    }

    private int count = 0;
    private int date;
    private String type;

    public String getType() {
        return type;

    }

    public void setType(String type) {
        this.type = type;
    }



    public void countIncrease(){
        count++;
    }
    public void countDiscrease(){
        count--;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
        countNotify();
    }


    public UpdateNotify() {
    }

    public int getCount() {
        return count;
    }


    public void countNotify(){
        if (date == 83){
            countIncrease();
            type = "7days";
            System.out.println("count date > 83 && date < 90");
        }
        else if(date == 90){
            countIncrease();
            type = "today";
            System.out.println("count date >= 90");
        }
    }
}
