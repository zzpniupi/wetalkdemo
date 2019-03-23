package com.example.wetalk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.wetalk.R;

public class FriendsActivity extends AppCompatActivity {
    private String data[]={"1","2","3","4","5","6","7","8","9","10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab02);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(FriendsActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listview=(ListView)findViewById(R.id.lsv_friends);
        listview.setAdapter(adapter);
    }
}
