package com.example.suttidasat.bloodsrecord.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class SortNotify implements Comparator<NotifyManange> {

    @Override
    public int compare(NotifyManange d1, NotifyManange d2) {


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {

            String dSt1 = d1.getDate();
            Date day1 = sdf.parse(dSt1);
            Date day2 = sdf.parse(d2.getDate());

            return day2.compareTo(day1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;

    }


}
