package com.example.user.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.user.myapplication.GoogleMap.MapsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FunctionListener{


    Button btn_weather,btn_pm,btn_youbike,btn_googlemap;
;

    ArrayList<YouBike> youBikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        admob();
        btn_weather = (Button) findViewById(R.id.button_weather);
        btn_pm = (Button) findViewById(R.id.button_pm);
        btn_youbike = (Button) findViewById(R.id.button_youbike);
        btn_googlemap=(Button)findViewById(R.id.button_googlemap) ;


        btn_weather.setOnClickListener(btnw);
        btn_pm.setOnClickListener(btnp);
        btn_youbike.setOnClickListener(btny);
        btn_googlemap.setOnClickListener(btgooglemap);


        InputYouBike();

    }

    private void admob(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB")
                .addTestDevice("BD34A9A0939A0A4AF862F98AB60A85E4")
                .build(); //測試用廣告
        //我的ASUS手機 BD34A9A0939A0A4AF862F98AB60A85E4
        Log.d("DEVICE_ID_EMULATOR",AdRequest.DEVICE_ID_EMULATOR);
        mAdView.loadAd(adRequest);
    }

    private OnClickListener btnw = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("http://www.cwb.gov.tw/V7/forecast/week/week.htm");//交通部中央氣象局
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    };
    private OnClickListener btnp = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("http://taqm.epa.gov.tw/taqm/tw/default.aspx");//行政院環保署空氣品質監測網
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    };


    private OnClickListener btny = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private OnClickListener btgooglemap = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("youBikes",youBikes);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


    public void InputYouBike(){
        YoubikeBW youbikeBW=new YoubikeBW(MainActivity.this);
        youbikeBW.execute();
    }

    @Override
    public void setYoubike(ArrayList<YouBike> youbike) {
        youBikes=youbike;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
