package com.example.user.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button btn_weather;
    Button btn_pm;
    Button btn_youbike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_weather = (Button) findViewById(R.id.button_weather);
        btn_pm = (Button) findViewById(R.id.button_pm);
        btn_youbike = (Button) findViewById(R.id.button_youbike);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_weather.setOnClickListener(btnw);
        btn_pm.setOnClickListener(btnp);
        btn_youbike.setOnClickListener(btny);
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
            Uri uri = Uri.parse("https://www.youbike.com.tw/");//youbike微笑單車
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    };
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
