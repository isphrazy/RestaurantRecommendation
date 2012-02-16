<?php

	include 'check_member.php';
	
	// Define $myusername and $mypassword
	$myusername=$_REQUEST[USERNAME_Q];
	$mypassword=$_REQUEST[PASSWORD_Q];
	
	$result = get_member();
	$result2 = mysql_fetch_assoc($result);
/*
	print $result2;
*/
	print(json_encode($result2));
	
	

?>
