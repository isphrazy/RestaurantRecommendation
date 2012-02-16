<?php
	include 'connect_db.php';
	session_start();

	$rid = $_POST["rid"];
	$is_like = $_POST["like"];
	
	connect_db();
	
	// Define $access_token
	if ( isset($_SESSION['SESS_ACCESS_TOKEN']) ) {
		$access_token=$_SESSION['SESS_ACCESS_TOKEN'];
	} else {
		$access_token=$_REQUEST["access_token"]; // for Android
	}
	if ( !empty($access_token) ) {
		$getid="SELECT id FROM members WHERE access_token='$access_token'";
		$result = mysql_query($getid);
		$row = mysql_fetch_assoc($result);
		$uid = $row['id'];
		
		if ($is_like==1) {
			$sql="INSERT likes VALUES('$uid','$rid')";
		} elseif ($is_like==0) {
			$sql="DELETE FROM likes WHERE uid='$uid' AND rid='$rid'";
		}
		mysql_query($sql);
	}
?>