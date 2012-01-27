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
	define('REVIEWS', 'Reviews');
	
	$restaurants_basic_info_json;
	$favorite_restaurants_weight;
	
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
		
		$restaurant_basic_info = $restaurants_basic_info_json[$new_restaurant_name];;
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
		public $all_detail;
		public $category;
		public $business_name;
		
	}
	
	/*
	 * restaurant that is relevant to the given restaurants
	 */
	class RelevantRestaurant extends Restaurant{
		public $relevance; //similarity of this restaurant to user's favorite
		public $reviews; //(eg: 12, 27, -3)
		public $ranking_score;
	}
	
	/*
	 * restaurant that is saved as user's favorite restaurant
	 */
	class FavoriteRestaurant extends Restaurant{
		public $reviews_weight; //(eg: 0.25, 0.5, 0.25)
		
	}

	function cmp($r1, $r2) {
		/*Take the following factors into consideration:
		  $r->price
		  $r->reviews_weight (an array of size 3, representing food, service and decor)
		  $r->relevance (related to FavoriteRestaurant's $category; currently a random decimal)	  
		*/
		/*
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
		* */
		if ( $r1->ranking_score == $r2->ranking_score){ return 0 ; }
		return ( $r1->ranking_score < $r2->ranking_score ) ? 1 : -1; // descending order
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
	 * return an instance of FavoriteRestaurant with the given restaurant info
	 */ 
	function generate_favorite_restaurant($restaurant_basic_info){
		
		$f_restaurant = new FavoriteRestaurant();
		$f_restaurant->name = $restaurant_basic_info[BUSINESS_NAME];
		$f_restaurant->price = $restaurant_basic_info[PRICE_RANGE];
		//$f_restaurant->all_details = $restaurant_info;
		$f_restaurant->category = $restaurant_basic_info[CATEGORY];
		$reviews = $restaurant_basic_info[REVIEWS];
		var_dump($restaurant_basic_info);
		$total_score = 0;
		for($i = 0; $i < count($reviews); $i++){
			$total_score += $reviews[$i];
		}
		
		if($total_score === 0) $f_restaurant->reviews_weight = array(0, 0, 0);
		else $f_restaurant->reviews_weight = array(1.0 * $reviews[0] / $total_score, 
												   1.0 * $reviews[1] / $total_score,
												   1.0 * $reviews[2] / $total_score);
		
		return $f_restaurant;
	}
	
	
	/*
	 * search database to find relevant restaurants;
	 */
	function generate_relevant_restaurants_list($favorite_restaurants_list){
		
		global $restaurants_basic_info_json;
		global $favorite_restaurants_weight;
		
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
		
		
		//calculate the categories count of each restaurants
		$unique_category = 0;
		$total_category_count = 1;
		
		$category_json = json_decode(file_get_contents(CATEGORY_DATA_FILE), true);
		$relevant_restaurants_count = array();
		foreach($category_list as $category => $frequency){
			$restaurants_array = $category_json[$category];
			foreach($restaurants_array as $restaurant){
				$relevant_restaurants_count[$restaurant][$total_category_count] += $frequency;
				$relevant_restaurants_count[$restaurant][$unique_category_count] ++;
			}
		}
		
		
		
		$F = $favorite_restaurants_weight[0];
		$S = $favorite_restaurants_weight[1];
		$D = $favorite_restaurants_weight[2];
		$relevance_weight = 0.5;
		$reviews_weight = 0.5; // a, b are weights
		
		foreach($relevant_restaurants_count as $r_name => $category_count_array){
			$relevant_restaurant = new RelevantRestaurant();
			$relevant_restaurant->name = $r_name;
			$relevant_restaurant_basic_info = $restaurants_basic_info_json[$r_name];
			$relevant_restaurant->price = $relevant_restaurant_basic_info[PRICE_RANGE];
			$relevant_category_count = $relevant_restaurant_basic_info[CATEGORY_COUNT];
			$relevant_restaurant->reviews = $relevant_restaurant_basic_info[REVIEWS];
			$relevant_restaurant->category = $relevant_restaurant_basic_info[CATEGORY];
			
			$relevant_restaurant -> relevance = 
				      (1.0 * $category_count_array[$unique_category_count] / $relevant_category_count) 
					* (1.0 * $category_count_array[$total_category_count] / $category_count);
					
			$relevant_restaurant->ranking_score = $relevant_restaurant->relevance * $relevance_weight + 
													($relevant_restaurant->reviews[0] * $F 
												   + $relevant_restaurant->reviews[1] * $S 
												   + $relevant_restaurant->reviews[2] * $D) * $reviews_weight;
			$relevant_restaurant->business_name = $relevant_restaurant_basic_info[BUSINESS_NAME];									   
												   
			$relevant_restaurants_list[] = $relevant_restaurant;//apend this restaurant.
			
		}
		
		return $relevant_restaurants_list;
	}
	

	/*
	 * add given restaurant to user's favorite restaurant list, then return this list.
	 */
	function add_to_favorite_restaurants_list($new_restaurant){
		
		global $favorite_restaurants_weight;
		
		//this list should get from database, which is part of milestone 3;
		$favorite_restaurants_list = array();
		
/*
		for($favorite_restaurants_list as $restaurant_name)
*/
		//wrong, will be changed in next milestone when login supported
		$favorite_restaurants_weight = $new_restaurant->reviews_weight;
		$favorite_restaurants_list[] = $new_restaurant;
		return $favorite_restaurants_list;
	}
	
	function print_relevant_restaurants_list($relevant_restaurants_list){
		?>
		<table>
		<?php
		foreach($relevant_restaurants_list as $r){
			?><tr><th>Name: <?=$r->business_name?></th></tr>
			<tr><td>Category:
				<?php
				foreach($r->category as $category){
					print $category . ' ';
				}
				?></td>
			<td>
				Food: <?=round($r->reviews[0], 1)?>
				Service: <?=round($r->reviews[1], 1)?>
				Decor: <?= round($r->reviews[2], 1)?>
			</td>
			<td>Price: <?=$r->price?></td>
			<!--<td>Relevance: <?=$r->relevance?></td>-->
			<!--<td>reviews_weight: <?=print_r ($r->reviews_weight)?></td></tr>-->
			<?php
		}?>
		</table>
		<?php
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
		<table>
		<tr><th>Do you mean:</th></tr>
		<?php
		
		foreach($search_result as $r_name => $attr_array){
			?>
			<tr><td>
				<a id="choice" href='result.php?restaurant_name=<?=$r_name?>&sure=true'>
					<?=$attr_array[BUSINESS_NAME] . ', ' . $attr_array[Address]?>
				</a>
			</td></tr>
			<?php
		}?>
		</table>
		<?php
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
		$category_string;
		foreach($restaurant_info[CATEGORY] as $category){
			$category_string .= $category;
		}
		?>
		<p><span>Category: </span><span><?=$category_string?></span></p><br/>
		<p><span>Price Range: </span><span><?=$restaurant_info[PRICE_RANGE]?></span></p><br/>
		<?php
	}
?>
