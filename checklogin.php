<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<?php
	session_start();
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>RevMiner Likeness</title>
</head>

<body>
<?php

	include 'connect_database.php';
	
	ob_start();
	
	// Define $myusername and $mypassword
	$myusername=$_POST[USERNAME_Q];
	$mypassword=$_POST[PASSWORD_Q];
	
	// Mysql_num_row is counting table row
	$result = connect_database();
	$count=mysql_num_rows($result);
	// If result matched $myusername and $mypassword, table row must be 1 row

	if($count==1){
		// Register $myusername, $mypassword and redirect to file "login_success.php"
		$_SESSION['SESS_USERNAME'] = $myusername;
		$_SESSION['SESS_PASSWORD'] = $mypassword;
		
		$member = mysql_fetch_assoc($result);
		$_SESSION['SESS_ACCESS_TOKEN'] = $member['access_token'];
		header("location:login_success.php");
	} else {
		echo "Wrong Username or Password";
	}

	ob_end_flush();
	
	?>
</body>
</html>
