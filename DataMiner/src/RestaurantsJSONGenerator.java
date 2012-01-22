import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class RestaurantsJSONGenerator {
	
	private static final String SOURCE_FILE_NAME = "all.metaData";
	private static final String TARGET_FILE_NAME = "Restaurants.data";
	private static final String CATEGORY = "Category";
	private static final String KEY_WORD = "Restaurants";

	public static void main(String[] args) throws JSONException, IOException{
		
		Scanner scn = new Scanner(new File(SOURCE_FILE_NAME));
		
		JSONObject allData = new JSONObject(scn.nextLine());
		
		for(String name : JSONObject.getNames(allData)){
//			System.out.println(name);
			JSONObject restaurant = allData.getJSONObject(name);
			String category = null;
			try{
				//if the place do not have category, remove it
				category = restaurant.getString(CATEGORY);
			}catch (org.json.JSONException e){
				allData.remove(name);
//				System.out.println("no category: " + name);
			}
			if(category != null && !category.contains(KEY_WORD)){
				allData.remove(name);
//				System.out.println("removed: " + name);
			}
		}
		
		
		String s = allData.toString();
//		System.out.println("length: " + s.length());
//		System.out.println("last: " + s.substring(s.length() - 500));
//		System.out.println("size: " + allData.length());
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(s);
		out.close();
		
//		for(String name : JSONObject.getNames(allData)){
//			if(name.equals("jade-dragon-cuisine-redmond")){
//				JSONObject restaurant = allData.getJSONObject(name);
//				System.out.println(restaurant.getString("Business Name"));
//			}
////			System.out.println(name + " is: " + restaurant.getString("Category"));
//		}
		
	}
	
}
