<?php
	include 'background_profile.php';
	session_start();

	$rid = $_POST["rid"];
	$is_like = $_POST["like"];
	
	connect_db_and_define_access_token();
	
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
?>