package com.example.wetalk.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wetalk.Friends;
import com.example.wetalk.R;
import com.example.wetalk.server.ParaseData;
import com.example.wetalk.server.ServerManager;
import com.example.wetalk.view.Register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addActivity extends AppCompatActivity {
    private ServerManager serverManager = ServerManager.getServerManager();
    private EditText afriendID;
    private Button btnAdd;

    String ack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        afriendID = (EditText)findViewById(R.id.add_friendID);
        btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int friendID = Integer.parseInt(afriendID.getText().toString());
                SharedPreferences pref=getSharedPreferences("chatInfo",MODE_PRIVATE);
                int userID=pref.getInt("userID",0);
                addFriend(userID,friendID);
            }
        });
    }

    private void addFriend(final int userID, final int friendID){
        String friendName = null;
        if(friendID == 0){
            Toast.makeText(getApplicationContext(), "check your input!!", Toast.LENGTH_SHORT).show();
            return;
        }
        friendName = ParaseData.getUserName(addActivity.this,friendID);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(null);//设置图标, 这里设为空值
        builder.setTitle("Add friend");
        builder.setMessage("Do you want to add "+ friendName +" as friend?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface arg0, int arg1){
                final String msg = "[ADDFRIEND]:[" + userID + ", " + friendID + "]";
                serverManager.sendMessage(addActivity.this, msg);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ack = serverManager.getMessage();
                Log.d("TAG", "ack : " + ack);
                if (ack == null) {
                    Toast.makeText(getApplicationContext(), "server not answer!",Toast.LENGTH_SHORT).show();
                    return;
                }
                serverManager.setMessage(null);
                String p = "\\[ACKADDFRIEND\\]:\\[(.*)\\]";
                Pattern pattern = Pattern.compile(p);
                Matcher matcher = pattern.matcher(ack);
                if(matcher.find() && matcher.group(1).equals("1")){
                    Friends newfriend = new Friends(ParaseData.getUserName(addActivity.this,friendID),friendID,R.drawable.tx);
                    MainActivity.friendsList.add(newfriend);
                    Message message = new Message();
                    message.what = 1;
                    MainActivity.handler.sendMessage(message);
                    Toast.makeText(getApplicationContext(), "add success!!!",Toast.LENGTH_LONG).show();
                }
                if(matcher.find() && matcher.group(1).equals("0")){
                    Toast.makeText(getApplicationContext(), "add failed!!!",Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0,int arg1){

            }
        });
        AlertDialog b = builder.create();
        b.show();//显示对话框
    }
}
