package com.example.smartwallet.personalCenter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartwallet.main.MainActivity;
import com.example.myapplication.R;
import com.example.smartwallet.main.Login;
import com.example.smartwallet.tool.UploadUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by lenovo on 2016/8/22.
 * 修改个人头像
 */
public class ChangeImage extends Activity {
    public static final int SHOW_RESPONSE = 0;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private ImageView picture;
    private Uri imageUri;
    String picpath;
    String url = Login.IP + "uploadAvatar";

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (response.equals("true")){
                        Toast.makeText(ChangeImage.this,"发送成功",Toast.LENGTH_SHORT).show();
                        Log.d("tag","................");
                        Intent intent = new Intent();
                        intent.putExtra("id",R.id.tab2);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ChangeImage.this,"发送失败",Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault);
        setContentView(R.layout.imageshow);

        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        picture = (ImageView) findViewById(R.id.picture);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String fileName = pref.getString("head_image", "");
        Log.e("tag", fileName + "..........");

        Bitmap bm = BitmapFactory.decodeFile(fileName);

        picture.setImageBitmap(bm);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, 101, 1, "从手机中选择图片");
        menu.add(1, 102, 1, "拍照");

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");

        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intentBack = new Intent();
                intentBack.setClass(ChangeImage.this, MainActivity.class);
                startActivity(intentBack);
                break;

            case 101:

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                Log.e("tag", "相册：uri=" + imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                break;

            case 102:

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                startActivityForResult(intent2, TAKE_PHOTO);
                break;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        imageUri = data.getData();
                    }

                    Log.e("tag", "剪裁：uri=" + imageUri);
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                    String[] imgs = {MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor = this.managedQuery(imageUri, imgs, null, null, null);

                    if (cursor != null) {
                        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        picpath = cursor.getString(index);
                        Log.e("tag", "img_url:" + picpath);
                    }else {
                        picpath = imageUri.getPath();
                        Log.e("tag", "img_url:" + picpath);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Log.d("tag","地址：" + picpath);

                                String request = UploadUtil.uploadFile(url,picpath, Login.USER.getId().toString());
                                Log.d("tag",request+ "str=======");

                                Message message = new Message();
                                message.what = SHOW_RESPONSE;
                                message.obj = request ;
                                handler.sendMessage(message);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    SharedPreferences.Editor editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putString("head_image", picpath);
                    editor.apply();

                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        Toast.makeText(ChangeImage.this, "shoudao", Toast.LENGTH_SHORT).show();
                        Log.e("tag", "uri = " + imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));

                        Log.e("tag", "save：uri = " + imageUri);


                        picture.setImageBitmap(bitmap);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

}
