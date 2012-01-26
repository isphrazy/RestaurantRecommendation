import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class SearchDatabaseGenerator {
	
	private static final String SOURCE_FILE_NAME = "Restaurants.data";
	private static final String TARGET_FILE_NAME = "SearchDatabase.data";
	private static final String BUSINESS_NAME = "Business Name";
	private static final String ADDRESS = "Address";
	
	public static void main(String[] args) throws JSONException, IOException{
		
		Scanner scn = new Scanner(new File(SOURCE_FILE_NAME));
		StringBuilder sb2 = new StringBuilder();
		while(scn.hasNextLine()){
			sb2.append(scn.nextLine());
		}
		
		JSONObject restaurants = new JSONObject(sb2.toString());
		
//		Map<String, String> map = new HashMap<String, String>();
		
		StringBuilder sb = new StringBuilder("{");
		
		for(String name : JSONObject.getNames(restaurants)){
			sb.append("\"" + name + "\": {");
			JSONObject restaurant = restaurants.getJSONObject(name);
			String businessName = (String)restaurant.get(BUSINESS_NAME);
			String address = (String) restaurant.get(ADDRESS);
			sb.append("\"" + BUSINESS_NAME + "\": \"" + businessName + "\", " + 
					  "\"" + ADDRESS + "\": \"" + address + "\"},");
			
//			map.put(name, (String)restaurant.get(BUSINESS_NAME));
		}
		String result = sb.toString();
		result = result.substring(0, result.length() - 1) + "}";
//		System.out.println(result);
//		JSONObject BusinessNameMapToRestaurantName = new JSONObject(map);
		JSONObject resultJSON = new JSONObject(result);
//		
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(resultJSON.toString());
		out.close();
		
	}
}
