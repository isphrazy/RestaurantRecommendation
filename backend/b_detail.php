<?php
	define('R_DATA', '/home/a6591147/public_html/454/data/Restaurants.data');
	define('R_REVIEW', '/home/a6591147/public_html/454/data/reviews.data');

	$r_id = $_REQUEST["name"];
	
	function getR(){
		global $r_id;
		$r_detail = json_decode(file_get_contents(R_DATA), true);
		$r_review = json_decode(file_get_contents(R_REVIEW), true);
		$result = $r_detail[$r_id];
		$result["review"] = $r_review[$r_id];
		$result["id"] = $r_id;
		return $result;
	}
?>