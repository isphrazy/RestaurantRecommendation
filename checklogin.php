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

	ob_start();
	
	$myusername;
	$mypassword;
	
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
	
	function connect_database(){
		global $myusername;
		global $mypassword;
		
		$host="mysql17.000webhost.com"; // Host name
		$username="a6591147_jinghao"; // Mysql username
		$password="admin123"; // Mysql password
		$db_name="a6591147_mydata"; // Database name
		$tbl_name="members"; // Table name

		// Connect to server and select databse.
		mysql_connect("$host", "$username", "$password")or die("cannot connect");
		mysql_select_db("$db_name")or die("cannot select DB");

		// Define $myusername and $mypassword
		$myusername=$_POST['myusername'];
		$mypassword=$_POST['mypassword'];

		// To protect MySQL injection
		$myusername = stripslashes($myusername);
		$mypassword = stripslashes($mypassword);
		$myusername = mysql_real_escape_string($myusername);
		$mypassword = mysql_real_escape_string($mypassword);

		$sql="SELECT * FROM $tbl_name WHERE username='$myusername' and password='$mypassword'";
		return mysql_query($sql); // result
	}
	?>
</body>
</html>