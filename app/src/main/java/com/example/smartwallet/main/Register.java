package com.example.smartwallet.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

/**
 * Created by lenovo on 2016/8/16.
 * 注册
 */

public class Register extends Activity{
    public static final int SHOW_RESPONSE = 0;
    private EditText userName;
    private EditText password;
    private EditText phoneNumber;
    private EditText email;
    private Button submit;
    private Button cancel;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    Toast.makeText(Register.this,response,Toast.LENGTH_SHORT).show();
                    if (response.equals("注册成功")){
                        Intent intent = new Intent();
                        intent.setClass(Register.this, Login.class);
                        startActivity(intent);
                    }
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        userName = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        phoneNumber = (EditText)findViewById(R.id.phone_number);
        email = (EditText)findViewById(R.id.email);
        submit = (Button)findViewById(R.id.submit);
        cancel = (Button)findViewById(R.id.cancel);

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       String url = Login.IP + "register";
                       String name = userName.getText().toString();
                       String psw = password.getText().toString();
                       String phone = phoneNumber.getText().toString();
                       String em = email.getText().toString();
                       HttpURLConnection conn = null;
                       try {
                           URL httpUrl = new URL(url);
                           conn = (HttpURLConnection) httpUrl.openConnection();
                           conn.setRequestMethod("POST");
                           conn.setReadTimeout(5000);
                           conn.setConnectTimeout(5000);
                           String content = "name=" + name + "&password=" + psw + "&email=" + em +
                                   "&phone=" + phone;

                           OutputStream out = conn.getOutputStream();
                           out.write(content.getBytes());

                           BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                           String str;
                           StringBuilder sb = new StringBuilder();
                           while ((str = reader.readLine()) != null) {
                               sb.append(str);
                           }
                           Message message = new Message();
                           message.what = SHOW_RESPONSE;
                           message.obj = sb.toString();
                           handler.sendMessage(message);

                       } catch (Exception e) {
                           e.printStackTrace();
                       } finally {
                           if (conn != null) {
                               conn.disconnect();
                           }
                       }
                   }
               }).start();
           }
       });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
