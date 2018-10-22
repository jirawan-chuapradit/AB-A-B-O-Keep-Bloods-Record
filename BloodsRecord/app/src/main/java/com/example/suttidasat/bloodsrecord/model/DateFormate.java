package com.example.suttidasat.bloodsrecord.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormate  {

    String historyDate;

    public DateFormate() {
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public DateFormate(String historyDate) {
        this.historyDate = historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    public int compareDate() throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        Date strDate = sdf.parse(historyDate);

        Calendar cal = Calendar.getInstance();
        Date currentDate = sdf.parse( cal.getTime().toString());


        long diff = strDate.getTime() - currentDate.getTime();

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        System.out.println("difference between days: " + diffDays);

        return 0;
    }

}
