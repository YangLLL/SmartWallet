package com.example.smartwallet.communication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartwallet.main.Login;
import com.example.smartwallet.main.MainActivity;
import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2017/2/26.
 * 发评论页面
 */

public class EditComment extends Activity{

    public static final int SHOW_RESPONSE = 0;
    private Button cancelButton;
    private Button commentSendButton;
    private TextView editCommentText;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (response.equals("true")){

                        Toast.makeText(EditComment.this,"发送成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(EditComment.this, MainActivity.class);
                        startActivity(intent);
                    }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comment);

        cancelButton = (Button) findViewById(R.id.edit_comment_cancel);
        commentSendButton = (Button) findViewById(R.id.edit_comment_send);
        editCommentText = (TextView) findViewById(R.id.edit_comment_text);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditComment.this,"评论发送",Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String path = "http://222.196.193.166:8080/SmartWallet/addComment";
                        try {
                            URL url = new URL(path);
                            Log.d("tag","发送请求");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setConnectTimeout(5000);
                            conn.setReadTimeout(5000);

                            Intent intent = getIntent();
                            Bundle bundle=intent.getExtras();
                            String authorId = bundle.getString("authorId");
                            String blogId = bundle.getString("blogId");

                            OutputStream out = conn.getOutputStream();
                            String content = "content=" + editCommentText.getText()
                                    + "&writerId=" + authorId + "&replierId="+ Login.USER.getId() + "&blogId=" + blogId;
                            out.write(content.getBytes());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            Log.d("tag", sb.toString());
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = sb.toString();
                            handler.sendMessage(message);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                finish();
            }
        });
    }

}
