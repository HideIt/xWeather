package so.reknew.xweather.util;

import android.text.TextUtils;
import so.reknew.xweather.model.City;
import so.reknew.xweather.model.County;
import so.reknew.xweather.model.Province;

public class Utility {
	
	public synchronized static boolean handleProvincesResponse
	(XWeatherDB xWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)) {
			android.util.Log.d("MINE", "handleProvinceResponse() response not empty");
			android.util.Log.d("MINE", "response data:"+response);
			String[] allProvinces = response.split(",");
			android.util.Log.d("MINE", "all provinces data:"+allProvinces);
			if(allProvinces != null && allProvinces.length > 0) {
				android.util.Log.d("MINE", "allProvince not null && > 0");
				for(String p:allProvinces) {
					android.util.Log.d("MINE", "String p:allProvinces -> p:"+p);
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setName(array[0]);
					android.util.Log.d("MINE", ""+province.getName());
					province.setCode(array[1]);
					android.util.Log.d("MINE", ""+province.getCode());
					xWeatherDB.saveProvince(province);
				}
				android.util.Log.d("MINE", "will return true");
				return true;
			}
		}
		android.util.Log.d("MINE", "will return false");
		return false;
	}
	
	public static boolean handleCitiesResponse
	(XWeatherDB xWeatherDB, String response, int provinceId) {
		if(!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0) {
				for(String c:allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setName(array[0]);
					city.setCode(array[1]);
					city.setProvinceId(provinceId);
					xWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleCountiesResponse
	(XWeatherDB xWeatherDB, String response,int cityId) {
		if(!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0) {
				for(String c:allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setName(array[0]);
					county.setCode(array[1]);
					county.setCityId(cityId);
					xWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}