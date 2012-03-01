<?php
	include '../backend/b_profile.php';
	get_restaurants();
	print json_encode($restaurants);
?>
