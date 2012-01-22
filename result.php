<?php
	include 'pattern.php';
	
	define('REVMINER_URL', 'http://revminer.com/#');
	define('SEARCH_FILE', 'data/SearchDatabase.data');
	define('BUSINESS_NAME_RESTAURANT_FILE_NAME', 'data/BusinessNameMapToRestaurantName.data');
	define('BUSINESS_NAME', 'Business Name');
	define('ADDRESS', 'Address');
	
	print_head();
	
	$new_restaurant_name = $_REQUEST["restaurant_name"];
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
			print $new_restaurant_name;
		}else{//found several restaurant
			print_restaurant_choices($search_result);
		}

	}
	
	if($found){
		
		$new_restaurant = get_restaurant($new_restaurant_name);
		
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
		
		$restaurant_name = trim($restaurant_name);
		
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
	
	function print_not_restaurant_found($new_restaurant_name){
		print_search_bar();
		?>
			<br/> 
			Sorry, we can not find the restaurant <?= $new_restaurant_name ?>, please try again.
		<?php
	}
	
	
	function print_restaurant_choices($search_result){
		?>
		Do you mean:<br/>
		
		<?php
		
			foreach($search_result as $r_name => $attr_array){
				?>
				
				<a href='result.php?restaurant_name=' + <?=r_name?> + '&sure=true'>
					<?=$attr_array[BUSINESS_NAME] . ', ' . $attr_array[Address]?>
				</a><br/>
				
				<?php
				
			}
		
		?>
		
		<?php
	}
	
	
	function get_restaurant($new_restaurant_name){
		
		
	}
	
	//fetch restaurant info with given name from revminer, return a FavoriteRestaurant object
	//contains the info of the searched restaurant.
	//if the restaurant is not found, then give user several related restaurant, and let user choose
/*
	function fetch_new_restaurant($restaurant_name){
		print "o_url: " . $restaurant_name . "\n";
		$restaurant_name = rawurlencode($restaurant_name);
		print "url: " . $restaurant_name . "<br/>";
		
		$new_restaurant = new FavoriteRestaurant();
		print "final url: " . REVMINER_URL . $restaurant_name . "<br/>";
		$html = file_get_contents(REVMINER_URL . $restaurant_name);
		
		print $html;
		
		
		$new_restaurant->name = random_string(10);//set name to random string with length of 10 chars
		$new_restaurant->price = rand(1, 100);
		
		return $new_restaurant;
	}
*/
	
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
?>
