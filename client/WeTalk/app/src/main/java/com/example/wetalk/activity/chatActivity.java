package com.example.wetalk.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wetalk.Friends;
import com.example.wetalk.Msg;
import com.example.wetalk.R;
import com.example.wetalk.adapter.MsgAdapter;
import com.example.wetalk.server.ParaseData;
import com.example.wetalk.server.ServerManager;
import com.example.wetalk.view.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chatActivity extends AppCompatActivity {
    public static List<Msg> msgList=new ArrayList<>();
    private EditText inputText;
    private Button send;
    private static RecyclerView msgRecyclerView;
    private static MsgAdapter adapter;
    private TextView friendsTextView;
    private String friendsName;
    public static int friendID = 0;
    public static int userID = 0;
    private ServerManager serverManager = ServerManager.getServerManager();
    String ack=null;        //服务器返回消息

    private static final int newMsgs=1;     //更新聊天界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        initIDInfo();
        msgList.clear();
        friendsTextView=(TextView)findViewById(R.id.tv_chat_friendsName);
        inputText=(EditText)findViewById(R.id.edt_inputText);
        send=(Button)findViewById(R.id.btn_chat_send);
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        friendsTextView.setText(friendsName);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        initMsgs();           //暂未完成
        //checkURMsgs();
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String content=inputText.getText().toString();
                if(!"".equals(content) && sendChatMsgs(userID,friendID,content)){
                    Msg msg=new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });
    }

    public static Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case newMsgs:
                    adapter.notifyItemChanged(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    break;
                default:
                    break;
            }
        }
    };

    Boolean sendChatMsgs(int userID, int friendID, String chatMsg){
        final String msg = "[CHATMSGS]:[" + userID + ", " + friendID + ", "+ chatMsg + "]";
        serverManager.sendMessage(chatActivity.this, msg);
        ack = serverManager.getMessage();
        Log.d("TAG", "ack : " + ack);
        if (ack == null) {
            Toast.makeText(getApplicationContext(), "server not answer!",Toast.LENGTH_SHORT).show();
            return false;
        }
        serverManager.setMessage(null);
        String p = "\\[ACKCHATMSGS\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        if(matcher.find() && matcher.group(1).equals("1")){
            return true;
        }
        return true;
    }

    private void initIDInfo(){
        SharedPreferences pref=getSharedPreferences("chatInfo",MODE_PRIVATE);
        friendsName=pref.getString("friendName","");
        friendID=pref.getInt("friendID",0);
        userID=pref.getInt("userID",0);
        Log.d("userID", "userID : " + userID);
    }

    private void initMsgs(){
        /*Msg msg1=new Msg("Hello, nice to meet you!",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2=new Msg("Me too!",Msg.TYPE_SEND);
        msgList.add(msg2);*/
        List<String> chatMsgs = ParaseData.getChatMsgs(chatActivity.this, userID ,friendID);
        List<String> msgsType = ParaseData.getMsgsType(chatActivity.this, userID ,friendID);
        for(int i=0;i<chatMsgs.size();i++){
            Msg msg;
            if(Integer.parseInt(msgsType.get(i))==userID){
                msg = new Msg(chatMsgs.get(i),Msg.TYPE_SEND);
            }else{
                msg = new Msg(chatMsgs.get(i),Msg.TYPE_RECEIVED);
            }
            msgList.add(msg);
            adapter.notifyItemChanged(msgList.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        }
    }

    private void checkURMsgs(){
        List<String> URMsgs = ParaseData.checkURMsgs(chatActivity.this, userID ,friendID);
        for (String URMsg : URMsgs){
            //String[] profile = ParaseData.getFriendProfile(MainActivity.this, friendID);
            Msg msg = new Msg(URMsg,Msg.TYPE_RECEIVED);
            msgList.add(msg);
            adapter.notifyItemChanged(msgList.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        }
    }
}
