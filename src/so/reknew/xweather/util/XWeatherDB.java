package so.reknew.xweather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import so.reknew.xweather.db.XWeatherOpenHelper;
import so.reknew.xweather.model.Province;

public class XWeatherDB {
	
	public static final String DB_NAME = "xweather";
	public static final int VERSION = 1;
	private static XWeatherDB xWeatherDB;
	private SQLiteDatabase db;
	
	private XWeatherDB(Context context) {
		XWeatherOpenHelper dbHelper = new XWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	public synchronized static XWeatherDB getInstance(Context context) {
		if(xWeatherDB == null) {
			xWeatherDB = new XWeatherDB(context);
		}
		return xWeatherDB;
	}
	
	public void saveProvince(Province province) {
		
	}
}
