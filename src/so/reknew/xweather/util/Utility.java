package so.reknew.xweather.util;

import android.text.TextUtils;
import so.reknew.xweather.model.City;
import so.reknew.xweather.model.County;
import so.reknew.xweather.model.Province;

public class Utility {
	
	public synchronized static boolean handleProvincesResponse
	(XWeatherDB xWeatherDB, String response) {
		P.d("handleProvincesResponse(XWeatherDB xWeatherDB, String response)");
		if(!TextUtils.isEmpty(response)) {
			P.d("!TextUtils.isEmpty(response)");
			P.d("-----response data:"+response);
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0) {
				P.d("allProvinces != null && allProvinces.length > 0");
				for(String p:allProvinces) {
					P.d("-----String p:allProvinces");
					P.d("p:"+p);
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setName(array[0]);
					P.d("province.getName():"+province.getName());
					province.setCode(array[1]);
					P.d("province.getCode():"+province.getCode());
					xWeatherDB.saveProvince(province);
				}
				P.d("-----save province data complete. will return true");
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