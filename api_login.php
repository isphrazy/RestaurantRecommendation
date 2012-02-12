<?php

	include 'connect_database.php';
	
	$result = connect_database();
	$result2 = mysql_fetch_assoc($result);
	
	echo $result2['access_token'];
	

?>
