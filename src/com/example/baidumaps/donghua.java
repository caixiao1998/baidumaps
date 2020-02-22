package com.example.baidumaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class donghua extends Activity {
	Handler handler=new Handler(){
	public void handleMessage(Message msg){
		Intent intent =new Intent();
		intent.setClass(donghua.this, loginactivity.class);
		startActivity(intent);
		finish();
		
	
	}
	};

protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.logo);
	handler.sendEmptyMessageDelayed(1, 3500);
}
}
