package com.example.nik_ax.kproject.xo;

public class MySpinnerData {
    int key;
    String value;
    public MySpinnerData(int key, String value){
        this.key = key;
        this.value = value;
    }
    public int getKey(){
        return key;
    }
    public String getValue(){
        return value;
    }
    public String toString(){
        return value;
    }
    public void setKey(int key){
        this.key = key;
    }
    public void setValue(String value){
        this.value = value;
    }
}