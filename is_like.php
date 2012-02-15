<?php
  session_start();

	$rid = $_POST["rid"];
	$host="mysql17.000webhost.com"; // Host name
	$username="a6591147_jinghao"; // Mysql username
	$password="admin123"; // Mysql password
	$db_name="a6591147_mydata"; // Database name

	// Connect to server and select databse.
	mysql_connect("$host", "$username", "$password")or die("cannot connect");
	mysql_select_db("$db_name")or die("cannot select DB");
	
	// Define $access_token
	if ( isset($_SESSION['SESS_ACCESS_TOKEN']) ) {
		$access_token=$_SESSION['SESS_ACCESS_TOKEN'];
	} else {
		$access_token=$_REQUEST["access_token"]; // for Android
	}
	$getid="SELECT id FROM members WHERE access_token='$access_token'";
	$result = mysql_query($getid);
	$row = mysql_fetch_assoc($result);
	$uid = $row['id'];
	
	$removelike="DELETE FROM likes WHERE uid='$uid' AND rid='$rid'";
	mysql_query($removelike);
?>