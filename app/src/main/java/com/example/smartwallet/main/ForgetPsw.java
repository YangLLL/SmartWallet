package com.example.smartwallet.main;

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

import com.example.myapplication.R;
import com.example.smartwallet.personalCenter.ChangePassword;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2016/8/22.
 * 找回密码
 */

public class ForgetPsw extends Activity {

    public static final int SHOW_RESPONSE = 0;
    private EditText telText;
    private EditText emailText;
    private Button sendButton;
    private String sessionId;

    private EditText verificationText;
    private Button verficationBtn;
    String phone;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (response.equals("true")){
                        Toast.makeText(ForgetPsw.this,"发送成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("phone",phone);
                        intent.setClass(ForgetPsw.this, ChangePassword.class);
                        startActivity(intent);
                    }
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psw);
        telText = (EditText)findViewById(R.id.tel_text);
        phone = telText.getText().toString();

        emailText = (EditText) findViewById(R.id.email_text);
        verificationText = (EditText) findViewById(R.id.verification_code);

        sendButton = (Button)findViewById(R.id.tel_submit_btn);
        verficationBtn = (Button) findViewById(R.id.verification_btn);
        verficationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean judgephone = isMobileNO(telText.getText().toString());
                boolean judgeemail = isEmail( emailText.getText().toString());

                if ((!judgephone )||(!judgeemail)) {
                    Toast.makeText(ForgetPsw.this, "电话号码或邮箱格式不对", Toast.LENGTH_SHORT).show();
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String phone = telText.getText().toString();
                            String email = emailText.getText().toString();

                            HttpURLConnection conn = null;
                            try {
                                String url =Login.IP + "sendMail?phone=" + phone + "&email=" + email;

                                URL httpUrl = new URL(url);
                                conn = (HttpURLConnection) httpUrl.openConnection();


                                String cookieval = conn.getHeaderField("set-cookie");
                                if (cookieval != null) {
                                    sessionId = cookieval.substring(0, cookieval.indexOf(";"));
                                }
                                conn.setRequestMethod("GET");
                                conn.setReadTimeout(5000);
                                conn.setConnectTimeout(5000);


                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String str;
                                StringBuilder sb = new StringBuilder();
                                while ((str = reader.readLine()) != null) {
                                    sb.append(str);
                                }

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
            }

        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Login.IP + "findPass";

                        String verificationCode = verificationText.getText().toString();
                        HttpURLConnection conn = null;
                        try {
                            URL httpUrl = new URL(url);
                            conn = (HttpURLConnection) httpUrl.openConnection();

                                String cookieval = conn.getHeaderField("set-cookie");
                                if(cookieval != null) {
                                    sessionId = cookieval.substring(0, cookieval.indexOf(";"));
                                }
                            if (sessionId != null) {
                                conn.setRequestProperty("cookie", sessionId);
                            }

                            conn.setRequestMethod("POST");
                            conn.setReadTimeout(5000);
                            conn.setConnectTimeout(5000);

                            OutputStream out = conn.getOutputStream();
                            String content = "verification=" + verificationCode;
                            out.write(content.getBytes());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String str;
                            StringBuilder sb = new StringBuilder();
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            Log.d("tag", "返回值：" + sb.toString());
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

                Toast.makeText(ForgetPsw.this, "消息已发送", Toast.LENGTH_SHORT).show();
            }


        });


    }

    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
