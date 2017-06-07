package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.GoogleMap.MapsActivity;
import com.example.user.myapplication.Youbike.YouBike;
import java.util.ArrayList;
import java.util.Map;


public class ListActivity extends AppCompatActivity {


    Intent YoubikeIntent;
    ArrayList<YouBike> youBikes;
    ArrayList<ArrayList<YouBike>> city=new ArrayList<>();
    MyAdapter myAdapter;
    final String cityName[]={"台北","基隆","新竹","桃園","台中","彰化"};


    ListView listView;
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        YoubikeIntent=getIntent();
        if(YoubikeIntent!=null)
            youBikes=(ArrayList<YouBike>) YoubikeIntent.getSerializableExtra(MainActivity.TAG);

        SortCity();

        listView=(ListView)findViewById(R.id.listview);

        myAdapter=new MyAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ListActivity.this, MapsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putDouble("Lat",youBikes.get(position).getLat());
                bundle.putDouble("Lng",youBikes.get(position).getLng());
                bundle.putSerializable(MainActivity.TAG,youBikes);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                cityName);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                youBikes.clear();
                for(int i=0;i<city.get(position).size();i++){
                    youBikes.add(city.get(position).get(i));
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void SortCity(){

        for(int i=0;i<cityName.length;i++){
            city.add(new ArrayList<YouBike>());
        }

        for(int i=0;i<youBikes.size();i++){
            if(!youBikes.get(i).getCity().equals(""))
            switch (youBikes.get(i).getCity()){
                case "台北市":
                case "新北市":
                    city.get(0).add(youBikes.get(i));
                    break;
                case "基隆市":
                    city.get(1).add(youBikes.get(i));
                    break;
                case "新竹市":
                    city.get(2).add(youBikes.get(i));
                    break;
                case "桃園市":
                    city.get(3).add(youBikes.get(i));
                    break;
                case "台中市":
                    city.get(4).add(youBikes.get(i));
                    break;
                case "彰化市":
                case "彰化縣":
                    city.get(5).add(youBikes.get(i));
                    break;
            }
        }

        for(int i=0;i<city.size();i++){
            for(int j=0;j<city.get(i).size();j++){
                System.out.println(city.get(i).get(j).getCity());
            }
            System.out.println("-------------------------------------------------");
        }
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
            viewHolder.list_head.setText("站點:"+youBike.getSna());
            viewHolder.list_sbi.setText("可停:"+String.valueOf(youBike.getSbi())+"/");
            viewHolder.list_bemp.setText("可還:"+String.valueOf(youBike.getBemp()));

            Log.v("TAG",youBike.getMday());
            StringBuffer sb=new StringBuffer("");
            sb.append(youBike.getMday().substring(0,4)+"/")
                    .append(youBike.getMday().substring(4,6)+"/")
                    .append(youBike.getMday().substring(6,8)+"/")
                    .append(youBike.getMday().substring(8,10)+":")
                    .append(youBike.getMday().substring(10,12)+":")
                     .append(youBike.getMday().substring(12,14));
            viewHolder.list_time.setText("更新日期:"+sb.toString());

            return convertView;
        }
    }

}
