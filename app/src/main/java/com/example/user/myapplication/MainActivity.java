
package com.example.user.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.user.myapplication.GoogleMap.MapsActivity;
import com.example.user.myapplication.Youbike.YouBike;
import com.example.user.myapplication.Youbike.YouBikeRunnable;
import com.example.user.myapplication.Youbike.YoubikeBW;
import com.example.user.myapplication.Youbike.testBW;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity implements FunctionListener{

    public static final String TAG="YOUBIKE";

    Button btn_weather,btn_pm,btn_googlemap,btn_list,btn_nb;
    ArrayList<YouBike> youBikes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        InputYouBike();
        admob();
        init();

    }

    public void InputYouBike(){
        YoubikeBW youbikeBW=new YoubikeBW(MainActivity.this,this);
        youbikeBW.execute();
    }

    public void init(){
        btn_weather = (Button) findViewById(R.id.button_weather);
        btn_pm = (Button) findViewById(R.id.button_pm);
        btn_googlemap=(Button)findViewById(R.id.button_googlemap);
        btn_list=(Button)findViewById(R.id.button_list);
        btn_nb=(Button)findViewById(R.id.button_nb);

        btn_weather.setOnClickListener(btnw);
        btn_pm.setOnClickListener(btnp);
        btn_googlemap.setOnClickListener(btgooglemap);
        btn_list.setOnClickListener(btlist);
        btn_nb.setOnClickListener(btnbook);

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

    private OnClickListener btnbook = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,NoteBookActivity.class);/*開啟筆記本功能*/
            startActivity(intent);
        }
    };

    private OnClickListener btgooglemap = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable(TAG,youBikes);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private OnClickListener btlist=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,ListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable(TAG,youBikes);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    public void setYoubike(ArrayList<YouBike> youbike) {
        youBikes=youbike;
    }

    @Override
    public void setYouBikeCity(ArrayList<YouBike> youbike){
        testBW t=new testBW(this,youbike);
        //t.execute();

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
        if (id == R.id.maker) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("製作者名單")
                    .setMessage("D0411303 黃柏勛\nD0544874 曹思璐\nD0577302 林峻逸\nD0514534 蔡尚霖")
                    .setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
