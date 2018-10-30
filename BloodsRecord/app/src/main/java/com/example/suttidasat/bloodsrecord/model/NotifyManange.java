package com.example.suttidasat.bloodsrecord.model;

public class NotifyManange {
    private String date, text;

    //Singleton
    private static NotifyManange notifyManangeInstance;

    public static NotifyManange getNotifyManangeInstance(){
        if (notifyManangeInstance == null){
            notifyManangeInstance = new NotifyManange();
        }
        return notifyManangeInstance;
    }




    private NotifyManange() {
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
