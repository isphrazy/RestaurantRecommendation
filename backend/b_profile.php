<?php
	include 'connect_db.php';

	define('RESTAURANT_BASIC_DATA_FILE', '/home/a6591147/public_html/454/data/restaurants_basic_info.data');
	$restaurants = array();
	$access_token;
	$favorite_restaurants_list;
	$new_restaurant_name;
	$restaurants_basic_info_json;
	$geocode_arr;
	
	function get_restaurants(){
		global $restaurants;
		global $access_token;
		
		$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);
		connect_db_and_define_access_token();
		$sql="SELECT rid FROM members, likes WHERE access_token='$access_token' and id=uid";
		$result = mysql_query($sql);
		while ( $row = mysql_fetch_assoc($result) ) {
			$rid = $row["rid"];
			$restaurant_basic_info = $restaurants_basic_info_json[$rid];
			$restaurants[$rid] = $restaurant_basic_info; // $restaurant_basic_info is an associative array
		}
	}
	
	function connect_db_and_define_access_token(){
		global $access_token;
		connect_db();

		// Define $access_token
		if ( isset($_SESSION['SESS_ACCESS_TOKEN']) ) {
			$access_token=$_SESSION['SESS_ACCESS_TOKEN'];
		} else {
			$access_token=$_REQUEST["access_token"]; // for Android
		}

		// To protect MySQL injection
		$access_token = stripslashes($access_token);
		$access_token = mysql_real_escape_string($access_token);
	}
	
	function initiate_vars(){
		global $favorite_restaurants_list;
		global $new_restaurant_name;
		global $restaurants_basic_info_json;

		$favorite_restaurants_list = array();

		$new_restaurant_name = "";
		$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);
	}
	
	function set_reviews_weigth(){
		global $favorite_restaurants_weight;
		global $favorite_restaurants_list;
		global $restaurants;
		$f_count=0;
		$s_count=0;
		$d_count=0;
		foreach ($restaurants as $rid => $restaurant) {
			$new_f_restaurant = generate_favorite_restaurant($restaurant);
			$favorite_restaurants_list[] = $new_f_restaurant;

			$f = $new_f_restaurant->reviews_weight[0];
			$s = $new_f_restaurant->reviews_weight[1];
			$d = $new_f_restaurant->reviews_weight[2];
			if ($f != 0) {
				$favorite_restaurants_weight[0] += $f;
				$f_count++;
			}
			if ($s != 0) {
				$favorite_restaurants_weight[1] += $s;
				$s_count++;
			}
			if ($d != 0) {
				$favorite_restaurants_weight[2] += $d;
				$d_count++;
			}
			
			$sum = $f_count + $s_count + $d_count;
			if($sum == 0){//no info about favorite over any of the three attributes
				$favorite_restaurants_weight[0] = 1/3;
				$favorite_restaurants_weight[1] = 1/3;
				$favorite_restaurants_weight[2] = 1/3;
			}else{
				$favorite_restaurants_weight[0] = $f_count / $sum;
				$favorite_restaurants_weight[1] = $s_count / $sum;
				$favorite_restaurants_weight[2] = $d_count / $sum;
			}
			
		}
	}
	
	function generate_users_recommendation(){
		global $relevant_restaurants_list;
		global $geocode_arr;
		global $favorite_restaurants_list;
		global $restaurants;
		//the list contains relevant restaurants
		
		$relevant_restaurants_list = generate_relevant_restaurants_list($favorite_restaurants_list);
		
		//sorts the relevant restaurants list based on relevance, qualities, and price 
		usort($relevant_restaurants_list, 'cmp');

		$count = 0;
		$filted_restaurant_list = array();
		$geocode_arr = array();
		foreach ($relevant_restaurants_list as $relevant_restaurant) {
			if ( !array_key_exists($relevant_restaurant->name, $restaurants) ) {
				$filted_restaurant_list[] = $relevant_restaurant;
				$geocode_arr[$relevant_restaurant->name] =
					array($relevant_restaurant->business_name,$relevant_restaurant->lat,$relevant_restaurant->lon);
				$count++;
			}
			if ($count==10)
				break;
		}
		return $filted_restaurant_list;
	}
?>