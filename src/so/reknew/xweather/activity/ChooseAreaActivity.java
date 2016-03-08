package so.reknew.xweather.activity;

import so.reknew.xweather.util.*;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import so.reknew.xweather.R;
import so.reknew.xweather.model.City;
import so.reknew.xweather.model.County;
import so.reknew.xweather.model.Province;
import so.reknew.xweather.util.HttpCallbackListener;
import so.reknew.xweather.util.HttpUtil;
import so.reknew.xweather.util.Utility;
import so.reknew.xweather.util.XWeatherDB;

public class ChooseAreaActivity extends Activity {
	
	private int currentLevel;
	
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private XWeatherDB xWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		P.d("onCreate(Bundle savedInstanceState)");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView =(ListView)findViewById(R.id.list_view);
		titleText = (TextView)findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		xWeatherDB = XWeatherDB.getInstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View view, int index, long arg3) {
				P.d("onItemClick()");
				if(currentLevel == LEVEL_PROVINCE) {
					P.d("currentLevel == LEVEL_PROVINCE");
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if(currentLevel == LEVEL_CITY) {
					P.d("currentLevel == LEVEL_CITY");
					selectedCity = cityList.get(index);
					queryCounties();
				}
			}
		});
		queryProvinces();
	}

	private void queryProvinces() {
		P.d("queryProvinces()");
		provinceList = xWeatherDB.loadProvince();
		if(provinceList.size() > 0) {
			P.d("provinceList.size() > 0");
			dataList.clear();//remove all elements of this List
			for(Province province : provinceList) {
				dataList.add(province.getName());//add provinces name into dataList
			}
			P.d("dataList.size():"+dataList.size());
			adapter.notifyDataSetChanged();//notify View to refresh
			listView.setSelection(0);
			titleText.setText("China");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	private void queryCities() {
		P.d("queryCities()");
		cityList = xWeatherDB.loadCity(selectedProvince.getId());
		if(cityList.size() > 0) {
			P.d("cityList.size() > 0");
			dataList.clear();
			for(City city : cityList) {
				dataList.add(city.getName());
			}
			P.d("dataList.size():"+dataList.size());
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getCode(), "city");
		}
	}

	private void queryCounties() {
		P.d("queryCounties()");
		countyList = xWeatherDB.loadCounty(selectedCity.getId());
		if(countyList.size() > 0) {
			P.d("countyList.size() > 0");
			dataList.clear();
			for(County county : countyList) {
				dataList.add(county.getName());
			}
			P.d("dataList.size():"+dataList.size());
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCode(), "county");
		}
	}

	private void queryFromServer(final String code, final String type) {
		P.d("queryFromServer()");
		String address;
		if(!TextUtils.isEmpty(code)) {
			P.d("!TextUtils.isEmpty(code)");
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		P.d("address:"+address);
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				P.d("onFinish(String response)");
				boolean result = false;
				if("province".equals(type)) {
					P.d("\"province\".equals(type)");
					result = Utility.handleProvincesResponse(xWeatherDB, response);
				} else if("city".equals(type)) {
					P.d("\"city\".equals(type)");
					result = Utility.handleCitiesResponse(xWeatherDB, response,
							selectedProvince.getId());
				} else if("county".equals(type)) {
					P.d("\"county\".equals(type)");
					result = Utility.handleCountiesResponse(xWeatherDB, response,
							selectedCity.getId());
				}
				if(result) {
					P.d("result == true");
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							P.d("run()");
							closeProgressDialog();
							if("province".equals(type)) {
								P.d("\"province\".equals(type)");
								queryProvinces();
							} else if("city".equals(type)) {
								P.d("\"city\".equals(type)");
								queryCities();
							} else if("county".equals(type)) {
								P.d("\"county\".equals(type)");
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				P.d("onError(Exception e)");
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						P.d("run()");
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "Load ERROR...",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	private void showProgressDialog() {
		P.d("showProgressDialog()");
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Loading...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		P.d("closeProgressDialog()");
		if(progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		P.d("onBackPressed()");
		if(currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if(currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			super.onBackPressed();
		}
	}
}
