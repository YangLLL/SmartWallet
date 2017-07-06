package com.example.smartwallet.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.smartwallet.communication.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 登录
 */
public class Login extends Activity {

    public static User USER = null;
    public static String IP =  "http://222.196.192.22:8080/SmartWallet/";

    public static final int SHOW_RESPONSE = 0;
    private EditText mUser;
    private EditText mPassword;
    String phone;
    String password;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;


                    if ((response.equals("密码错误"))||(response.equals("该用户不存在"))){
                        Toast.makeText(Login.this,response,Toast.LENGTH_SHORT).show();
                    }  else {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:SS").create();
                        USER = gson.fromJson(response,User.class);
                        Toast.makeText(Login.this,"登录成功！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(Login.this, MainActivity.class);
                        startActivity(intent);
                    }

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mUser = (EditText) findViewById(R.id.login_user_edit);

        mPassword = (EditText) findViewById(R.id.login_password_edit);

    }


   public void login_main(View view) {

       phone = mUser.getText().toString();
       SharedPreferences.Editor editor = getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
       editor.putString("name",phone);
       editor.apply();

       password = mPassword.getText().toString();

       new Thread(new Runnable() {
           @Override
           public void run() {
               HttpURLConnection conn = null;
               try {
                  String url = IP + "login";

                   URL httpUrl = new URL(url);
                   conn = (HttpURLConnection) httpUrl.openConnection();

                   conn.setRequestMethod("POST");
                   conn.setReadTimeout(5000);
                   conn.setConnectTimeout(5000);

                   conn.setDoInput(true);     //读取数据
                   conn.setDoOutput(true);

                   String content = "phone=" + phone + "&password=" + password;
                   Log.d("tag",content);
                   OutputStream out = conn.getOutputStream();
                   out.write(content.getBytes());
                   BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                   String str;
                   StringBuilder sb = new StringBuilder();
                   while ((str = reader.readLine()) != null) {
                       sb.append(str);

                   }
                   Log.d("tag","登录返回值：" + sb.toString());
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




    public void login_forget(View view){
        Intent intent = new Intent();
        intent.setClass(Login.this,ForgetPsw.class);
        startActivity(intent);
    }
    public void login_register(View view){
        Intent intent = new Intent();
        intent.setClass(Login.this,Register.class);
        startActivity(intent);
    }
}
