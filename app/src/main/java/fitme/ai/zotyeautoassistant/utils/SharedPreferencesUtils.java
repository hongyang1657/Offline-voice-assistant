package fitme.ai.zotyeautoassistant.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import fitme.ai.zotyeautoassistant.MyApplication;


/**
 * 缓存
 * @author Administrator
 * 
 */
public class SharedPreferencesUtils {

	private static SharedPreferencesUtils instance;
	private static Context context;

	private SharedPreferencesUtils(Context context) {
		this.context = context;
	}

	public synchronized static SharedPreferencesUtils getInstance() {
		if (instance == null)
			instance = new SharedPreferencesUtils(MyApplication.getInstance());
		return instance;
	}

	private SharedPreferences.Editor getEditor() {
		return getSharedPreferences().edit();
	}

	private static SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(SPConstants.SP_FILE_NAME, Activity.MODE_PRIVATE);
	}

	public String getStringValueByKey(String key) {
		return getSharedPreferences().getString(key, "");
	}

	public synchronized void setStringKeyValue(String key, String value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putString(key, value);
		editor.commit();
	}
	public long getLongValueByKey(String key, long defaultNum) {
		return getSharedPreferences().getLong(key,defaultNum);
	}

	public synchronized void setLongKeyValue(String key, long value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putLong(key, value);
		editor.commit();
	}

	public boolean getBooleanValueByKey(String key, boolean defaultValue) {
		return getSharedPreferences().getBoolean(key, defaultValue);
	}

	public synchronized void setBooleanKeyValue(String key, boolean flagValue) {
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(key, flagValue);
		editor.commit();
	}

	public int getIntValueByKey(String key) {
		return getSharedPreferences().getInt(key, 0);
	}

	public synchronized void setIntKeyValue(String key, int value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(key, value);
		editor.commit();
	}

	//清空数据
	public synchronized void clearSharedPreference(){
		SharedPreferences.Editor editor = getEditor();
		editor.clear();
		editor.commit();
	}
}
