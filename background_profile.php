<?php
	define('RESTAURANT_BASIC_DATA_FILE', 'data/restaurants_basic_info.data');
	$restaurants = array();
	
	function get_restaurants(){
		global $restaurants;
		$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);
		$result = connect_database();
		while ( $row = mysql_fetch_assoc($result) ) {
			$rid = $row["rid"];
			$restaurant_basic_info = $restaurants_basic_info_json[$rid];
			$restaurants[$rid] = $restaurant_basic_info; // $restaurant_basic_info is an associative array
		}
	}
	
	function connect_database(){
		$host="mysql17.000webhost.com"; // Host name
		$username="a6591147_jinghao"; // Mysql username
		$password="admin123"; // Mysql password
		$db_name="a6591147_mydata"; // Database name

		// Connect to server and select databse.
		mysql_connect("$host", "$username", "$password")or die("cannot connect");
		mysql_select_db("$db_name")or die("cannot select DB");

		// Define $access_token
		if ( isset($_SESSION['SESS_ACCESS_TOKEN']) ) {
			$access_token=$_SESSION['SESS_ACCESS_TOKEN'];
		} else {
			$access_token=$_REQUEST["access_token"];
		}

		// To protect MySQL injection
		$access_token = stripslashes($access_token);
		$access_token = mysql_real_escape_string($access_token);

		$sql="SELECT rid FROM members, likes WHERE access_token='$access_token' and id=uid";
		return mysql_query($sql); // result
	}
?>