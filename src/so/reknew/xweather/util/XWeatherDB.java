package so.reknew.xweather.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import so.reknew.xweather.db.XWeatherOpenHelper;
import so.reknew.xweather.model.City;
import so.reknew.xweather.model.County;
import so.reknew.xweather.model.Province;

public class XWeatherDB {
	
	public static final String DB_NAME = "xweather.db";
	public static final int VERSION = 1;
	private static XWeatherDB xWeatherDB;
	private SQLiteDatabase db;

	//��ֻ֤��һ��XWeatherDB��ʵ��
	private XWeatherDB(Context context) {
		P.d("XWeatherDB(Context context)");
		XWeatherOpenHelper dbHelper = new XWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public synchronized static XWeatherDB getInstance(Context context) {
		P.d("getInstance(Context context)");
		if(xWeatherDB == null) {
			P.d("xWeatherDB == null");
			xWeatherDB = new XWeatherDB(context);
		}
		return xWeatherDB;
	}
	
	//�洢province
	public void saveProvince(Province province) {
		P.d("saveProvince(Province province)");
		if(province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getName());
			values.put("province_code", province.getCode());
			db.insert("Province", null, values);
		}
	}
	
	//��ȡprovince
	public List<Province> loadProvince() {
		P.d("loadProvince()");
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()) {
			P.d("cursor.moveToFirst()");
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while(cursor.moveToNext());
		}
		P.d("list.zise()="+list.size());
		return list;
	}
	
	//�洢city
	public void saveCity(City city) {
		P.d("saveCity(City city)");
		if(city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getName());
			values.put("city_code", city.getCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	//��ȡcity
	public List<City> loadCity(int provinceId) {
		P.d("loadCity(int provinceId)");
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[]{String.valueOf(provinceId)}, null, null, null, null);
		if(cursor.moveToFirst()) {
			P.d("cursor.moveToFirst()");;
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while(cursor.moveToNext());
		}
		P.d("list.zise()="+list.size());
		return list;
	}
	
	//�洢county
	public void saveCounty(County county) {
		P.d("saveCounty(County county)");
		if(county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getName());
			values.put("county_code", county.getCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	//��ȡcounty
	public List<County> loadCounty(int cityId) {
		P.d("loadCounty(int cityId)");
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[]{String.valueOf(cityId)}, null, null, null, null);
		if(cursor.moveToFirst()) {
			P.d("cursor.moveToFirst()");
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while(cursor.moveToNext());
		}
		P.d("list.zise()="+list.size());
		return list;
	}
}
