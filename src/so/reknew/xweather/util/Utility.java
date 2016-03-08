package so.reknew.xweather.util;

import android.text.TextUtils;
import so.reknew.xweather.model.City;
import so.reknew.xweather.model.County;
import so.reknew.xweather.model.Province;

public class Utility {
	
	public synchronized static boolean handleProvincesResponse
	(XWeatherDB xWeatherDB, String response) {
		P.d("handleProvincesResponse()");
		if(!TextUtils.isEmpty(response)) {
			P.d("!TextUtils.isEmpty(response)");
			P.d("----------response data:"+response);
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0) {
				P.d("allProvinces != null && allProvinces.length > 0");
				for(String p:allProvinces) {
					P.d("-----String p:"+p);
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setCode(array[0]);
					province.setName(array[1]);
					xWeatherDB.saveProvince(province);
				}
				P.d("----------save province data complete. will return true");
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleCitiesResponse
	(XWeatherDB xWeatherDB, String response, int provinceId) {
		P.d("handleCitiesResponse()");
		if(!TextUtils.isEmpty(response)) {
			P.d("!TextUtils.isEmpty(response)");
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0) {
				P.d("allCities != null && allCities.length > 0");
				for(String c:allCities) {
					P.d("-----String c:"+c);
					String[] array = c.split("\\|");
					City city = new City();
					city.setCode(array[0]);
					city.setName(array[1]);
					city.setProvinceId(provinceId);
					xWeatherDB.saveCity(city);
				}
				P.d("----------save city data complete. will return true");
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
					P.d("-----String c:"+c);
					String[] array = c.split("\\|");
					County county = new County();
					county.setCode(array[0]);
					county.setName(array[1]);
					county.setCityId(cityId);
					xWeatherDB.saveCounty(county);
				}
				P.d("----------save county data complete. will return true");
				return true;
			}
		}
		return false;
	}
}