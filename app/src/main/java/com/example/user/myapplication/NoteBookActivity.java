package com.example.user.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by User on 2017/6/14.
 */

public class NoteBookActivity extends AppCompatActivity {

    Button btn_addnote;
    ListView lv_notes;
    SQLiteDatabase db;
    ArrayList<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        btn_addnote = (Button)findViewById(R.id.btn_addnote);
        btn_addnote.setOnClickListener(addnote);

        lv_notes = (ListView)findViewById(R.id.lv_notes);
        lv_notes.setOnItemClickListener(iclick);
        lv_notes.setOnItemLongClickListener(ilclick);
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DBOpenHelper openhelper = new DBOpenHelper(this);
        db = openhelper.getWritableDatabase();

        titlelist = NoteDB.getTitleList(db);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titlelist);
        lv_notes.setAdapter(adapter);
    }

    OnClickListener addnote = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(NoteBookActivity.this,
                    NoteEditor.class);
            intent.putExtra("NOTEPOS", -1);
            startActivity(intent);
        }
    };

    AdapterView.OnItemClickListener iclick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View v,
                                int position, long id) {
            Intent intent = new Intent();
            intent.setClass(NoteBookActivity.this,
                    NoteEditor.class);
            intent.putExtra("NOTEPOS", position);
            startActivity(intent);
        }
    };

    AdapterView.OnItemLongClickListener ilclick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> av, View v,
                                       int position, long id) {
            String title = titlelist.get(position);
            NoteDB.delNote(db, title);
            titlelist = NoteDB.getTitleList(db);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (NoteBookActivity.this,
                            android.R.layout.simple_list_item_1, titlelist);
            lv_notes.setAdapter(adapter);
            return false;
        }

    };
}
