package com.example.smartwallet.personalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartwallet.main.Login;
import com.example.smartwallet.main.MainActivity;
import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2016/9/24.
 * 改密码
 */
public class ChangePassword extends Activity {
    public static final int SHOW_RESPONSE = 0;

    private Button confirmButton;
    private EditText newPassword;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (response.equals("true")){
                        Toast.makeText(ChangePassword.this,"修改成功!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(ChangePassword.this, MainActivity.class);
                        startActivity(intent);
                    }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        confirmButton = (Button) findViewById(R.id.confirm_button);
        newPassword = (EditText) findViewById(R.id.new_password);



        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Login.IP + "findPass";
                        String password = newPassword.getText().toString();
                        HttpURLConnection conn = null;
                        try {
                            URL httpUrl = new URL(url);
                            conn = (HttpURLConnection) httpUrl.openConnection();

                            conn.setRequestMethod("POST");
                            conn.setReadTimeout(5000);
                            conn.setConnectTimeout(5000);
                            OutputStream out = conn.getOutputStream();

                            Intent intent=getIntent();
                            Bundle bundle=intent.getExtras();
                            String phone=bundle.getString("phone");

                            String content = "password=" + password + "&phone=" + phone;
                            out.write(content.getBytes());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String str;
                            StringBuilder sb = new StringBuilder();
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            Log.d("tag","返回值：" + sb.toString());
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

                Toast.makeText(ChangePassword.this,"消息已发送",Toast.LENGTH_SHORT).show();
            }

        });

    }
}
