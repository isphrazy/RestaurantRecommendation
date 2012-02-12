<?php
	
	include 'pattern.php';
	include 'search.php';
	
	print_head();
	
	print_login();
	
	print_search_bar();
	
	get_r_name();
	
	if(!$found){
		$nFound = 0;
		if($new_restaurant_name != ''){
			
			$search_result = search_restaurant($new_restaurant_name);

			$nFound = count($search_result);
		}
		if($nFound === 0){
			print_not_restaurant_found();
		}else if($nFound === 1){
			$found = true;
			$name_array = array_keys($search_result);
			$new_restaurant_name = $name_array[0];
		}else{//found several restaurant
			print_restaurant_choices($search_result);
		}
	}
	
	if($found){
		
		$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);
		
		$restaurant_basic_info = $restaurants_basic_info_json[$new_restaurant_name];
		
		$new_f_restaurant = generate_favorite_restaurant($restaurant_basic_info);
		
		//this list contains user's favorite restaurants
		$favorite_restaurants_list = add_to_favorite_restaurants_list($new_f_restaurant);
			
		//the list contains relevant restaurants
		$relevant_restaurants_list = generate_relevant_restaurants_list($favorite_restaurants_list);

		//sorts the relevant restaurants list based on relevance, qualities, and price 
		usort($relevant_restaurants_list, 'cmp');

		//prints the relevant restaurants list.
		print_relevant_restaurants_list($relevant_restaurants_list);
		
	}
	
	print_bottom();
		
?>
