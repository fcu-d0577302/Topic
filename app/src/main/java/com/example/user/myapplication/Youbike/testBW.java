package com.example.user.myapplication.Youbike;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shang on 2017/6/13.
 */

public class testBW extends AsyncTask<String,Integer,String> {

    long start,end;
    ProgressDialog progressDialog;
    Context context;
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;
    boolean flag=true;
    ArrayList<YouBike> youbikes;
    String city[]={"彰化縣","彰化市","新北市","基隆市","台北市","桃園市","台中市","新竹市"};

    int p=0;

    public testBW(Context context, ArrayList<YouBike> youBikes){
        this.context=context;
        this.youbikes=youBikes;
    }

    @Override
    protected void onPreExecute() {
        Log.d("Runnable","現成啟動"+youbikes.size());
        start=System.currentTimeMillis();
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("載入資料中...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(youbikes.size());
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        for(int i=0;i<youbikes.size();i++){
            String urlcity=getUrl_City(youbikes.get(i).getLat(), youbikes.get(i).getLng());
            openHttp(urlcity);
            readCity(i);

            while(flag){
                try {
                    Thread.sleep(100);
                    openHttp(urlcity);
                    readCity(i);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();
        end=System.currentTimeMillis();
        Log.d("Runnable","現成結束 "+(double)(end-start)/1000);

    }


    public String getUrl_City(double lat,double lng){
        StringBuffer url_city=new StringBuffer("");
        url_city.append("https://maps.google.com/maps/api/geocode/json?latlng=")
                .append(lat)
                .append(",")
                .append(lng)
                .append("&language=zh-TW&sensor=true");
        Log.d("URL",url_city.toString());
        return url_city.toString();
    }

    public void openHttp(String urlcity){
        try {
            url=new URL(urlcity);
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(3000);
            inputStream=httpURLConnection.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        }  catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCity(int p){
        String line="";
        StringBuffer sb=new StringBuffer("");
        try {
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
                //Log.v("CITY",sb.toString());
                int[] a=new int[city.length];
                for(int i=0;i<city.length;i++) {
                    a[i] = line.indexOf(city[i]);
                    if (a[i] != -1) {
                        youbikes.get(p).setCity(city[i]);
                        Log.v("CITY", youbikes.get(p).getCity());
                        break;
                    }
                }
                if(!youbikes.get(p).getCity().equals("")){
                    progressDialog.setProgress(p++);
                    flag=false;
                    break;
                }
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
