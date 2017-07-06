package com.example.smartwallet.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.example.myapplication.R;

/**
 * 启动界面
 */
public class Appstart extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.appstart);
		
	new Handler().postDelayed(new Runnable(){
		public void run(){
			Intent intent = new Intent (Appstart.this,Login.class);
			startActivity(intent);			
			Appstart.this.finish();
		}
	}, 2000);
   }
}