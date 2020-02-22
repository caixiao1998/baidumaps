package com.example.baidumaps;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainA extends Activity {
		
	private Button registerButton;
	private EditText usernametext;
	private EditText passwordEditText;
	private EditText passwordEditText2;
	private EditText birthsdayEditText;
	private RadioButton maleEditText;
	private EditText emailText;
	private EditText hobbyEditText;
	private EditText phonenumberText;
	private EditText introduceText;
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private EditText lineEditText;
	private SharedPreferences sp;
	private Editor editor;
	
	
	@SuppressWarnings("unused")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		sp=getSharedPreferences("baidumap",Context.MODE_PRIVATE);
		editor =sp.edit();
		
		
		registerButton = (Button) findViewById(R.id.registerbutton999);
		usernametext=(EditText) findViewById(R.id.username);
		passwordEditText=(EditText) findViewById(R.id.userpassword);
		passwordEditText2=(EditText) findViewById(R.id.userpassword2);
		birthsdayEditText=(EditText) findViewById(R.id.birthday);
		maleEditText=(RadioButton) findViewById(R.id.maleradio);
		emailText=(EditText) findViewById(R.id.email);
		hobbyEditText=(EditText) findViewById(R.id.hobby888);
		phonenumberText=(EditText) findViewById(R.id.phonenumber);
		introduceText=(EditText) findViewById(R.id.introduce);
		provinceSpinner = (Spinner)findViewById(R.id.provinceSpinner);
		citySpinner = (Spinner)findViewById(R.id.citySpinner);
		lineEditText = (EditText)findViewById(R.id.lineEditText);
		
		

		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//
		
		
		
		
		provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
			 */
			public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
				int provinceIndex = position;
				String[] province =  ProvinceCityUtil.PROVINCE_ARRAY;
				int cityIndex = 0;
				if(position==0||position==1||position==2||position==3||position==31||position==32){
					lineEditText.setVisibility(View.INVISIBLE);
					citySpinner.setVisibility(View.INVISIBLE);
				}else{
					String[] city = ProvinceCityUtil.CITY_ARRAY[provinceIndex];
					ArrayAdapter<String> cityArrayAdapter = new ArrayAdapter<String>(MainA.this,android.R.layout.simple_spinner_dropdown_item,city);
					//
					cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					citySpinner.setAdapter(cityArrayAdapter);
					lineEditText.setVisibility(View.VISIBLE);
					citySpinner.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			};
		}
		);
		
//爱好下拉列表	
		hobbyEditText.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0){
				//创建一个弹出框，弹出可供选择的爱好选项
				//定义一个弹出框
				AlertDialog.Builder build = new AlertDialog.Builder(MainA.this);
				final String[] items = {"唱","跳","RAP","篮球"};
				final boolean[] checkedItems = {false,false,false,false};
				build.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener(){
					
					@Override//监听多选按钮的点击事件，第二个参数，表示第几个被操作，第三个参数表示当前的选中状态
					public void onClick(DialogInterface di, int which, boolean cheecked){
						checkedItems[which] = cheecked;//修改某个爱好的选择状态
					}
				});
					build.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface di, int which) {
							StringBuffer sb = new StringBuffer();
							for(int i = 0;i< checkedItems.length;i++){
								if(checkedItems[i]){
									sb.append(items[i] + "  ");
								}
							}
							hobbyEditText.setText(sb.toString());
						}
					});//设置确定按钮
					build.setNegativeButton("取消", null);//设置取消按钮
					build.show();
				
			}
		});	
		
//生日下拉列表
		birthsdayEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DatePickerDialog dd=new DatePickerDialog(MainA.this,new OnDateSetListener(){
					public void onDateSet(DatePicker dp,int year,int month,int day){
						
						Calendar calendar = Calendar.getInstance();
						int s1=Integer.valueOf(calendar.get(Calendar.YEAR));
						int s2=Integer.valueOf(calendar.get(Calendar.MONTH));
						int s3=Integer.valueOf(calendar.get(Calendar.DATE));
if(year>s1){
	Toast.makeText(MainA.this, "超出范围", Toast.LENGTH_SHORT).show();
	return;
}
else if(year==s1&&month>s2){
	Toast.makeText(MainA.this, "超出范围", Toast.LENGTH_SHORT).show();
	return;
}
else if(year==s1&&month==s2&&day>s3){
	Toast.makeText(MainA.this, "超出范围", Toast.LENGTH_SHORT).show();
	return;
}
else
						birthsdayEditText.setText(year+"_"+(month+1)+"_"+day);
					
				}
				},2000,1-1,1);
					dd.show();
			}
		});
		


		registerButton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				//
				String username=usernametext.getText().toString();
				String password=passwordEditText.getText().toString();
				String password2=passwordEditText2.getText().toString();
				String birthday=birthsdayEditText.getText().toString();
				String sex =maleEditText.isChecked()?"男":"女";
				String emails=emailText.getText().toString();
				String phone=phonenumberText.getText().toString();
				String intro=introduceText.getText().toString();
				String[] birth =emails.split("_");
				//用户名是否存在
				
				if(username.length()<=0){
					Toast.makeText(MainA.this, "用户名不为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				String u=sp.getString(username, "");
				if(!"".equals(u)){
					Toast.makeText(MainA.this, "用户名已存在，请重新输入！", Toast.LENGTH_SHORT).show();
					return;
				}
				String regex2="1[3|4|5|7|8][0-9]\\d{4,8}$";
				if(!phone.matches(regex2)){
					Toast.makeText(MainA.this, "不正确的手机格式", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
				 if(!emails.matches(regex))
		            {
		               Toast.makeText(MainA.this, "不正确的邮箱格式", Toast.LENGTH_SHORT).show();
		               return;
		            }
				
				if(!password.equals(password2)){
					Toast.makeText(MainA.this, "两次密码输入不同，请重新输入！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(password.length()<=6){
					Toast.makeText(MainA.this, "密码应为7位以上！", Toast.LENGTH_SHORT).show();
					return;
				}
				String info=username+"_"+password+"_"+password2+"_"+birthday+"_"+sex+"_"+emails+"_"+phone+"_"+intro;
				Toast.makeText(MainA.this, info, Toast.LENGTH_SHORT).show();
				editor.putString(username, info);
				editor.commit();
				
				Intent intent=new Intent();
				intent.setClass(MainA.this, loginactivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		
		
	}


	private SharedPreferences getSharedPreferences(String string, Button button) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
	
	


			
