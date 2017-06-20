package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
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

    public static final String SPTAG="FAVORITE";

    Intent YoubikeIntent;
    ArrayList<YouBike> youBikes;
    ArrayList<YouBike> copyYouBikes=new ArrayList<>();
    ArrayList<ArrayList<YouBike>> city=new ArrayList<>();

    MyAdapter myAdapter;
    final String cityName[]={"台北","基隆","新竹","桃園","台中","彰化","常用"};


    ListView listView;
    Spinner spinner;

    SharedPreferences sp;
    int pos=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        YoubikeIntent=getIntent();
        if(YoubikeIntent!=null){
            youBikes=(ArrayList<YouBike>) YoubikeIntent.getSerializableExtra(MainActivity.TAG);
            for(int i=0;i<youBikes.size();i++){
                copyYouBikes.add(youBikes.get(i));     //如果用跟youbikes依樣那會項指標一樣,youbikes被清空,自己也會被清空,因為是指向一樣的地方
            }
        }

        SortCity();
        init();
    }

    public void init(){
        listView=(ListView)findViewById(R.id.listview);
        myAdapter=new MyAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(listOnItemClick);
        listView.setOnItemLongClickListener(listOnItemLongClick);

        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                cityName));
        spinner.setOnItemSelectedListener(spinnerItemClick);

        sp=getSharedPreferences("data",0);


    }

    public void load(){

        sp=getSharedPreferences("data",0);
        String value=sp.getString("Value","0");
        for(int i=0;i<Integer.parseInt(value);i++){
            System.out.println(sp.getInt(String.valueOf(i),100));
            System.out.println(sp.getString("sp"+String.valueOf(i),"NULL"));
            String spName=sp.getString("sp"+String.valueOf(i),"NULL");         //站名

            for(int j=0;j<copyYouBikes.size();j++){
                System.out.println(copyYouBikes.get(j).getSna());
                if(copyYouBikes.get(j).getSna().equals(spName)==true){
                    youBikes.add(copyYouBikes.get(j));
                    break;
                }
            }
        }
    }


    AdapterView.OnItemClickListener listOnItemClick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(ListActivity.this, MapsActivity.class);
            Bundle bundle=new Bundle();
            bundle.putDouble("Lat",youBikes.get(position).getLat());
            bundle.putDouble("Lng",youBikes.get(position).getLng());
            bundle.putSerializable(MainActivity.TAG,copyYouBikes);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener listOnItemLongClick=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if(sp.getString("Value","NULL").equals("NULL")){      //第一次新增,防止下次進入頁面時又是從0開始
                sp.edit().putString("Value","0").commit();
                Log.v(SPTAG,sp.getString("Value","NULL")+"");
            }

            int v=Integer.parseInt(sp.getString("Value","0"));
            sp.edit().putInt(String.valueOf(v),v).commit();
            sp.edit().putString("sp"+String.valueOf(v),city.get(pos).get(position).getSna()).commit();   //不能跟INT的KEY重複
            Log.v(SPTAG,sp.getInt(String.valueOf(v),v)+"");
            Log.v(SPTAG,sp.getString("sp"+String.valueOf(v),"NULL"));

            sp.edit().putString("Value",String.valueOf(v+1)).commit();        //更新到下一筆

            Toast.makeText(ListActivity.this,city.get(pos).get(position).getSna()+" 加入常用站點",Toast.LENGTH_SHORT).show();

            return true;
        }
    };

    AdapterView.OnItemSelectedListener spinnerItemClick=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            youBikes.clear();
            pos=position;        //紀錄現在是哪一個縣市,長壓的時候要用到

            if(position==cityName.length-1){
                load();
            }else{
                for(int i=0;i<city.get(position).size();i++){
                    youBikes.add(city.get(position).get(i));
                }
            }

            myAdapter.notifyDataSetChanged();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };




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

            //Log.v("TAG",youBike.getMday());
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
