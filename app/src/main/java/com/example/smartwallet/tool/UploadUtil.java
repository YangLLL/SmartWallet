package com.example.smartwallet.tool;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by lenovo on 2016/12/13.
 * 上传图片
 */
public class UploadUtil {

    private static String message;

    public static String uploadFile(String url, String name,String title, String id, String content) {
        String requestUrl = url;

        String fileName = name;
        String userId = id;
        String fileContent = content;

        String boundary = UUID.randomUUID().toString();
        String prefix = "--";
        String end = "\r\n";

        try {
            URL httpUrl = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data; name=\"title\"" + end);
            out.writeBytes(end);
            out.writeBytes(title + end);
            Log.e("tag","发送标题");

            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data; name=\"authorId\"" + end);
            out.writeBytes(end);
            out.writeBytes(userId + end);
            Log.e("tag","作者id");

            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data; name=\"content\"" + end);
            out.writeBytes(end);
            out.writeBytes(fileContent + end);
            Log.e("tag","评论内容");

            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data;" + "name=\"picture\";filename=\"" + fileName + "\"" + end);
            out.writeBytes(end);
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            byte[] b = new byte[1024 * 4];
            int len;
            while ((len = fileInputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }

            out.writeBytes(end);
            out.writeBytes(prefix + boundary + prefix + end);

            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            message = sb.toString();
            Log.e("tag", "response:" + message);


            if (out != null) {
                out.close();
            }
            if (reader != null) {
                reader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }



    public static String uploadFile(String url, String name,String id) {
        String requestUrl = url;
        String userId = id;
        String fileName = name;


        String boundary = UUID.randomUUID().toString();
        String prefix = "--";
        String end = "\r\n";

        try {
            URL httpUrl = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data; name=\"authorId\"" + end);
            out.writeBytes(end);
            out.writeBytes(userId + end);
            Log.e("tag","作者id");

            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition: form-data;" + "name=\"picture\";filename=\"" + fileName + "\"" + end);
            out.writeBytes(end);
            //往服务器写数据
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            byte[] b = new byte[1024 * 4];
            int len;
            while ((len = fileInputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }

            out.writeBytes(end);
            out.writeBytes(prefix + boundary + prefix + end);

            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            message = sb.toString();
            Log.e("tag", "response:" + message);
            if (out != null) {
                out.close();
            }
            if (reader != null) {
                reader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
