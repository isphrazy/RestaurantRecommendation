<?php

/*
	include 'connect_database.php';
*/
	define('USERNAME_Q', 'myusername');
	define('PASSWORD_Q', 'mypassword');

	$myusername;
	$mypassword;
	
	$result = connect_database();
	$result2 = mysql_fetch_assoc($result);
/*
	print $result2;
*/
	print(json_encode($result2));
	
	
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
		$myusername=$_REQUEST['myusername'];
		$mypassword=$_REQUEST['mypassword'];
		

		// To protect MySQL injection
		$myusername = stripslashes($myusername);
		$mypassword = stripslashes($mypassword);
		$myusername = mysql_real_escape_string($myusername);
		$mypassword = mysql_real_escape_string($mypassword);

		$sql="SELECT * FROM $tbl_name WHERE username='$myusername' and password='$mypassword'";
		$result = mysql_query($sql); // result
		return $result;
	}

?>
