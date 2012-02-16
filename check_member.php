<?php
	include 'connect_db.php';
	define('USERNAME_Q', 'myusername');
	define('PASSWORD_Q', 'mypassword');

	$myusername;
	$mypassword;
	
	function get_member(){
		global $myusername;
		global $mypassword;
		
		connect_db();

		// To protect MySQL injection
		$myusername = stripslashes($myusername);
		$mypassword = stripslashes($mypassword);
		$myusername = mysql_real_escape_string($myusername);
		$mypassword = mysql_real_escape_string($mypassword);

		$sql="SELECT * FROM members WHERE username='$myusername' AND password='$mypassword'";
		return mysql_query($sql); // result
	}
?>