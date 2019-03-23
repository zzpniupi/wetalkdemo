package com.example.wetalk;

public class Friends {
    private String phoneNumber;
    private String name;
    private int friendID;
    private int imagNumber;

    public Friends(String name,int imagNumber){
        this.name=name;
        this.imagNumber=imagNumber;
    }

    public Friends(String name,int friendID, int imagNumber){
        this.name=name;
        this.friendID=friendID;
        this.imagNumber=imagNumber;
    }

    public int getImagNumber() {
        return imagNumber;
    }

    public String getName() {
        return name;
    }

    public int getFriendID() {
        return friendID;
    }
}
