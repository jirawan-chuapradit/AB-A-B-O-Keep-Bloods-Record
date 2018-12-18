package com.example.suttidasat.bloodsrecord.model;

import java.text.ParseException;
import java.util.Comparator;

public class NumSorter implements Comparator<History> {

    @Override
    public int compare(History n1, History n2) {


        try {

            int num1 = Integer.parseInt(n1.getNum());
            int num2 = Integer.parseInt(n2.getNum());


            return Integer.compare(num1, num2);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return 0;
    }
}
