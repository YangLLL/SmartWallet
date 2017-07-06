package com.example.smartwallet.personalCenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.smartwallet.tool.Message;
import com.example.smartwallet.tool.Msg;
import com.example.smartwallet.tool.MsgAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pusher.java_websocket.client.WebSocketClient;
import com.pusher.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2016/11/23.
 * 聊天
 */
public class Chat extends Activity {

    private Button chatBackButton;
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    private WebSocketClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        chatBackButton = (Button) findViewById(R.id.chat_back_button);

        connect();
        initMsgs();
        adapter = new MsgAdapter(Chat.this,R.layout.chat_msg_item,msgList);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    try {
                        Message message = new Message();
                        message.setSender(1);
                        message.setReceiver(2);
                        message.setSendDate(new Date());
                        message.setContent(content);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:SS").create();
                        String contentMsg = gson.toJson(message);
                        client.send(contentMsg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        Msg msg = new Msg(content, Msg.TYPE_SENT);
                        msgList.add(msg);
                        adapter.notifyDataSetChanged();
                        msgListView.setSelection(msgList.size());
                        inputText.setText("");
                    }
                }
            }
        });
       chatBackButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }



    private void connect() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {

            @Override
            public void run() {


                client = new WebSocketClient(URI.
                        create("ws://222.196.193.67:8080/SmartWallet/ws?"+ 1 +":" + 2)) {

                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.d("tag","client onOpen");
                    }

                    @Override
                    public void onMessage(String s) {
                        System.out.println("client onMessage:" + s);
                        Msg msg = new Msg(s, Msg.TYPE_RECEIVED);
                        msgList.add(msg);
                        adapter.notifyDataSetChanged();
                        msgListView.setSelection(msgList.size());
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        System.out.println("client onClose:" + i + " " + s + " " + b);
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("client onError:" + e);
                    }



                };

                client.connect();
            }
        });


    }

    private void initMsgs() {
        // TODO Auto-generated method stub
        Msg msg1 = new Msg("Hello guy.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello Who is that?",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom.Nice to talking to you.",Msg.TYPE_RECEIVED);
        msgList.add(msg3);

    }
}


