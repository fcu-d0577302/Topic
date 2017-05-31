package com.example.user.myapplication.Youbike;

import android.util.Log;

import com.example.user.myapplication.FunctionListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shang on 2017/5/21.
 */
public class YouBikeRunnable implements Runnable{
    FunctionListener listener;
    URL url;
    HttpURLConnection httpURLConnection;
    InputStream inputStream;
    BufferedReader bufferedReader;
    boolean flag=true;

    YouBike youbike;

    public YouBikeRunnable( YouBike youbike){
        this.youbike=youbike;
    }

    @Override
    public void run() {
        String urlcity=getUrl_City(youbike.getLat(), youbike.getLng());
        openHttp(urlcity);
        readCity();

        while(flag){
            try {
                Thread.sleep(100);
                openHttp(urlcity);
                readCity();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

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

    public void readCity(){
        String line="";
        StringBuffer sb=new StringBuffer("");
        try {
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
                int a=line.indexOf("市"),b=line.indexOf("縣");
                if(a!=-1){
                    flag=false;
                    youbike.setCity(line.substring(a-2,a+1));
                    break;
                }else if(b!=-1){
                    flag=false;
                    youbike.setCity(line.substring(b-2,b+1));
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
