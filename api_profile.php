<?php
	include 'backgroudn_profile.php';
	get_restaurants();
	print json_encode($restaurants);
?>