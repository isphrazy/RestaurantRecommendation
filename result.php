<?php
	
	include 'pattern.php';
	
	define('SEARCH_FILE', 'data/SearchDatabase.data');
	define('RESTAURANT_BASIC_DATA_FILE', 'data/restaurants_basic_info.data');
	define('CATEGORY_DATA_FILE', 'data/Category.data');
	define('BUSINESS_NAME', 'Business Name');
	define('ADDRESS', 'Address');
	define('PRICE_RANGE', 'Price Range');
	define('CATEGORY', 'Category');
	define('CATEGORY_COUNT', 'Category Count');
	define('RESTAURANT', 'Restaurants');
	
	$restaurants_basic_info_json;
	
	print_head();
	
	print_search_bar();
	
	$new_restaurant_name = $_REQUEST["restaurant_name"];
	$new_restaurant_name = trim($new_restaurant_name);
	//if it's true, then this call is by this file itself, and $new_restaurant_name
	//is the key of the restaurant.
	//if not set, then we will search restaurant name to find the restaurant.
	$found = isset($_REQUEST["sure"]);
	
	
	if(!$found){
		$search_result = search_restaurant($new_restaurant_name);

		$nFound = count($search_result);
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
		
		$restaurant_basic_info = get_restaurant_basic_info($new_restaurant_name);
/*
		var_dump($restaurant_basic_info);
*/
		print_restaurant_info($restaurant_basic_info);
		
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
		
	/*
	 * abstract class for restaurant
	 */
	abstract class Restaurant{
		public $name;
		public $price;
		public $reviews_weight;
		public $all_detail;
	}
	
	/*
	 * restaurant that is relevant to the given restaurants
	 */
	class RelevantRestaurant extends Restaurant{
		public $relevance; //similarity of this restaurant to user's favorite
	}
	
	/*
	 * restaurant that is saved as user's favorite restaurant
	 */
	class FavoriteRestaurant extends Restaurant{
		public $category;
	}

	function cmp($r1, $r2) {
		/*Take the following factors into consideration:
		  $r->price
		  $r->reviews_weight (an array of size 3, representing food, service and decor)
		  $r->relevance (related to FavoriteRestaurant's $category; currently a random decimal)	  
		*/
		if ( $r1->relevance == $r2->relevance ) {
			if ( $r1->price == $r2->price ) {
				if ( $r1->reviews_weight[0] == $r2->reviews_weight[0] ) {
					return 0;
				}
				return ( $r1->reviews_weight[0] < $r2->reviews_weight[0] ) ? 1 : -1;
			}
			return ( $r1->price < $r2->price ) ? -1 : 1; // ascending order
		}
		return ( $r1->relevance < $r2->relevance ) ? 1 : -1;
	}
	
	/*
	 * search the given restaurant name in database. 
	 * */
	function search_restaurant($new_restaurant_name){
		
		$search_file = file_get_contents(SEARCH_FILE);
		$search_json = json_decode($search_file, true);
		$search_result = array();
		foreach($search_json as $r_name => $attr_array){
			if(strlen(stristr($attr_array[BUSINESS_NAME], $new_restaurant_name)) > 0){
				$search_result[$r_name] = $attr_array;
			}
		}
		return $search_result;
	}
	
	
	/*
	 * returns the restaurant info with given restaurant name
	 */
	function get_restaurant_basic_info($new_restaurant_name){
		
/*
		$restaurant_file = file_get_contents(RESTAURANT_BASIC_DATA_FILE);
*/
		global $restaurants_basic_info_json;
		return $restaurants_basic_info_json[$new_restaurant_name];
		
	}
	

	/*
	 * return an instance of FavoriteRestaurant with the given restaurant info
	 */ 
	function generate_favorite_restaurant($restaurant_basic_info){
		
		$f_restaurant = new FavoriteRestaurant();
		$f_restaurant->name = $restaurant_basic_info[BUSINESS_NAME];
		$f_restaurant->price = $restaurant_basic_info[PRICE_RANGE];
		//$f_restaurant->all_details = $restaurant_info;
		$f_restaurant->category = $restaurant_basic_info[CATEGORY];
		
		return $f_restaurant;
	}
	
	
	/*
	 * search database to find relevant restaurants;
	 */
	function generate_relevant_restaurants_list($favorite_restaurants_list){
		
		global $restaurants_basic_info_json;
		
		$relevant_restaurants_list = array();
		
		//store favorite restaurants' categories and their frequences in the array.
		$category_list = array();
		$category_count = 0;
		foreach($favorite_restaurants_list as $f_restaurant){
			$categories = $f_restaurant->category;
			foreach($categories as $category){
				//creat such category if not exist, otherwise, increase by one.
				$category_list[$category]++;
				$category_count++;
			}
		}
		
		$unique_category = 'Unique Category';
		$total_category_count = 'total category count';
		
		$category_json = json_decode(file_get_contents(CATEGORY_DATA_FILE), true);
		$relevant_restaurants_count = array();
		foreach($category_list as $category => $frequency){
			$restaurants_array = $category_json[$category];
			foreach($restaurants_array as $restaurant){
				$relevant_restaurants_count[$restaurant][$total_category_count] += $frequency;
				$relevant_restaurants_count[$restaurant][$unique_category_count] ++;
			}
		}
		
		foreach($relevant_restaurants_count as $r_name => $category_count_array){
			$relevant_restaurant = new RelevantRestaurant();
			$relevant_restaurant->name = $r_name;
			$relevant_restaurant_basic_info = $restaurants_basic_info_json[$r_name];
			$relevant_restaurant->price = $relevant_restaurant_basic_info[PRICE_RANGE];
			$relevant_category_count = $relevant_restaurant_basic_info[CATEGORY_COUNT];
			
			$relevant_restaurant -> relevance = 
				      (1.0 * $category_count_array[$unique_category_count] / $relevant_category_count) 
					* (1.0 * $category_count_array[$total_category_count] / $category_count);
			$relevant_restaurants_list[] = $relevant_restaurant;//apend this restaurant.
			
		}
		
		//create random relevant restaurant list so far
/*
		for($i = 0; $i < 10; $i++){
			$r = new RelevantRestaurant();
			$r->name = random_string(10);//set name to random string with length of 10 chars
			$r->price = rand(1, 100);
			$r->relevance = rand(1, 100) / 100;
			
			//reviews weight generator
			$food_weight = rand(1, 100);
			$service_weight = rand(1, 100);
			$decor_weight = rand(1, 100);
			$total_weight = $food_weight + $service_weight + $decor_weight;
			$r->reviews_weight = array( $food_weight / $total_weight, 
										$service_weight / $total_weight, 
										$decor_weight / $total_weight);
										
			$relevant_restaurants_list[] = $r; //append new restaurant
		}
*/
		return $relevant_restaurants_list;
	}
	

	/*
	 * add given restaurant to user's favorite restaurant list, then return this list.
	 */
	function add_to_favorite_restaurants_list($new_restaurant){
		//this list should get from database, which is part of milestone 3;
		$favorite_restaurants_list = array();
		
		$favorite_restaurants_list[] = $new_restaurant;
		return $favorite_restaurants_list;
	}
	
	function print_relevant_restaurants_list($relevant_restaurants_list){
		foreach($relevant_restaurants_list as $r){
			?> name: <?= $r->name?> price: <?= $r->price?>  relevance: <?= $r->relevance?> 
					 reviews_weight: <?= print_r ($r->reviews_weight) ?><br/><?php;
		}
	}

	function random_string($length){      
		$chars = 'bcdfghjklmnprstvwxzaeiou';

		for ($p = 0; $p < $length; $p++){
			$result .= ($p%2) ? $chars[mt_rand(19, 23)] : $chars[mt_rand(0, 18)];
		}

		return $result;
	}
	
	/*
	 * if the more than one restaurant match the serched restaurant, then let user
	 * select from these restaurants.
	 */
	function print_restaurant_choices($search_result){
		?>
		Do you mean:<br/>
		
		<?php
		
		foreach($search_result as $r_name => $attr_array){
			?>
			
			<a href='result.php?restaurant_name=<?=$r_name?>&sure=true'>
				<?=$attr_array[BUSINESS_NAME] . ', ' . $attr_array[Address]?>
			</a><br/>
			
			<?php
		}
	}
	
	
	/*
	 * tells user that the given restaurant name can not be found
	 */
	function print_not_restaurant_found(){
		global $new_restaurant_name;
		?>
		<br/> 
		Sorry, we can not find the restaurant <?= $new_restaurant_name ?>, please try again.
		<?php
	}
	
	/*
	 * print all overview infos about the given restaurant
	 */
	function print_restaurant_info($restaurant_info){
		//foreach($restaurant_info as $key => $value){
			$category_string;
			foreach($restaurant_info[CATEGORY] as $category){
				$category_string .= $category;
			}
			?>
			<p><span>Category: </span><span><?=$category_string?></span></p><br/>
			<p><span>Price Range: </span><span><?=$restaurant_info[PRICE_RANGE]?></span></p><br/>
			<?php
		//}
	}
?>
