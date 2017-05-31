package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.myapplication.Youbike.YouBike;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {


    Intent YoubileIntent;
    ArrayList<YouBike> youBikes;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        YoubileIntent=getIntent();
        if(YoubileIntent!=null)
            youBikes=(ArrayList<YouBike>) YoubileIntent.getSerializableExtra(MainActivity.TAG);

        listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter(this));


    }

    static class ViewHolder{
        TextView list_head,list_sbi,list_bemp,list_time;
    }

    class MyAdapter extends BaseAdapter{
        LayoutInflater mLayInf;

        public MyAdapter(Context context){
            mLayInf=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return youBikes.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder viewHolder;
            if(convertView==null){
                convertView=mLayInf.inflate(R.layout.itemlayout,parent,false);
                viewHolder=new ViewHolder();
                viewHolder.list_head=(TextView)convertView.findViewById(R.id.list_head);
                viewHolder.list_sbi=(TextView)convertView.findViewById(R.id.list_sbi);
                viewHolder.list_bemp=(TextView)convertView.findViewById(R.id.list_bemp);
                viewHolder.list_time=(TextView)convertView.findViewById(R.id.list_time);

                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder)convertView.getTag();
            }
            YouBike youBike=youBikes.get(position);
            viewHolder.list_head.setText(youBike.getSna());
            viewHolder.list_sbi.setText(String.valueOf(youBike.getSbi()));
            viewHolder.list_bemp.setText(String.valueOf(youBike.getBemp()));
            viewHolder.list_time.setText(youBike.getMday());
            return convertView;
        }
    }

}
