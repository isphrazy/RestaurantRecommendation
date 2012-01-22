import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class RestaurantsBasicInfoGenerator {
	
	private static final String SOURCE_FILE_NAME = "Restaurants.data";
	private static final String TARGET_FILE_NAME = "restaurants_basic_info.data";
	private static final String PRICE_RANGE = "Price Range";
	private static final String CATEGORY = "Category";
	private static final String CATEGORY_COUNT = "Category Count";
	private static final String RESTAURANTS = "Restaurants";
	
	public static void main(String[] args) throws JSONException, IOException{
		Scanner scn = new Scanner(new File(SOURCE_FILE_NAME));
		
		JSONObject restaurants = new JSONObject(scn.nextLine());
		JSONObject basicRestaurants = new JSONObject();
		
		for(String name : JSONObject.getNames(restaurants)){
			JSONObject restaurant = restaurants.getJSONObject(name);
			
			JSONObject info = new JSONObject();
			try{
				String price = restaurant.getString(PRICE_RANGE);
				info.put(PRICE_RANGE, restaurant.getString(PRICE_RANGE));
			}catch (JSONException e){
				info.put(PRICE_RANGE, "");
			}
			
			String[] pre_category = restaurant.getString(CATEGORY).split(", *");
			//get rid of "Restaurant" category.
			String[] category = new String[pre_category.length - 1];
			int offset = 0;
			for(int i = 0; i < pre_category.length; i++){
				if(!pre_category[i].equals(RESTAURANTS)){
					category[i - offset] = pre_category[i];
				}else offset ++;
			}
			info.put(CATEGORY, category);
			
			Integer categoryCount = category.length;
			info.put(CATEGORY_COUNT, categoryCount);
			
			basicRestaurants.put(name, info);
		}
		
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(basicRestaurants.toString());
		out.close();
			
	}
}
