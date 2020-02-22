package com.example.baidumaps;//这里要修改成自己的包名

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class BaiduMapActivity extends Activity implements OnMenuItemClickListener {

	private Context context;
	private SharedPreferences sp;
	private MapView mapView;
	private BaiduMap baiduMap;
	private RadioGroup maptypeRadioGroup;
	private CheckBox trafficCheckBox;
	private Handler handler;
	
	  Editor editor;
	
	//待删
	private static double EARTH_RADIUS = 6378.137;

	 

	private static double rad(double d) {

		return d*Math.PI/180.0;

	}
	public static double getDistance(double lat1, double lng1, double lat2,	double lng2) {	
		double radLat1 = rad(lat1);		
		double radLat2 = rad(lat2);		
		double a = radLat1 - radLat2;		
		double b = rad(lng1)-rad(lng2);		
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
				
		s = s * EARTH_RADIUS;		
		s = Math.round(s*10000d)/10000d;		
		s = s * 1000;		
		return s;	
		}
	
	
	
	
	//删除
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.map);
		init();
		setListener();
	}

	private void init() {
		context = BaiduMapActivity.this;
		sp = context.getSharedPreferences("baidumap", Context.MODE_PRIVATE);
		mapView = (MapView) findViewById(R.id.mapView);
		// 取消放大缩小键
		mapView.showZoomControls(false);
		baiduMap = mapView.getMap();
		toNewAddress(baiduMap, 118.78, 32.07);
		maptypeRadioGroup = (RadioGroup) findViewById(R.id.maptypeRadioGroup);
		trafficCheckBox = (CheckBox) findViewById(R.id.trafficCheckBox);
		handler = new Handler() {
			@Override
			public void handleMessage(final Message msg) {
				switch (msg.what) {
				case 1:
					showToast(context, msg.obj.toString());
					break;
				}
			}
		};
	}

	private void setListener() {
		maptypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.normalRadioButton) {
					baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					Toast.makeText(context, "普通地图", Toast.LENGTH_LONG).show();
				}
				if (checkedId == R.id.satelliteRadioButton) {
					baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					Toast.makeText(context, "卫星地图", Toast.LENGTH_LONG).show();
				}
			}
		});

		trafficCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
				if (flag) {
					// 开启交通图
					baiduMap.setTrafficEnabled(true);
				} else {
					// 关闭交通图
					baiduMap.setTrafficEnabled(false);
				}
			}
		});
	}

	// 添加菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// groupId,itemId,orderId,名称
		menu.add(1, 1, 1, "经纬度定位").setOnMenuItemClickListener(this);
		menu.add(2, 2, 2, "城市定位").setOnMenuItemClickListener(this);
		menu.add(3, 3, 3, "公里数计算").setOnMenuItemClickListener(this);
		menu.add(4, 4, 4, "当前用户信息").setOnMenuItemClickListener(this);
		menu.add(5, 5, 5, "清除屏幕").setOnMenuItemClickListener(this);
		menu.add(6, 6, 6, "退出").setOnMenuItemClickListener(this);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		int itemId = item.getItemId();
		AlertDialog.Builder builder = new AlertDialog.Builder(BaiduMapActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		switch (itemId) {
		case 1:
			final View locationView = View.inflate(BaiduMapActivity.this, R.layout.dialog_location, null);
			builder.setTitle("经纬度定位");
			builder.setView(locationView);
			final EditText longitudeEditText = (EditText) locationView.findViewById(R.id.longitudeEditText);
			final EditText latitudeEditText = (EditText) locationView.findViewById(R.id.latitudeEditText);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String longitudeString = longitudeEditText.getText().toString().trim();
					if (longitudeString.trim().length() == 0) {
						showToast(context, "经度不能为空!");
						return;
					}
					String latitudeString = latitudeEditText.getText().toString().trim();
					if (latitudeString.trim().length() == 0) {
						showToast(context, "纬度不能为空!");
						return;
					}
					double longitude = Double.parseDouble(longitudeString);
					if (longitude < -180 || longitude > 180) {
						showToast(context, "经度的范围在-180~180之间!");
						return;
					}
					double latitude = Double.parseDouble(latitudeString);
					if (latitude < -90 || latitude > 90) {
						showToast(context, "纬度的范围在-90~90之间!");
						return;
					}
					// 定义Maker坐标点
					pointOverlay(baiduMap, longitude, latitude);
					// 将地图移动过去
					toNewAddress(baiduMap, longitude, latitude);
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			break;
		case 2:// 城市定位功能
			final View cityView = View.inflate(BaiduMapActivity.this, R.layout.city_location, null);
			builder.setTitle("城市定位");
			builder.setView(cityView);
			final EditText cityEditText = (EditText) cityView.findViewById(R.id.CityeditText);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String city = cityEditText.getText().toString();// 获得城市信息
					// 需要知道当前城市的坐标
					if (city.equals("南京")) {
						// 南京经纬度:(118.78333,32.05000)
						// 利用经纬度城市定位
						pointOverlay(baiduMap, 118.78333, 32.05000);
						// 将地图移动过去
						toNewAddress(baiduMap, 118.78333, 32.05000);
					}
					else if(city.equals("北京")){
						pointOverlay(baiduMap, 116.41667, 39.91667);
						// 将地图移动过去
						toNewAddress(baiduMap, 116.41667, 39.91667);
					}
					else if(city.equals("渭南")){
						pointOverlay(baiduMap, 109.50, 34.50);
						// 将地图移动过去
						toNewAddress(baiduMap, 109.50, 34.50);
					}
			//////////////////////////////////////////////////////////////////
					else if(city.equals("上海")){
						pointOverlay(baiduMap, 121.43333, 34.50000);
						// 将地图移动过去
						toNewAddress(baiduMap, 121.43333, 34.50000);
					}
					else if(city.equals("天津")){
						pointOverlay(baiduMap, 117.20000, 39.13333);
						// 将地图移动过去
						toNewAddress(baiduMap, 117.20000, 39.13333);
					}
					else if(city.equals("香港")){
						pointOverlay(baiduMap, 114.10000, 22.20000);
						// 将地图移动过去
						toNewAddress(baiduMap, 114.10000, 22.20000);
					}
					else if(city.equals("广州")){
						pointOverlay(baiduMap, 113.23333, 34.50);
						// 将地图移动过去
						toNewAddress(baiduMap, 113.23333, 34.50);
					}
					else if(city.equals("深圳")){
						pointOverlay(baiduMap, 114.06667, 22.61667);
						// 将地图移动过去
						toNewAddress(baiduMap, 114.06667, 22.61667);
					}
					else if(city.equals("杭州")){
						pointOverlay(baiduMap, 120.20000, 30.26667);
						// 将地图移动过去
						toNewAddress(baiduMap, 120.20000, 30.26667);
					}
					else if(city.equals("重庆")){
						pointOverlay(baiduMap, 106.45000,29.56667);
						// 将地图移动过去
						toNewAddress(baiduMap, 106.45000, 29.56667);
					}
					else if(city.equals("西安")){
						pointOverlay(baiduMap, 108.95000, 34.26667);
						// 将地图移动过去
						toNewAddress(baiduMap, 108.95000, 34.26667);
					}
					else if(city.equals("武汉")){
						pointOverlay(baiduMap, 114.31667, 30.51667);
						// 将地图移动过去
						toNewAddress(baiduMap, 114.31667, 30.51667);
					}
					else if(city.equals("郑州")){
						pointOverlay(baiduMap, 113.65000, 34.76667);
						// 将地图移动过去
						toNewAddress(baiduMap, 113.65000, 34.76667);
					}
					else if(city.equals("哈尔滨")){
						pointOverlay(baiduMap, 126.63333, 45.75000);
						// 将地图移动过去
						toNewAddress(baiduMap, 126.63333, 45.75000);
					}
					else if(city.equals("石家庄")){
						pointOverlay(baiduMap, 114.48333, 38.03333);
						// 将地图移动过去
						toNewAddress(baiduMap, 114.48333, 38.03333);
					}
					else if(city.equals("蒲城")){
						pointOverlay(baiduMap, 109.58, 34.95);
						// 将地图移动过去
						toNewAddress(baiduMap, 109.58, 34.95);
					}
					else if(city.equals("银川")){
						pointOverlay(baiduMap, 106.26667, 38.46667);
						// 将地图移动过去
						toNewAddress(baiduMap, 106.26667, 38.46667);
					}
					else{
						Toast.makeText(BaiduMapActivity.this, "抱歉，当前所查询城市尚未收录！", Toast.LENGTH_LONG).show();
					}

				}
			});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();

			break;
		case 3:
			final View kiloView = View.inflate(BaiduMapActivity.this, R.layout.kilo, null);
			builder.setTitle("公里数计算");
			builder.setView(kiloView);
			final EditText weizhiyijingduEditText = (EditText) kiloView.findViewById(R.id.weizhiyijingdueditText1);
			final EditText weizhiyiweiduEditText = (EditText) kiloView.findViewById(R.id.weizhiyiweidueditText2);
			final EditText weizhierjingduEditText = (EditText) kiloView.findViewById(R.id.weizhierjingdueditText3);
			final EditText weizhierweiduEditText = (EditText) kiloView.findViewById(R.id.weizhierweidueditText4);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				String jingdu_one=weizhiyijingduEditText.getText().toString();
				String weidu_one=weizhiyiweiduEditText.getText().toString();
				String jingdu_two=weizhierjingduEditText.getText().toString();
				String weidu_two=weizhierweiduEditText.getText().toString();
				double s1=Double.parseDouble(jingdu_one);
				double s2=Double.parseDouble(weidu_one);
				double s3=Double.parseDouble(jingdu_two);
				double s4=Double.parseDouble(weidu_two);
					double distance = getDistance(s2, s1,s4, s3);		
					System.out.println("距离" + distance / 1000 + "公里");
					double dd=(distance/1000);
					 
					  BigDecimal b = new BigDecimal(dd);
					  dd = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();        
					
					Toast.makeText(BaiduMapActivity.this, "距离" + dd + "公里", Toast.LENGTH_LONG).show();
					
					
					
					
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			break;
		case 4:
			final EditText username1;
			final EditText userpass;
			final View whoView = View.inflate(BaiduMapActivity.this, R.layout.info, null);
			builder.setTitle("个人信息");
			builder.setView(whoView);
			username1=(EditText) whoView.findViewById(R.id.lkk);
			userpass=(EditText) whoView.findViewById(R.id.lpp);
			 
			 sp=getSharedPreferences("baidumap",Context.MODE_PRIVATE);
				editor =sp.edit();
			String info=sp.getString("remName", "");
			
			username1.setText(info);
			userpass.setText(sp.getString("remPwd", ""));
			 builder.setNegativeButton("取消", null);
				builder.create();
				builder.show();
			break;
		case 5:
				baiduMap.clear();
			break;
		case 6:
			exist();
			break;
		default:
			break;
		}
		return true;
	}

	private void showToast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 将地图的中兴点移动到指定点
	 * 
	 * @param baiduMap
	 *            百度地图对象
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 */
	private void toNewAddress(BaiduMap baiduMap, double longitude, double latitude) {
		// 设定中心点坐标
		LatLng cenpt = new LatLng(latitude, longitude);
		// 定义地图状态
		MapStatus mapStatus = new MapStatus.Builder().target(cenpt).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
		// 改变地图状态
		baiduMap.setMapStatus(mapStatusUpdate);
	}

	/**
	 * 绘制点标记，并将新的点标记添加到地图中
	 * 
	 * @param baiduMap
	 *            百度地图对象
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 */
	private void pointOverlay(BaiduMap baiduMap, double longitude, double latitude) {
		// 定义Maker坐标点
		LatLng point = new LatLng(latitude, longitude);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.mark);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		// 在地图上添加Marker，并显示
		baiduMap.addOverlay(option);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exist();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	// 退出
	private void exist() {
		AlertDialog.Builder builder = new AlertDialog.Builder(BaiduMapActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("真的要退出吗？");
		builder.setMessage("可不可以留下来陪陪我？");
		builder.setPositiveButton("依然要走", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				BaiduMapActivity.this.finish();
			}
		});
		builder.setNegativeButton("留下来", null);
		builder.create();
		builder.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

}
