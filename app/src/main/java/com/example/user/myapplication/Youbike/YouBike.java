package com.example.user.myapplication.Youbike;

import java.io.Serializable;

public class YouBike implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private int sno;               //sno:站點代號
    private String sna;           //sna:場站名稱
    private String sarea;         //sarea:場站區域(中文)
    private String ar;            //ar:地址(中文)
    private int tot;              //tot:場站總停車格
    private int sbi;              //sbi:可借車位數
    private int bemp;           //bemp:可還空位數
    private double lat;           //lat:緯度
    private double lng;          //lng:經度
    private String mday;        //mday:資料更新時間
    private int sv;
    private String city="";

    public void setSno(int sno) {
        this.sno = sno;
    }
    public void setSna(String sna) {
        this.sna = sna;
    }
    public void setSarea(String sarea) {
        this.sarea = sarea;
    }
    public void setAr(String ar) {
        this.ar = ar;
    }
    public void setTot(int tot) {
        this.tot = tot;
    }
    public void setSbi(int sbi) {
        this.sbi = sbi;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setBemp(int bemp) {
        this.bemp = bemp;
    }
    public void setMday(String mday) {
        this.mday = mday;
    }
    public void setSv(int sv) {
        this.sv = sv;
    }
    public void setCity(String city){
        this.city=city;
    }

    public int getSno() {
        return sno;
    }
    public String getSna() {
        return sna;
    }
    public String getSarea() {
        return sarea;
    }
    public String getAr() {
        return ar;
    }
    public int getTot() {
        return tot;
    }
    public int getSbi() {
        return sbi;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
    public int getBemp() {
        return bemp;
    }
    public String getMday() {
        return mday;
    }
    public int getSv() {
        return sv;
    }
    public String getCity(){
        return city;
    }

}
