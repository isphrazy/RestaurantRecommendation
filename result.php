<?php
	
	include 'pattern.php';
	
	define('SEARCH_FILE', 'DataMiner/SearchDatabase.data');
	define('RESTAURANT_DATA', 'DataMiner/Restaurants.data');
	define('BUSINESS_NAME', 'Business Name');
	define('ADDRESS', 'Address');
	
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
			print_not_restaurant_found($new_restaurant_name);
		}else if($nFound === 1){
			$found = true;
			$name_array = array_keys($search_result);
			$new_restaurant_name = $name_array[0];
		}else{//found several restaurant
			print_restaurant_choices($search_result);
		}

	}
	
	if($found){
		
		$restaurant_info = get_restaurant($new_restaurant_name);
		print_restaurant_info($restaurant_info);
		
		$new_restaurant = generate_favorite_restaurant($restaurant_info);
		
		//this list contains user's favorite restaurants
		$favorite_restaurants_list = get_favorite_restaurants(); 
		//append new restaurant
		$favorite_restaurants_list[] = $new_restaurant;
			
		//the list contains relevant restaurants
		$relevant_restaurants_list = generate_relevant_restaurants_list();

		//ranks the relevant restaurants list.
		rank_relevant_restaurants($relevant_restaurants_list);

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
		
	}
	
	/*
	 * restaurant that is relevant to the given restaurants
	 */
	class RelevantRestaurant extends Restaurant{
		public $relevance; //similarity of this restaurant to the searched one
	}
	
	/*
	 * restaurant that is saved as user's favorite restaurant
	 */
	class FavoriteRestaurant extends Restaurant{
		public $category;
	}

	/*
	 * sort the relevant restaurants based on relevance, qualities, and price 
	 */
	function rank_relevant_restaurants($relevant_restaurants_list){
	/*Take the following factors into consideration:
	  $r->price
	  $r->reviews_weight (an array of size 3, representing food, service and decor)
	  $r->relevance (related to FavoriteRestaurant's $category; currently a random decimal)	  
	*/
		// Use uasort() - Sorts by value
	}
	
	/*
	 * search the given restaurant name in database. 
	 * */
	function search_restaurant($restaurant_name){
		
		$search_file = file_get_contents(SEARCH_FILE);
		$search_json = json_decode($search_file, true);
		$search_result = array();
		foreach($search_json as $r_name => $attr_array){
			if(strlen(stristr($attr_array[BUSINESS_NAME], $restaurant_name)) > 0){
				$search_result[$r_name] = $attr_array;
			}
		}
		return $search_result;
	}
	
	
	/*
	 * returns the restaurant info with 
	 */
	function get_restaurant($new_restaurant_name){
		
		$restaurant_file = file_get_contents(RESTAURANT_DATA);
		$restaurant_json = json_decode($restaurant_file, true);
		return $restaurant_json[$new_restaurant_name];
		
	}
	

	
	function generate_favorite_restaurant($restaurant_info){
		
		
	}
	
	
	/*
	 * search database to find relevant restaurants;
	 */
	function generate_relevant_restaurants_list(){
		$relevant_restaurants_list = array();
		
		//create random relevant restaurant list so far
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
		return $relevant_restaurants_list;
	}
	
	
	
	/*
	 * return user's favorite restaurant list. This list is sotred in database
	 */
	function get_favorite_restaurants(){
		$favorite_restaurants_list = array();
		
		return $favorite_restaurants_list;
	}
	
	function print_relevant_restaurants_list($relevant_restaurants_list){
		foreach($relevant_restaurants_list as $r){
			?>name: <?= $r->name?> price: <?= $r->price?>  relevance: <?= $r->relevance?> 
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
		
		?>
		
		<?php
	}
	
	
	/*
	 * tells user that the given restaurant name can not be found
	 */
	function print_not_restaurant_found($new_restaurant_name){
		
		?>
			<br/> 
			Sorry, we can not find the restaurant <?= $new_restaurant_name ?>, please try again.
		<?php
	}
	
	/*
	 * print all overview infos about the given restaurant
	 */
	function print_restaurant_info($restaurant_info){
		foreach($restaurant_info as $key => $value){
			
			?>
			<p><span><?=$key?>: </span><span><?=$value?></span></p><br/>
			
			<?php
			
		}
	}
?>
