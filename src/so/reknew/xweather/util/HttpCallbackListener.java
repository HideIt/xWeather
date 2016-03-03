package so.reknew.xweather.util;

public interface HttpCallbackListener {

	void onFinish(String response);
	
	void onError(Exception e);
}
