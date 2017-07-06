package com.example.smartwallet.personalCenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.smartwallet.communication.Data;
import com.example.smartwallet.main.Login;
import com.example.smartwallet.tool.CommonAdapter;
import com.example.smartwallet.tool.DownloadImage;
import com.example.smartwallet.tool.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lenovo on 2016/9/10.
 * 好友列表
 */
public class Friends extends Activity {

        List<FriendsData> friendsDataList;
    Button friendsBackButton;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MyAdapterWithCommonAdpater listAdapter = new MyAdapterWithCommonAdpater(Friends.this, friendsDataList);
            ListView listView = (ListView) findViewById(R.id.my_friends_list);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    intent.setClass(Friends.this,Chat.class);
                    startActivity(intent);
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);

        friendsBackButton = (Button) findViewById(R.id.friends_back_button);
        friendsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getInfo();

    }

    class MyAdapterWithCommonAdpater extends CommonAdapter<FriendsData> {

        public MyAdapterWithCommonAdpater(Context context, List<FriendsData> datas) {
            super(context, datas, R.layout.friends_item);

        }

        @Override
        public void convert(ViewHolder holder, FriendsData friendsData) {
            holder.setText(R.id.friends_list_name,friendsData.getFriendsName());

            ImageView friendsHeadImage = holder.getView(R.id.friends_head_image);
                new DownloadImage().setImage(friendsHeadImage,friendsData.getFriendsHeadImage());
        }
    }

    private void getInfo() {
        Thread t = new Thread() {
            public void run() {
                String path = Login.IP + "listFriends/"+ Login.USER.getId().toString();
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
                    Log.e("tag", sb.toString());
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

    private List<Data> parseJson(String json) {
        try{
            friendsDataList = new ArrayList<FriendsData>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d("tag","for循环。。。。。。"+i);
                FriendsData dataObject = new FriendsData();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String friendsName = jsonObject.getString("name");
                String friendsHeadImage = jsonObject.getString("avatar");

                dataObject.setFriendsName(friendsName);
                dataObject.setFriendsHeadImage(friendsHeadImage);

                friendsDataList.add(dataObject);
                Log.e("tag", dataObject.toString()+".........SS");
            }
            Log.e("tag",friendsDataList.toString());
            handler.sendEmptyMessage(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
