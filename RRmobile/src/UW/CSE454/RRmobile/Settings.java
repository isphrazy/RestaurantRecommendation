package UW.CSE454.RRmobile;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings {
	
	private final String SETTINGS_FILE_NAME = "settings";
	private final String ACCESS_TOKEN = "accessToken";
	private final String USERNAME = "username";
	
	private static Settings instance;
	private static Context context;
	private SharedPreferences settings;
	private SharedPreferences.Editor settingsEditor;
			
	private Settings(){
		settings = context.getSharedPreferences(SETTINGS_FILE_NAME, 0);
		settingsEditor = settings.edit();
	}
	
	public static Settings getInstance(Context c){
		if(context == null) context = c;
		if(instance == null) instance = new Settings();
		return instance;
	}
	
	public void saveUserInfo(String ui){
		if(hasAt()) throw new IllegalStateException("access token has already exist");
		Log.e("saveUserInfo", ui);
		try {
			JSONObject user = new JSONObject(ui);
			settingsEditor.putString(USERNAME, user.getString("username"));
			settingsEditor.putString(ACCESS_TOKEN, user.getString("access_token"));
			settingsEditor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasAt(){
		return settings.getString(ACCESS_TOKEN, null) != null;
	}
	
	public String getUsername(){
		return settings.getString(USERNAME, null);
	}
	
	public void logout(){
		settingsEditor.remove(USERNAME);
		settingsEditor.remove(ACCESS_TOKEN);
		settingsEditor.commit();
	}
	
}
