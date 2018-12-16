package com.example.suttidasat.bloodsrecord.model;

public class Privilege {

    private String content;
    private String num;

    public Privilege() {
    }

    public Privilege(String content, String num) {
        this.content = content;
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
