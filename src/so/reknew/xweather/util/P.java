package so.reknew.xweather.util;
import android.util.Log;
public class P {
	
	public static final boolean ISPRINT = true;
	public static final String TAG = "MINE";
	
	public static void d(Object obj) {
		if(ISPRINT) {
			Log.d(TAG, String.valueOf(obj));
		}
	}
/*	
	public static void v(String msg) {
		if(ISPRINT) {
			Log.v(TAG, msg);
		}
	}
	
	public static void i(String msg) {
		if(ISPRINT) {
			Log.i(TAG, msg);
		}
	}
	
	public static void w(String msg) {
		if(ISPRINT) {
			Log.w(TAG, msg);
		}
	}
	
	public static void e(String msg) {
		if(ISPRINT) {
			Log.e(TAG, msg);
		}
	}*/
}
