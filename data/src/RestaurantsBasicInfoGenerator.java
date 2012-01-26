import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class RestaurantsBasicInfoGenerator {
	
	private static final String SOURCE_FILE_NAME = "Restaurants.data";
	private static final String ADJ_SCORE_FILE_NAME = "adjScore.data";
	private static final String RESTAURANTS_REVIEW_FILE_NAME = "all.placeData";
	private static final String ATTRIBUTE_CATEGORY_FILE_NAME = "attributeCategories";
	private static final String TARGET_FILE_NAME = "restaurants_basic_info.data";
	private static final String PRICE_RANGE = "Price Range";
	private static final String CATEGORY = "Category";
	private static final String CATEGORY_COUNT = "Category Count";
	private static final String RESTAURANTS = "Restaurants";
	private static final String REVIEWS = "Reviews";
	private static final String FOOD = "Food";
	private static final String SERVICE = "Service";
	private static final String DECOR = "Decor";
	
	
	private static JSONObject restaurantsReviews;
	private static JSONObject attr;
	private static JSONObject adjScore;
	
	public static void main(String[] args) throws JSONException, IOException{
		Scanner scn = new Scanner(new File(SOURCE_FILE_NAME));
		StringBuilder sb = new StringBuilder();
		while(scn.hasNext()){
			sb.append(scn.nextLine());
		}
		JSONObject restaurants = new JSONObject(sb.toString());
		JSONObject basicRestaurants = new JSONObject();
		
		Scanner rReviewScn = new Scanner(new File(RESTAURANTS_REVIEW_FILE_NAME));
		StringBuilder sb2 = new StringBuilder();
		while(rReviewScn.hasNextLine()){
			sb2.append(rReviewScn.nextLine());
		}
		restaurantsReviews = new JSONObject(sb2.toString());

		Scanner attrScn = new Scanner(new File(ATTRIBUTE_CATEGORY_FILE_NAME));
		StringBuilder sb3 = new StringBuilder();
		while(attrScn.hasNextLine()){
			sb3.append(attrScn.nextLine());
		}
		attr = new JSONObject(sb3.toString());
		
		
		Scanner adjScn = new Scanner(new File(ADJ_SCORE_FILE_NAME));
		StringBuilder sb4 = new StringBuilder();
		while(adjScn.hasNextLine()){
			sb4.append(adjScn.nextLine());
		}
		adjScore = new JSONObject(sb4.toString());
		

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
			
			int[] scores = getScores(name);
			info.put(REVIEWS, scores);
			
			basicRestaurants.put(name, info);
		}
		
		FileWriter out = new FileWriter(TARGET_FILE_NAME);
		out.write(basicRestaurants.toString());
		out.close();
			
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws JSONException
	 * @throws FileNotFoundException
	 */
	private static int[] getScores(String name) throws JSONException, FileNotFoundException {
		
		int[] result = new int[3]; // food, service and decor
		JSONObject reviews = restaurantsReviews.getJSONObject(name);
		for(String typeName : JSONObject.getNames(reviews)){
			String type = attr.getString(typeName);
			JSONObject reviewAdjs = reviews.getJSONObject(typeName);
			int score = 0;
			for(String adj : JSONObject.getNames(reviewAdjs)){
				score += reviewAdjs.getInt(adj) * adjScore.getInt(adj);
			}
			if(type.equals(FOOD)){
				result[0] += score;
			}else if(type.equals(SERVICE)){
				result[1] += score;
			}else if(type.equals(DECOR)){
				result[2] += score;
			}
		}
		
		
		return result;
	}
}
