package com.example.wetalk.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wetalk.activity.MainActivity;
import com.example.wetalk.R;
import com.example.wetalk.server.ServerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private ServerManager serverManager = ServerManager.getServerManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText log_userID=(EditText) findViewById(R.id.log_userID);
        final EditText log_passoword=(EditText) findViewById(R.id.log_passoword);

        serverManager.start();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button btn_login=(Button)findViewById(R.id.btn_log_login);
        Button btn_register=(Button)findViewById(R.id.btn_log_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userID = Integer.parseInt(log_userID.getText().toString());
                String password = log_passoword.getText().toString();
                if(login(userID,password)){
                    SharedPreferences.Editor editor=getSharedPreferences("chatInfo",MODE_PRIVATE).edit();
                    editor.putInt("userID",userID);
                    editor.apply();
                    serverManager.setUserID(userID);
                    Intent intent=new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Login failed, please try again!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }

    Boolean login(int userID,String password){
        if ( userID==0 || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "check your input!!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            final String msg = "[LOGIN]:[" + userID +  ", " + password + "]";
            serverManager.sendMessage(Login.this, msg);
            String ack = serverManager.getMessage();
            Log.d("TAG", "ack : " + ack);
            if (ack == null) {
                Toast.makeText(getApplicationContext(), "server not answer!",Toast.LENGTH_SHORT).show();
                return false;
            }
            serverManager.setMessage(null);
            String p = "\\[ACKLOGIN\\]:\\[(.*)\\]";
            Pattern pattern = Pattern.compile(p);
            Matcher matcher = pattern.matcher(ack);
            /*if(matcher.find() && matcher.group(1).equals("1")){
                Toast.makeText(getApplicationContext(), "Login successfully!!",Toast.LENGTH_SHORT).show();
            }
            if(matcher.find() && matcher.group(1).equals("0")){
                Toast.makeText(getApplicationContext(), "Login failed!!!",Toast.LENGTH_SHORT).show();
            }*/
            return matcher.find() && matcher.group(1).equals("1");
        }
    }
}
