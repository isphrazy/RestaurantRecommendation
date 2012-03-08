<?php

	include '../backend/search.php';
	
	get_restaurants();
	initiate_vars();
	set_reviews_weigth();
	print(json_encode(generate_users_recommendation()));

?>