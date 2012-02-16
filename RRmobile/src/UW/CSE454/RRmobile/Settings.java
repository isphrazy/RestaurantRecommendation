package UW.CSE454.RRmobile;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * manage the user's restaurants, use singleton
 * @author Pingyang He
 *
 */
public class Settings {
	
	private final String SETTINGS_FILE_NAME = "settings";
	private final String ACCESS_TOKEN = "accessToken";
	private final String USERNAME = "username";
	
	private static Settings instance;
	private static Context context;
	private SharedPreferences settings;
	private SharedPreferences.Editor settingsEditor;
			
	//constructor
	private Settings(){
		settings = context.getSharedPreferences(SETTINGS_FILE_NAME, 0);
		settingsEditor = settings.edit();
	}
	
	/**
	 * return the instance of this class
	 * @param c set the context to c if this context is not defined
	 * @return the instance of this class
	 */
	public static Settings getInstance(Context c){
		if(context == null) context = c;
		if(instance == null) instance = new Settings();
		return instance;
	}
	
	/**
	 * save the given username and access token
	 * @param ui
	 */
	public void saveUserInfo(String ui){
		try {
			JSONObject user = new JSONObject(ui);
			settingsEditor.putString(USERNAME, user.getString("username"));
			settingsEditor.putString(ACCESS_TOKEN, user.getString("access_token"));
			settingsEditor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return whether the user is logged in
	 */
	public boolean hasAt(){
		return settings.getString(ACCESS_TOKEN, null) != null;
	}
	
	/**
	 * 
	 * @return the user name
	 */
	public String getUsername(){
		return settings.getString(USERNAME, null);
	}
	
	/**
	 * log user out
	 */
	public void logout(){
		settingsEditor.remove(USERNAME);
		settingsEditor.remove(ACCESS_TOKEN);
		settingsEditor.commit();
	}
	
	/**
	 * 
	 * @return the access token of user
	 */
	public String getAt(){
		return settings.getString(ACCESS_TOKEN, null);
	}
	
}
