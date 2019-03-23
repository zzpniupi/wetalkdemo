package com.example.wetalk.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wetalk.Msg;
import com.example.wetalk.R;
import com.example.wetalk.adapter.MsgAdapter;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {
    private List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private TextView friendsTextView;
    private String friendsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        initMsgs();
        initIDInfo();
        friendsTextView=(TextView)findViewById(R.id.tv_chat_friendsName);
        inputText=(EditText)findViewById(R.id.edt_inputText);
        send=(Button)findViewById(R.id.btn_chat_send);
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        friendsTextView.setText(friendsName);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String content=inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg=new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });
    }

    private void initIDInfo(){
        SharedPreferences pref=getSharedPreferences("chatInfo",MODE_PRIVATE);
        friendsName=pref.getString("friendName","");
    }

    private void initMsgs(){
        Msg msg1=new Msg("Hello, nice to meet you!",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2=new Msg("Me too!",Msg.TYPE_SEND);
        msgList.add(msg2);
    }
}
