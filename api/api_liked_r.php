<?php

	include '../backend/b_profile.php';
	
	get_restaurants();
	// $result = array();
	// foreach($restaurants as $r){
		// $result[] = $r;
	// }
	print(json_encode(array_keys($restaurants)));

?>