<?php
	include 'pattern.php';
	include 'html_scraper.php';
	
	define('REVMINER_URL', 'http://revminer.com/#');
	
	print_head();
	
	
	$new_restaurant_name = $_REQUEST["restaurant_name"];
	
	$new_restaurant = fetch_new_restaurant($new_restaurant_name);
	//this list contains user's favorite restaurants
	$favorite_restaurants_list = get_favorite_restaurants(); 
	//apend new restaurant
	$favorite_restaurants_list[] = $new_restaurant;
		
	//the list contains relevant restaurants
	$relevant_restaurants_list = generate_relevant_restaurants_list();
	
	//this method can print the relevant restaurants list.
	print_relevant_restaurants_list($relevant_restaurants_list);
	
	//Jinghao, your code will be in this function, relevant_restaurants_list contains the list of relevant restaurants
	//$relevant restaurants are generated randomly now, 
	rank_relevant_restaurants($relevant_restaurants_list);
	
	print_bottom();
		

	abstract class Restaurant{
		public $name;
		public $price;
		public $reviews_weight;
		
	}
	
	class RelevantRestaurant extends Restaurant{
		public $relevance; //similarity of this restaurant to the searched one
	}
	
	class FavoriteRestaurant extends Restaurant{
		public $category;
	}
	
	
	
	
	//Jinghao's code
	function rank_relevant_restaurants($relevant_restaurants_list){
		
	}
	
	
	
	
	
	
	//fetch restaurant info with given name from revminer, return a FavoriteRestaurant object
	//contains the info of the searched restaurant.
	//if the restaurant is not found, then give user several related restaurant, and let user choose
	function fetch_new_restaurant($restaurant_name){
		print "o_url: " . $restaurant_name . "\n";
		$restaurant_name = rawurlencode($restaurant_name);
		print "url: " . $restaurant_name . "<br/>";
		
		$new_restaurant = new FavoriteRestaurant();
/*
		$dom = file_get_html(REVMINER_URL . urlencode($restaurant_name));
*/	
		print "final url: " . REVMINER_URL . $restaurant_name . "<br/>";
		$html = file_get_contents(REVMINER_URL . $restaurant_name);
		
		print $html;
		
		
		$new_restaurant->name = random_string(10);//set name to random string with length of 10 chars
		$new_restaurant->price = rand(1, 100);
		
		return $new_restaurant;
	}
	
	//search database to find relevant restaurants;
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
										
			$relevant_restaurants_list[] = $r; //apend new restaurant
		}
		return $relevant_restaurants_list;
	}
	
	//return user's favorite restaurant list. This list is sotred in database
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

		for ($p = 0; $p < $length; $p++)
		{
			$result .= ($p%2) ? $chars[mt_rand(19, 23)] : $chars[mt_rand(0, 18)];
		}

		return $result;
	}
?>
