package com.example.smartwallet.communication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.smartwallet.main.Login;
import com.example.smartwallet.tool.CommonAdapter;
import com.example.smartwallet.tool.DownloadImage;
import com.example.smartwallet.tool.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

//import android.support.v4.app.Fragment;

/**
 * 主页面第二部分
 * 显示动态
 */

public class TabFragment2 extends Fragment {
    ListView listView;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MyAdapterWithCommonAdpater listAdapter = new MyAdapterWithCommonAdpater(getActivity(), dataList);
            listView = (ListView) getActivity().findViewById(R.id.list_view);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity(), "点击", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    List<Data> dataList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view2, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button editButton = (Button) getActivity().findViewById(R.id.edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditItem.class);
                startActivity(intent);
            }
        });

        getInfo();
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

            headImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), FriendInfo.class);
                    intent.putExtra("headImage",data.getHeadImage());
                    intent.putExtra("userName",data.getName());
                    intent.putExtra("friendsBirthday",data.getAuthorBirthday().replaceAll("\"",""));
                    intent.putExtra("friendsSex",data.getAuthorSex());
                    intent.putExtra("friendsAddress",data.getAuthorAddress());
                    intent.putExtra("id",data.getAuthorId());
                    startActivity(intent);
                }
            });

            holder.setText(R.id.item_text, data.getItemText());

            ImageView itemImage = holder.getView(R.id.item_image);
            if (!data.getItemImage().equals("")) {
                itemImage.getLayoutParams().width = 200;
                itemImage.getLayoutParams().height = 200;
                new DownloadImage().setImage(itemImage, data.getItemImage());
            }

            Log.d("tag",data.getCommentText().toString() + "评论内容设置listview");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_list_item_1, data.getCommentText());
            ListView listView = holder.getView(R.id.comment_text);
            Log.d("tag","设置评论list");
            listView.setAdapter(adapter);

                TextView commentButton = holder.getView(R.id.comment_button);

                commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), EditComment.class);
                        intent.putExtra("authorId",data.getAuthorId());
                        intent.putExtra("blogId",data.getBlogId());
                        startActivity(intent);
                    }
                });
        }



        }



        private void getInfo() {
            Log.d("tag", "getInfo'''''''''''''''''''''''''");
            Thread t = new Thread() {
                public void run() {
                String path = Login.IP + "listAllBlogs";
                    try {
                        URL url = new URL(path);
                        Log.d("tag","发送请求");
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
            Log.d("tag","开始解析json文件" );

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
                dataObject.setBlogId(blog.getId());
                dataObject.setAuthorId(blog.getAuthor().getId());
                dataObject.setauthorSex(blog.getAuthor().getSex());
                dataObject.setAuthorAddress(blog.getAuthor().getAddress());
                dataObject.setAuthorBirthday(blog.getAuthor().getBirth().toString());

                dataList.add(dataObject);
            Log.d("tag", dataObject.toString() + 1);
                Log.d("tag","输出dataObject");
            }
            Log.d("tag","输出list集合");
        Log.d("tag",dataList.toString());
            handler.sendEmptyMessage(1);
        }



}





