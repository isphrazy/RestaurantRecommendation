<?php
	include 'background_profile.php';
	get_restaurants();
	print json_encode($restaurants);
?>
