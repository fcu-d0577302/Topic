package com.example.user.myapplication.GoogleMap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.MainActivity;
import com.example.user.myapplication.R;
import com.example.user.myapplication.Youbike.YouBike;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
        ,DirectionFinderListener{

    private GoogleMap mMap;

    Intent intent;
    ArrayList<YouBike> youBikes;

    private ImageButton navigateBt,backBt;
    private Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    protected static final String TAG="MapsActivity";

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    protected static int Navigation=0;         //0=非導航 1=導航模式
    protected static double MyLat=0;
    protected static double MyLng=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
    }

    private void init(){
        navigateBt=(ImageButton)findViewById(R.id.gmBt);
        navigateBt.setOnClickListener(BtListener);

        backBt=(ImageButton)findViewById(R.id.back);
        backBt.setOnClickListener(BackListener);

        intent=getIntent();
        youBikes=(ArrayList<YouBike>) intent.getSerializableExtra(MainActivity.TAG);
        buildGoogleApiClient();           //打開GOOGLEMAP API 取得GPS
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int i=0;i<youBikes.size();i++){               //匯入所有的youbike
            LatLng local=new LatLng(youBikes.get(i).getLat(),youBikes.get(i).getLng());
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(local)
                    .title(youBikes.get(i).getSna())
                    .snippet(getYoubikeInfo(youBikes.get(i)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

            mMap.addMarker(markerOptions);

        }

        mMap.setMyLocationEnabled(true);                        //點下去移動我的位置  右上角的按鈕
        mMap.setInfoWindowAdapter(InfoWindowAdapter);          //marker點下去跑出資訊視窗
        mMap.setOnMarkerClickListener(markerClickListener); //marker點下發生事件

        LatLng fcu=new LatLng(24.178808,120.646797);             //預設逢甲大學
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fcu,15));

    }

    View.OnClickListener BtListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation=1;           //導航模式
            Toast.makeText(MapsActivity.this,"請點選一個站點",Toast.LENGTH_LONG).show();
        }
    };

    View.OnClickListener BackListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public String getYoubikeInfo(YouBike youBike){            //infolayout要用的資料
        StringBuffer sb=new StringBuffer("");
        sb.append(youBike.getSbi()+"\n")
                .append(youBike.getBemp()+"\n")
                .append(youBike.getMday()+"\n");
        return sb.toString();
    }

    GoogleMap.OnMarkerClickListener markerClickListener=new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(MapsActivity.Navigation==1){
                mGoogleApiClient.connect();    //可以取得最新經緯
                sendRequest(MyLat,MyLng,marker.getPosition().latitude,marker.getPosition().longitude);
                Log.d("YOUBIKE",marker.getPosition().latitude+" "+marker.getPosition().longitude);
                Navigation=0;
                return true;
            }else{
                return false;
            }
        }
    };

    GoogleMap.InfoWindowAdapter InfoWindowAdapter=new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {          //如果這個false 才會呼叫上面的
            String youbikeTitle=marker.getTitle();
            String youbikeInfo[]=marker.getSnippet().split("\n");

            View viwe=getLayoutInflater().inflate(R.layout.infolayout,null);
            TextView tv1=(TextView) viwe.findViewById(R.id.ybTv1);
            TextView tv2=(TextView) viwe.findViewById(R.id.ybTv2);
            TextView tv3=(TextView) viwe.findViewById(R.id.ybTv3);


            tv1.setText("場站名稱:"+youbikeTitle);
            tv2.setText("可借/可停:"+youbikeInfo[0]+"/"+youbikeInfo[1]);
            tv3.setText("更新時間:"+youbikeInfo[2]);
            return viwe;
        }
    };

    private void sendRequest(double myLat,double myLng,double youbikeLat,double youbikeLng) {
        String origin="origin=" + myLat + "," +myLng;
        String destination="destination=" + youbikeLat + "," + youbikeLng;
        try {
            new DirectionFinder(MapsActivity.this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(MapsActivity.this)
                .addConnectionCallbacks(MapsActivity.this)
                .addOnConnectionFailedListener(MapsActivity.this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {   //GoogleApiClient.ConnectionCallbacks介面
        mLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);  //取得目前最新位置
        if(mLocation!=null){
            MyLat=mLocation.getLatitude();
            MyLng=mLocation.getLongitude();
            Log.d(TAG,mLocation.getLatitude()+"");
            Log.d(TAG,mLocation.getLongitude()+"");
        }else{
            Toast.makeText(this,"GPS NOT OPEN",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route routes : route) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routes.startLocation, 16));

            /*originMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(routes.startAddress)
                    .position(routes.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .title("cow meal")
                    .position(routes.endLocation)));*/

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < routes.points.size(); i++)
                polylineOptions.add(routes.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

}
