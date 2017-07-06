package com.example.smartwallet.personalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartwallet.main.Login;
import com.example.myapplication.R;

/**
 * Created by lenovo on 2016/8/26.
 *显示个人信息
 */
public class PersonalInfo extends Activity {

    private Button backButton;
    private Button editButton;

    private TextView nameInfo;
    private TextView phoneInfo;
    private TextView emailInfo;
    private TextView sexInfo;
    private TextView birthday;
    private TextView addressInfo;

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.personal_information);
        backButton = (Button)findViewById(R.id.back_button);
        editButton = (Button)findViewById(R.id.edit_button);
        nameInfo = (TextView) findViewById(R.id.person_info_username);
        sexInfo = (TextView) findViewById(R.id.person_info_sex);
        birthday = (TextView) findViewById(R.id.person_info_birthday);
        phoneInfo = (TextView) findViewById(R.id.person_info_phone_number);
        emailInfo = (TextView) findViewById(R.id.person_info_email);
        addressInfo = (TextView) findViewById(R.id.person_info_address);

        nameInfo.setText(Login.USER.getName());
        sexInfo.setText(Login.USER.getName());
        birthday.setText(Login.USER.getName());
        phoneInfo.setText(Login.USER.getPhone());
        emailInfo.setText(Login.USER.getEmail());
        addressInfo.setText(Login.USER.getName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PersonalInfo.this,EditPersonInfo.class);
                startActivity(intent);
            }
        });
    }

//    private void getInfo() {
//        Thread t = new Thread() {
//            public void run() {
//                String path = "http:/222.196.194.52/hello/data/data.json";
//                try {
//                    URL url = new URL(path);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    // 设置必要的参数
//                    conn.setRequestMethod("GET");
//                    conn.setConnectTimeout(5000);
//                    conn.setReadTimeout(5000);
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    StringBuilder sb = new StringBuilder();
//                    String str;
//                    while ((str = reader.readLine()) != null) {
//                        sb.append(str);
//                    }
//                    Log.d("tag", sb.toString());
//                    parseJson(sb.toString());
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        t.start();
//    }
//
//    private void parseJson(String json) {
//        try{
//            JSONArray jsonArray = new JSONArray(json);
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String name = jsonObject.getString("name");
//                String count = jsonObject.getString("count");
//                String phone = jsonObject.getString("phone");
//                String email = jsonObject.getString("email");
//                String address = jsonObject.getString("address");
//
//
//                nameInfo.setText(name);
//                countInfo.setText(count);
//                phoneInfo.setText(phone);
//                emailInfo.setText(email);
//                addressInfo.setText(address);
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
