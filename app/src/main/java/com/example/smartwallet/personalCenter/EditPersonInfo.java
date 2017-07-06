package com.example.smartwallet.personalCenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartwallet.main.Login;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2016/9/24.
 * 修改个人资料
 */
public class EditPersonInfo extends Activity {

    public static final int SHOW_RESPONSE = 0;
    private Button editBackButton;
    private Button editSaveButton;
    private EditText editName;
    private EditText editCount;
    private EditText editPhone;
    private EditText editEmail;
    private EditText editAddress;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;

                    if (response.equals("true")){
                        Toast.makeText(EditPersonInfo.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_info);

        editName = (EditText) findViewById(R.id.edit_info_name);
//        editCount = (EditText) findViewById(R.id.edit_info_count);
        editPhone = (EditText) findViewById(R.id.edit_info_phone);
        editEmail = (EditText) findViewById(R.id.edit_info_email);
        editAddress = (EditText) findViewById(R.id.edit_info_address);
        editSaveButton = (Button) findViewById(R.id.edit_info_save);
        editBackButton = (Button) findViewById(R.id.edit_info_back_button);



        getInfo();
        editBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((editName.getText().toString()!="")&&(editCount.getText().toString()!="")&&
                        (editPhone.getText().toString()!="")&&(editEmail.getText().toString()!="")&&(
                        editAddress.getText().toString()!="")) {
                    Save_info();
                }
                else {
                    Toast.makeText(EditPersonInfo.this,"请输入所有的个人资料",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void Save_info() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Login.IP + "register";
                String name = editName.getText().toString();
                String count = editCount.getText().toString();
                String phone = editPhone.getText().toString();
                String em = editEmail.getText().toString();
                String address = editAddress.getText().toString();


                HttpURLConnection conn = null;
                try {
                    URL httpUrl = new URL(url);
                    conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);


                    OutputStream out = conn.getOutputStream();
                    String content = "name=" + name + "&password=" + count +
                            "&email=" + em + "&phone=" + phone + "&address=" + address;

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


    private void getInfo() {
        Thread t = new Thread() {
            public void run() {
                String path = "http://222.196.202.107/hello/data/data.json";
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置必要的参数
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    Log.d("tag", sb.toString());
                    parseJson(sb.toString());


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void parseJson(String json) {
        try{
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String count = jsonObject.getString("count");
                String phone = jsonObject.getString("phone");
                String email = jsonObject.getString("email");
                String address = jsonObject.getString("address");

                editName.setText(name);
                editCount.setText(count);
                editPhone.setText(phone);
                editEmail.setText(email);
                editAddress.setText(address);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
