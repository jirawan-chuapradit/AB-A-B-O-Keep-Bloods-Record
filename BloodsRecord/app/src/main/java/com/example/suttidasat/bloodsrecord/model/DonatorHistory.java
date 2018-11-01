package com.example.suttidasat.bloodsrecord.model;

public class DonatorHistory  {
    private String date;

    //Singleton
    private static DonatorHistory donatorHistoryInstance;

    public static DonatorHistory getDonatorHistoryInstance(){
        if(donatorHistoryInstance == null){
            donatorHistoryInstance = new DonatorHistory();
        }
        return donatorHistoryInstance;
    }


    private DonatorHistory() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}