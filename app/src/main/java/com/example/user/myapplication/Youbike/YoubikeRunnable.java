package com.example.user.myapplication.Youbike;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myapplication.FunctionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Shang on 2017/5/17.
 */
public class YoubikeRunnable{

    FunctionListener functionListener;
    int size=0;

    public YoubikeRunnable(FunctionListener functionListener,int size){
        this.functionListener=functionListener;
        this.size=size;
    }

    public JsonObjectRequest getRequest(final YouBike youBike, final int i){
        String url=getUrl_City(youBike.getLat(),youBike.getLng());
        Log.d("URL",url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String city="";
                        String json=response.toString();
                        Log.d("josn",json);
                        JSONObject object=null,results_object=null,ac_object=null;
                        JSONArray results=null,ac_array=null;
                        try{
                            object=new JSONObject(json);
                            results=object.getJSONArray("results");
                            Log.d("results",results.length()+"");  //幹你的google api調用居然有速率限制 你他阿罵開好 每秒最多10個要求
                            results_object=results.getJSONObject(0);             //會有問題
                            ac_array=results_object.getJSONArray("address_components");
                            ac_object=ac_array.getJSONObject(4);                      //有可能是3
                            city=ac_object.getString("short_name");
                            Log.d("City",city);
                        }catch (JSONException e){
                        }
                        youBike.setCity(city);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        return jsonObjectRequest;
    }

    public String getUrl_City(double lat,double lng){
        StringBuffer url_city=new StringBuffer("");
        url_city.append("https://maps.google.com/maps/api/geocode/json?latlng=")
                .append(lat)
                .append(",")
                .append(lng)
                .append("&language=zh-TW&sensor=true");
        //Log.d("URL",url_city.toString());
        return url_city.toString();
    }

}
