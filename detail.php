<?php

	define('R_DATA', 'data/Restaurants.data');

	$r_id = $_REQUEST["name"];
	
	function getR(){
		
		global $r_id;
		json_decode('R_DATA');
			
	}

?>
