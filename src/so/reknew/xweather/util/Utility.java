package so.reknew.xweather.util;

import android.text.TextUtils;
import so.reknew.xweather.model.City;
import so.reknew.xweather.model.County;
import so.reknew.xweather.model.Province;

public class Utility {
	
	public synchronized static boolean handleProvincesResponse
	(XWeatherDB xWeatherDB, String response) {
		if(!TextUtils.isEmpty(response)) {
			String[] allPronvinces = response.split(",");
			if(allPronvinces != null && allPronvinces.length > 0) {
				for(String p:allPronvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setName(array[0]);
					province.setCode(array[1]);
					xWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
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