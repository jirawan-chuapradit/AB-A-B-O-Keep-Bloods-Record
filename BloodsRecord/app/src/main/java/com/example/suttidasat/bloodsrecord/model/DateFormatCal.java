package com.example.suttidasat.bloodsrecord.model;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateFormatCal {

    String historyDate,currentDate;
    int diffDays;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(int diffDays) {
        this.diffDays = diffDays;
    }

    public DateFormatCal() {
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public DateFormatCal(String historyDate) {
        this.historyDate = historyDate;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //16-12-2018
        setCurrentDate(dateFormat.format(date));

        try {
            Date date1 = dateFormat.parse(historyDate);
            long diffInMillies = Math.abs(date1.getTime() - date.getTime());
            diffDays = (int) (diffInMillies / (24 * 60 * 60 * 1000));
            System.out.println("difference between days: " + diffDays);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

//    public String sorter(NotifyManange d1,NotifyManange d2) throws ParseException {
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String d1Str = d1.getDate();
//        String d2Str = d2.getDate();
//        Date date1 = dateFormat.parse(d1Str);
//        Date date2 = dateFormat.parse(d2Str);
//        long diffInMillies = Math.abs(date1.getTime() - date2.getTime());
//        if (date1.getTime() - date2.getTime() > 0){
//            return d1Str;
//        }else
//            return d2Str;
//
//    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }


}
