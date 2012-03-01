<?php
	include 'connect_db.php';

	define('RESTAURANT_BASIC_DATA_FILE', '/home/a6591147/public_html/454/data/restaurants_basic_info.data');
	$restaurants = array();
	$access_token;
	
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
?>