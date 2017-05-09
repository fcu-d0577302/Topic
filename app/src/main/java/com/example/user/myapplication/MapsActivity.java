package com.example.user.myapplication;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Intent intent;
    ArrayList<YouBike> youBikes;

    private Button navigateBt;
    private Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    protected static final String TAG="MapsActivity";

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    static int Navigation=0;         //0=非導航 1=導航模式
    static double MyLat=0;
    static double MyLng=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent=getIntent();
        youBikes=(ArrayList<YouBike>) intent.getSerializableExtra("youBikes");
        //buildGoogleApiClient();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int i=0;i<youBikes.size();i++){               //匯入所有的youbike
            LatLng local=new LatLng(youBikes.get(i).getLat(),youBikes.get(i).getLng());
            //mMap.addMarker(new MarkerOptions().position(local).title(youBikes.get(i).getSna()));
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(local)
                    .title(youBikes.get(i).getSna())
                    .snippet(getYoubikeInfo(youBikes.get(i)));
            mMap.addMarker(markerOptions);

        }

        mMap.setInfoWindowAdapter(InfoWindowAdapter);      //點下去跑出資訊視窗
        mMap.setMyLocationEnabled(true);                    //點下去移動我的位置

        LatLng fcu=new LatLng(24.178808,120.646797);         //預設逢甲大學
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fcu,12));

    }

    private String getYoubikeInfo(YouBike youBike){
        StringBuffer sb=new StringBuffer("");
        sb.append(youBike.getSarea()+"\n")
                .append(youBike.getAr()+"\n")
                .append(youBike.getSbi()+"\n")
                .append(youBike.getBemp()+"\n")
                .append(youBike.getMday()+"\n");
        return sb.toString();
    }

    GoogleMap.InfoWindowAdapter InfoWindowAdapter=new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {          //如果這個false 才會呼叫上面的
            String youbikeTitle=marker.getTitle();
            String youbikeInfo[]=marker.getSnippet().split("\n");

            View viwe=MapsActivity.this.getLayoutInflater().inflate(R.layout.infolayout,null);
            TextView tv1=(TextView) viwe.findViewById(R.id.ybTv1);
            TextView tv2=(TextView) viwe.findViewById(R.id.ybTv2);
            TextView tv3=(TextView) viwe.findViewById(R.id.ybTv3);
            TextView tv4=(TextView) viwe.findViewById(R.id.ybTv4);
            TextView tv5=(TextView) viwe.findViewById(R.id.ybTv5);
            TextView tv6=(TextView) viwe.findViewById(R.id.ybTv6);

            tv1.setText("場站名稱:"+youbikeTitle);
            tv2.setText("場站區域:"+youbikeInfo[0]);
            tv3.setText("地址:"+youbikeInfo[1]);
            tv4.setText("可借車位數:"+youbikeInfo[2]);
            tv5.setText("可還空位數:"+youbikeInfo[3]);
            tv6.setText("資料更新時間:"+youbikeInfo[4]);
            return viwe;
        }
    };









}
