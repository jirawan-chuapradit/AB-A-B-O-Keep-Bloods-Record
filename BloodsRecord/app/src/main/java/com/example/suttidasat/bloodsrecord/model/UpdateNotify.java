package com.example.suttidasat.bloodsrecord.model;

public class UpdateNotify {


//    private int count;
    private int date;

    //Singleton
    private static UpdateNotify updateNotifyInstance;

    public static UpdateNotify getUpdateNotifyInstance(){
        if(updateNotifyInstance == null){
            updateNotifyInstance = new UpdateNotify();
        }
        return updateNotifyInstance;
    }

    private UpdateNotify() {
    }



//    public static void setCount(int count) {
//        this.count = count;
//    }

//    public void countIncrease(){
//        count++;
//    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;

    }

//    public int getCount() {
//        return count;
//    }


    public String countNotify(String type){
        type = "not change";
        if (date == 83){
            type = "7days";
            System.out.println("count date > 83 && date < 90");
        }
        else if(date == 90){
            type = "today";
            System.out.println("count date >= 90");
        }
        return type;
    }
}
