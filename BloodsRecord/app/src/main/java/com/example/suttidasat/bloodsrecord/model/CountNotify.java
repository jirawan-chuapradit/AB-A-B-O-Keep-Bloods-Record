package com.example.suttidasat.bloodsrecord.model;

public class CountNotify {

    public static int COUNT=0;

    public static int getCOUNT() {
        return COUNT;
    }

    public static void setCOUNT(int COUNT) {
        CountNotify.COUNT += COUNT;
    }
}
