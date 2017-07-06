package com.example.smartwallet.personalCenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

//import android.support.v4.app.Fragment;

/**
 * 主界面第三个部分
 * 个人中心
 */

public class TabFragment3 extends Fragment {
    private TextView userName;
    private ImageView editImage;
    private Button personalInfo;
    private Button myFriends;
    private Button changePassword;
    private Button changeCount;
    private Button exitApp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view3,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        SharedPreferences pref = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        String name = pref.getString("name","");
        userName = (TextView)getActivity().findViewById(R.id.user_name);
        if (!name.equals("")) {
            userName.setText(name);
        }
        editImage = (ImageView)getActivity().findViewById(R.id.edit_image);

        SharedPreferences pref1 = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String fileName = pref1.getString("head_image", "");
        if (!fileName.equals("")) {
            Bitmap bm = BitmapFactory.decodeFile(fileName);
            editImage.setImageBitmap(bm);
        }

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ChangeImage.class);
                startActivity(intent);
            }
        });

        personalInfo = (Button)getActivity().findViewById(R.id.person_info);
        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),PersonalInfo.class);
                startActivity(intent);
            }
        });

        myFriends = (Button)getActivity().findViewById(R.id.my_friends);
        myFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),Friends.class);
                startActivity(intent);
            }
        });

        changePassword = (Button) getActivity().findViewById(R.id.chang_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(getActivity(),ChangePassword.class);
                startActivity(intent);
            }
        });

        changeCount = (Button) getActivity().findViewById(R.id.chang_count);
        changeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        exitApp = (Button) getActivity().findViewById(R.id.exit_app);
        exitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

}
