package com.example.suttidasat.bloodsrecord.model;

public class NotifyManange {
    private String date;
    private String text;

    private String num;

    //Singleton
    private static NotifyManange notifyManangeInstance;

    public static NotifyManange getNotifyManangeInstance(){
        if (notifyManangeInstance == null){
            notifyManangeInstance = new NotifyManange();
        }
        return notifyManangeInstance;
    }



    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
        System.out.println(num);
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

}
