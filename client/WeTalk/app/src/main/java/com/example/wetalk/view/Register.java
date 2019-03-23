package com.example.wetalk.view;

import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wetalk.R;
import com.example.wetalk.activity.MainActivity;
import com.example.wetalk.server.ServerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {
    private ServerManager serverManager = ServerManager.getServerManager();
    String ack=null;        //服务器返回消息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //serverManager.start();

        Button btn_reg_register=(Button)findViewById(R.id.btn_reg_register);
        Button btn_reg_send=(Button)findViewById(R.id.btn_reg_send);
        final EditText reg_phone_number=(EditText) findViewById(R.id.reg_phone_number);
        final EditText reg_passoword=(EditText) findViewById(R.id.reg_passoword);
        final EditText reg_nickname=(EditText) findViewById(R.id.reg_nickname);
        final EditText reg_verification=(EditText) findViewById(R.id.reg_verification);

        btn_reg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = reg_phone_number.getText().toString();
                if(send(phoneNumber)){

                }
            }
        });

        btn_reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //serverManager.run();
                String phoneNumber = reg_phone_number.getText().toString();
                String password = reg_passoword.getText().toString();
                String nickname = reg_nickname.getText().toString();
                String verification = reg_verification.getText().toString();

                if(register(phoneNumber,password,nickname,verification)){

                }
                //serverManager.sendMessage(Register.this,"hello server!");
            }
        });
    }

    Boolean send(String phoneNumber){
        if(phoneNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), "check your input!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            final String msg = "[REGISTERSEND]:[" + phoneNumber + "]";
            serverManager.sendMessage(Register.this, msg);
            ack = serverManager.getMessage();
            Log.d("TAG", "ack : " + ack);
            if (ack == null) {
                Toast.makeText(getApplicationContext(), "server not answer!",Toast.LENGTH_SHORT).show();
                return false;
            }
            serverManager.setMessage(null);
            String p = "\\[ACKREGISTERSEND\\]:\\[(.*)\\]";
            Pattern pattern = Pattern.compile(p);
            Matcher matcher = pattern.matcher(ack);
            if(matcher.find() && matcher.group(1).equals("1")){
                Toast.makeText(getApplicationContext(), "Send verification successfully!!",Toast.LENGTH_LONG).show();
            }
            if(matcher.find() && matcher.group(1).equals("0")){
                Toast.makeText(getApplicationContext(), "Send verification failed!!!",Toast.LENGTH_LONG).show();
            }
            return matcher.find() && matcher.group(1).equals("1");
        }
    }

    Boolean register(String phoneNumber,String password,String nickname,String verification){
        if (phoneNumber.isEmpty() || password.isEmpty() || nickname.isEmpty() || verification.isEmpty()) {
            Toast.makeText(getApplicationContext(), "check your input!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            final String msg = "[REGISTER]:[" + phoneNumber + ", " + password + ", "+ nickname + ", "+ verification + "]";

            //新建线程发送获取数据，避免阻塞主线程
            /*new Thread(new Runnable(){
                @Override
                public void run() {
                    serverManager.sendMessage(Register.this, msg);
                    ack = serverManager.getMessage();
                    Log.d("TAG", "ack : " + ack);
                }
            }).start();*/

            /***
             * 子线程未运行完成，主线程进入
             * ack未更新
             * 暂不创建子线程
             * */
            serverManager.sendMessage(Register.this, msg);
            ack = serverManager.getMessage();
            Log.d("TAG", "ack : " + ack);
            if (ack == null) {
                Toast.makeText(getApplicationContext(), "server not answer!",Toast.LENGTH_SHORT).show();
                return false;
            }
            serverManager.setMessage(null);
            String p = "\\[ACKREGISTER\\]:\\[(.*),(.*)\\]";
            Pattern pattern = Pattern.compile(p);
            Matcher matcher = pattern.matcher(ack);
            if(matcher.find() && matcher.group(1).equals("1")){
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Reminder");
                builder.setMessage("Register successfully,\nyour userID is:"+matcher.group(2)+"\nPlease remember it!");
                builder.setPositiveButton("got it",
                        new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                /*AlertDialog.Builder alertDialog=new AlertDialog.Builder(this).setTitle("important")
                        .setMessage("Register successfully!!\nYour userID is:"+matcher.group(2)+"\nPlease remember it!");
                Toast.makeText(getApplicationContext(), "Register successfully!!\nYour userID is:"+matcher.group(2),Toast.LENGTH_LONG).show();*/
            }
            if(matcher.find() && matcher.group(1).equals("0")){
                Toast.makeText(getApplicationContext(), "Register failed!!!",Toast.LENGTH_LONG).show();
            }
            return matcher.find() && matcher.group(1).equals("1");
        }
    }
}
