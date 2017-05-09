package com.example.user.myapplication.GoogleMap;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * Created by Shang on 2017/3/11.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
