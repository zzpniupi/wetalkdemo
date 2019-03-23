package com.example.wetalk;

public class Msg {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SEND=1;
    private int infoID;
    private String content;
    private int type;

    public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }

    public Msg(int infoID, String content,int type){
        this.infoID=infoID;
        this.content=content;
        this.type=type;
    }

    public int getInfoID(){ return infoID;}

    public String getContent() {
        return content;
    }

    public int getType(){
        return type;
    }
}
