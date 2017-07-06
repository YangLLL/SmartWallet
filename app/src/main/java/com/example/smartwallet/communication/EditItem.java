package com.example.smartwallet.communication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartwallet.main.Login;
import com.example.smartwallet.main.MainActivity;
import com.example.myapplication.R;
import com.example.smartwallet.tool.UploadUtil;

/**
 * Created by lenovo on 2016/8/18.
 * 发帖
 */
public class EditItem extends Activity {
    public static final int SHOW_RESPONSE = 0;
    private Button backButton;
    private EditText editTitle;
    private EditText editText;
    private ImageView imageView;
    private Button sendButton;
    String imagePath;
    String imageUrl = "222.196.193.166:8080/SmartWallet/addBlog";




    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
//                    Log.d("tag","response ;;;;;;;;;;;;;;;;;;;;");
                    if (response.equals("true")){
                        Toast.makeText(EditItem.this,"发送成功",Toast.LENGTH_SHORT).show();
                        Log.d("tag","................");
                        Intent intent = new Intent();
                        intent.setClass(EditItem.this, MainActivity.class);
                        intent.putExtra("id",R.id.tab2);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(EditItem.this,"发送失败",Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_view2_item);
        backButton = (Button)findViewById(R.id.edit_view2_back_button);
        editText = (EditText)findViewById(R.id.edit_Text);
        editTitle = (EditText) findViewById(R.id.edit_title);
        imageView = (ImageView) findViewById(R.id.edit_view2__image);
        sendButton = (Button)findViewById(R.id.send_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);


            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Login.IP + "addBlog";

                        SharedPreferences.Editor editor = getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
                        editor.putString("name","1");
                        editor.apply();
                        SharedPreferences pref = getSharedPreferences("userInfo",MODE_PRIVATE);
                        String name = pref.getString("name","");
                        String text = editText.getText().toString();
                        String title = editTitle.getText().toString();

                        try{
                            String request = UploadUtil.uploadFile(url,imagePath,title,"1",text);
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

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();

            Log.e("tag", "uri = " + uri);

            try {
                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        imagePath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        imageView.setImageBitmap(bitmap);

                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        imagePath = null;
                    }
                }).create();
        dialog.show();
    }
}
