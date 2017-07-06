package com.example.smartwallet.communication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.smartwallet.main.Login;
import com.example.smartwallet.main.MainActivity;
import com.example.smartwallet.tool.CommonAdapter;
import com.example.smartwallet.tool.DownloadImage;
import com.example.smartwallet.tool.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2017/3/12.
 * 好友的个人资料
 */
public class FriendInfo extends Activity {
    public static final int SHOW_RESPONSE = 0;
    private Button friendBackButton;
    private Button attentionButton;

    private ImageView friendHeadImg;
    private TextView friendName;
    private TextView friendBirthday;
    private TextView friendAddress;
    private TextView friendSex;
    private TextView friendemail;
    private String attention = "true";
    ListView listView;

    private int authorId;
    List<Data> dataList;

     Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (response.equals("true")){
                        attentionButton.setVisibility(View.GONE);
                    }
            }
            MyAdapterWithCommonAdpater listAdapter = new MyAdapterWithCommonAdpater(FriendInfo.this, dataList);
            listView = (ListView) findViewById(R.id.list_view);
            listView.setAdapter(listAdapter);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.friend_personinfo);

        getInfo();

        friendBackButton = (Button)findViewById(R.id.friend_info_back_button);
        attentionButton = (Button) findViewById(R.id.friend_info_attention);
        friendName = (TextView) findViewById(R.id.friend_info_user_name);
        friendBirthday = (TextView) findViewById(R.id.friend_info_birthday);
        friendSex = (TextView) findViewById(R.id.friend_info_sex);
        friendemail = (TextView) findViewById(R.id.friend_info_email);
        friendAddress = (TextView) findViewById(R.id.friend_info_address);
        friendHeadImg = (ImageView) findViewById(R.id.friend_info_head_image);

        Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String headImage=bundle.getString("headImage");//getString()返回指定key的值
        String userName = bundle.getString("userName");
        String birthday = bundle.getString("friendsBirthday");
        String sex = bundle.getString("friendsSex");
        String address = bundle.getString("friendsAddress");
        String email = bundle.getString("email");
        authorId = bundle.getInt("id");

        new DownloadImage().setImage(friendHeadImg,headImage);

        friendName.setText(userName);
        friendBirthday.setText(birthday);
        friendSex.setText(sex);
        friendAddress.setText(address);
        friendemail.setText(email);

        if (attention.equals("true")){
            attentionButton.setVisibility(View.INVISIBLE);
        }

        friendBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent1 = new Intent();
                intent1.setClass(FriendInfo.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        attentionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String path = Login.IP + "addFriend/"+ authorId;
                        try {
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            // 设置必要的参数
                            conn.setRequestMethod("POST");
                            conn.setConnectTimeout(5000);
                            conn.setReadTimeout(5000);

                            String content = "id=" + Login.USER.getId();
                            OutputStream out = conn.getOutputStream();
                            out.write(content.getBytes());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                sb.append(str);
                            }
                            Log.e("tag", sb.toString());



                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }
        });
    }


    class MyAdapterWithCommonAdpater extends CommonAdapter<Data> {

        public MyAdapterWithCommonAdpater(Context context, List<Data> datas) {
            super(context, datas, R.layout.view2_item);

        }

        @Override
        public void convert(ViewHolder holder, final Data data) {

            Log.d("tag",data.getName());
            holder.setText(R.id.username, data.getName());
            holder.setText(R.id.date,data.getDate().replaceAll("\"",""));

            holder.setText(R.id.item_title,data.getTitle());

            ImageView headImage = holder.getView(R.id.head_image);
            if (!data.getHeadImage().equals("")) {
                new DownloadImage().setImage(headImage, data.getHeadImage());
            }
            holder.setText(R.id.item_text, data.getItemText());

            ImageView itemImage = holder.getView(R.id.item_image);
            if (!data.getItemImage().equals("")) {
                itemImage.getLayoutParams().width = 200;
                itemImage.getLayoutParams().height = 200;
                new DownloadImage().setImage(itemImage, data.getItemImage());
            }


            Log.d("tag",data.getCommentText().toString() + "评论内容设置listview");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    FriendInfo.this, android.R.layout.simple_list_item_1, data.getCommentText());
            ListView listView = holder.getView(R.id.comment_text);
            Log.d("tag","设置评论list");
            listView.setAdapter(adapter);
            TextView commentButton = holder.getView(R.id.comment_button);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FriendInfo.this, EditComment.class);
                    startActivity(intent);
                }
            });


        }

    }



    private void getInfo() {
        Thread t = new Thread() {
            public void run() {
                String path = Login.IP +"listBlog/" + authorId;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置必要的参数
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    Log.d("tag","connected");

                    String myId = Login.USER.getId().toString();

                    OutputStream out = conn.getOutputStream();
                    String content = "id=" + myId + "friendId=" + authorId ;
                    out.write(content.getBytes());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    Log.d("tag", sb.toString());

                    String[] s = sb.toString().split("/");

                    attention = s[1];

                    parseJSONWithGSON(s[0]);
                    parseJSONWithGSON(sb.toString());


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }


    private void parseJSONWithGSON(String jsonData) {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:SS").create();
        dataList = new ArrayList<Data>();
        Log.d("tag","开始解析json文件----------------------------" );

        List<Blog> blogList = gson.fromJson(jsonData, new TypeToken<List<Blog>>(){}.getType());

        for (Blog blog : blogList) {
            Log.d("tag",blog.toString());
            Data dataObject = new Data();
            String title = blog.getTitle();
            String itemText = blog.getContent();
            String itemImageUrl = blog.getPictures();
            Date sendDate = blog.getSendTime();


            String name = blog.getAuthor().getName();
            String headImageUrl = blog.getAuthor().getAvatar();
            String phone = blog.getAuthor().getPhone();
            String email = blog.getAuthor().getEmail();
            int authorId = blog.getAuthor().getId();


            Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:SS").create();
            String time = gson1.toJson(sendDate);
            Log.d("tag",time);

            Set<Comment> commentList = blog.getComments();
            List<String> commentData = new ArrayList<String>();
            for (Comment comment1 : commentList) {

                String replierName = comment1.getReplier().getName();
                String replierCotent = comment1.getContent();
                Log.d("tag", replierName + ":" + replierCotent + "\n");
                commentData.add(replierName + ":" + replierCotent);
            }
            Log.d("tag",commentData.toString());

            dataObject.setName(name);
            dataObject.setTitle(title);
            Log.d("tag","标题：" + blog.getTitle());
            dataObject.setHeadImage(headImageUrl);
            dataObject.setItemText(itemText);
            dataObject.setCommentText(commentData);
            dataObject.setItemImage(itemImageUrl);
            dataObject.setDate(time);
            Log.d("tag","时间：" +time);
            dataObject.setPhone(phone);
            dataObject.setEmail(email);
            dataObject.setAuthorId(authorId);

            dataList.add(dataObject);
            Log.d("tag", dataObject.toString() + 1);
            Log.d("tag","输出dataObject");
        }


        Log.d("tag","输出list集合-----------------");
        Log.d("tag",blogList.toString());

        Message message = new Message();
        message.what = SHOW_RESPONSE;
        message.obj = attention;
        handler.sendMessage(message);

    }
}
