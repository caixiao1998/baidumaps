package com.example.baidumaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class loginactivity extends Activity {
	
	//定义一个按钮
	private View registerbutton;  //不代表注册按钮
	private View forgetbutton;
	private View LoginButton;
	private EditText usernameTextView;
	private EditText passwordTextView;
	private SharedPreferences sp;
	private Editor editor;
	private CheckBox remUnameCheckBox;
	private CheckBox remPwdCheckBox;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   //初始化
		setContentView(R.layout.loginin);	
		//加载要显示的文件
		sp=getSharedPreferences("baidumap",Context.MODE_PRIVATE);
		editor =sp.edit();
		LoginButton=findViewById(R.id.loginbutton777);
		usernameTextView=(EditText) findViewById(R.id.loginusername);
		remUnameCheckBox=(CheckBox) findViewById(R.id.remunamecheckBox666);
		passwordTextView=(EditText) findViewById(R.id.loginuserpassword);
		remPwdCheckBox=(CheckBox) findViewById(R.id.remupasswdcheckBox777);
		registerbutton = findViewById(R.id.registerbutton3);
	
		if(sp.getBoolean("remUnameIsChecked", false)){
			usernameTextView.setText(sp.getString("remName", ""));
			remUnameCheckBox.setChecked(true);
			
		}
		if(sp.getBoolean("remPwdIsChecked", false)){
			passwordTextView.setText(sp.getString("remPwd", ""));
			remPwdCheckBox.setChecked(true);
			
		}
		
		registerbutton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				Intent intent=new Intent();
				intent.setClass(loginactivity.this, MainA.class);  //要跳转的activity.class 从loginactivity到Maina
				startActivity(intent);
				finish();
			}
		});
		
		forgetbutton=findViewById(R.id.forgetbutton999);
		forgetbutton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				Intent intent=new Intent();
				intent.setClass(loginactivity.this, forget.class);  //要跳转的activity.class 从loginactivity到Maina
				startActivity(intent);
				finish();
			}
		});
		
		
		LoginButton.setOnClickListener(new View.OnClickListener() {

			
			//输入合法性判定
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String username = usernameTextView.getText().toString();
				String password = passwordTextView.getText().toString();
				String userinfo=sp.getString(username, "");//没查询到返回空字符串
				if(username.length()<1){
					Toast.makeText(loginactivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if("".equals(userinfo)){
					Toast.makeText(loginactivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
					return;
				}
				//查到数据,从数据中找到密码
				String[] user =userinfo.split("_");  //按照下划线切割成数组
				if(password.length()<1){
					Toast.makeText(loginactivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.equals(user[1])){
					Toast.makeText(loginactivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				
				
				//登陆成功
				//页面跳转到地图页
			
				
				
				boolean uNameIsChecked = remUnameCheckBox.isChecked();
				boolean uPwdIsChecked = remPwdCheckBox.isChecked();
				if(uNameIsChecked){
					editor.putString("remName", username);
				}
				if(uPwdIsChecked){
					editor.putString("remPwd", password);
				}
				editor.putBoolean("remUnameIsChecked", uNameIsChecked);
				editor.putBoolean("remPwdIsChecked", uPwdIsChecked);
				editor.commit();
				
				
				Intent intent =new Intent();
				intent.setClass(loginactivity.this, BaiduMapActivity.class);
				startActivity(intent);
				finish();
				
			
				
				
				
				
				
				
				
			}
		});
		
		//给记住密码设置监听器
		remPwdCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton cb, boolean ischecked) {
				// TODO Auto-generated method stub
				if(ischecked==true){
					remUnameCheckBox.setChecked(true);
				}
			}
		});
		
		remUnameCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
				// TODO Auto-generated method stub
				if(ischecked==false){
					remPwdCheckBox.setChecked(false);
			}
			}
		});
		
	}
}