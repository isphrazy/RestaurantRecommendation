import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoriesJSONGenerator {
	
	private static final String SOURCE_FILE_NAME = "Restaurants.data";
	private static final String TARGET_FILE_NAME = "Category.data";
	private static final String CATEGORY = "Category";
	private static final String KEY_WORD = "Restaurants";
	private static final String ENCODE_AND = "&amp;";
	
	public static void main(String[] args) throws JSONException, IOException{
		Scanner scn = new Scanner(new File(SOURCE_FILE_NAME));
//		System.out.println("length: " + scn.nextLine().length());
		JSONObject allData = null;
		StringBuilder sb = new StringBuilder();
		while(scn.hasNext()){
			sb.append(scn.nextLine());
		}
		try{
			allData = new JSONObject(sb.toString());
			
		}catch (Exception e){
//			e.printStackTrace();
		}
		if(allData != null){
			Map map = new HashMap<String, LinkedList<String>>();
			for(String name : JSONObject.getNames(allData)){
				JSONObject restaurant = allData.getJSONObject(name);
				String[] categoryList = restaurant.getString(CATEGORY).split(", *");
//				if(categoryList.length > 2) System.out.println(restaurant.getString("Business Name"));
				for(String category : categoryList){
					//we don't want the word "restaurants", since it's in all of them
					if(!category.equals(KEY_WORD)){
						if(map.containsKey(category)){
							((LinkedList<String>) map.get(category)).add(name);
						}else{
							LinkedList<String> list = new LinkedList<String>();
							list.add(name);
							map.put(category, list);
						}
					}
				}
			}
			
			JSONObject categoryJSON = new JSONObject(map);
			FileWriter out = new FileWriter(TARGET_FILE_NAME);
			out.write(categoryJSON.toString());
			out.close();
			
		}
	}
	
}
