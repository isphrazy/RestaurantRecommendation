<?php
	
/*
	include 'result.php';
*/

	$restaurants_basic_info_json;
	$favorite_restaurants_weight;
	$new_restaurant_name;
	$found;
	
/*
	get_r_name();
*/
	$new_restaurant_name = $_REQUEST["r_name"];
	$new_restaurant_name = trim($new_restaurant_name);
	//if it's true, then this call is by this file itself, and $new_restaurant_name
	//is the key of the restaurant.
	//if not set, then we will search restaurant name to find the restaurant.
	$found = isset($_REQUEST["sure"]);
	
	print($new_restaurant_name);

?>
