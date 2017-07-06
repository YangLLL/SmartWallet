package com.example.smartwallet.bluetooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Luhao on 2016/9/28.
 * 控制蓝牙钱包
 */
public class BltSocketActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView shake, bright, sing;
    private Button cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getActionBar();
//        if(actionBar != null){
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        setContentView(R.layout.bluetooth_kz);
        shake = (TextView) findViewById(R.id.shake);
        bright = (TextView) findViewById(R.id.bright);
        sing = (TextView) findViewById(R.id.sing);
        cancel = (Button) findViewById(R.id.blue_cancel);
        cancel.setOnClickListener(this);
        shake.setOnClickListener(this);
        bright.setOnClickListener(this);
        sing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shake:
                sendMessage("a");
                Toast.makeText(BltSocketActivity.this,"已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bright:
                sendMessage("b");
                Toast.makeText(BltSocketActivity.this,"已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sing:
                sendMessage("c");
                Toast.makeText(BltSocketActivity.this,"已发送", Toast.LENGTH_SHORT).show();
                break;
            case R.id.blue_cancel:
                finish();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    break;
                case 3://
                    break;
                case 4://
                    break;
                case 5:
                    break;
            }
        }
    };

    public void sendMessage(String message) {
        if (TabFragment1.bluetoothSocket == null || TextUtils.isEmpty(message)) return;
        try {
            message += "\n";
            OutputStream outputStream = TabFragment1.bluetoothSocket.getOutputStream();
            outputStream.write(message.getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
