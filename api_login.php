<?php

	include 'check_member.php';
	
	// Define $myusername and $mypassword
	$myusername=$_REQUEST[USERNAME_Q];
	$mypassword=$_REQUEST[PASSWORD_Q];
	
	$result = get_member();
	print(json_encode(mysql_fetch_assoc($result)));
	
?>
