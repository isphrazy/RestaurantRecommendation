import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class BusinessNameMapToRestaurantName {
	
	private static final String SOURCE_FILE_NAME = "Restaurants.data";
	private static final String TARGET_FILE_NAME = "BusinessNameMapToRestaurantName.data";
	private static final String BUSINESS_NAME = "Business Name";
	
	public static void main(String[] args) throws JSONException, IOException{
		
		Scanner scn = new Scanner(new File(SOURCE_FILE_NAME));
		
		JSONObject restaurants = new JSONObject(scn.nextLine());
		
		Map<String, String> map = new HashMap<String, String>();
		
		for(String name : JSONObject.getNames(restaurants)){
			JSONObject restaurant = restaurants.getJSONObject(name);
			map.put((String)restaurant.get(BUSINESS_NAME), name);
		}
		
		JSONObject BusinessNameMapToRestaurantName = new JSONObject(map);
		
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(BusinessNameMapToRestaurantName.toString());
		out.close();
		
	}
}
