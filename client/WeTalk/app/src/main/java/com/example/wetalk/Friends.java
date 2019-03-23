package com.example.wetalk;

public class Friends {
    private String phoneNumber;
    private String name;
    private int imagNumber;

    public Friends(String name,int imagNumber){
        this.name=name;
        this.imagNumber=imagNumber;
    }

    public int getImagNumber() {
        return imagNumber;
    }

    public String getName() {
        return name;
    }
}
