<?php
	
	include '../backend/search.php';
	
	define('NO_RESTAURANT_FOUND_MESSAGE', -1);
	define('MANY_RESTAURANTS_FOUND_MESSAGE', 0);

	get_r_name();
	
	if(!$found){
		$nFound = 0;
		if($new_restaurant_name != ''){
			
			$search_result = search_restaurant($new_restaurant_name);

			$nFound = count($search_result);
		}
		if($nFound === 0){
			print(NO_RESTAURANT_FOUND_MESSAGE);
		}else if($nFound === 1){
			$found = true;
			$name_array = array_keys($search_result);
			$new_restaurant_name = $name_array[0];
		}else{//found several restaurants
			print(json_encode(array(MANY_RESTAURANTS_FOUND_MESSAGE, $search_result)));
		}
	}
	
	if($found){
		
		$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);
		
		$restaurant_basic_info = $restaurants_basic_info_json[$new_restaurant_name];
		
		$new_f_restaurant = generate_favorite_restaurant($restaurant_basic_info);
		
		//this list contains user's favorite restaurants
		$favorite_restaurants_list = generate_f_list($new_f_restaurant);
			
		//the list contains relevant restaurants
		$relevant_restaurants_list = generate_relevant_restaurants_list($favorite_restaurants_list);

		//sorts the relevant restaurants list based on relevance, qualities, and price 
		usort($relevant_restaurants_list, 'cmp');
		
		//we only want the first MAX_RELEVANT_RESTAURANTS restaurants
		array_splice($relevant_restaurants_list, MAX_RELEVANT_RESTAURANTS);

		//prints the relevant restaurants list.
		print(json_encode($relevant_restaurants_list));
		
	}
	

?>