<?php
	include 'background_profile.php';
	session_start();

	$rid = $_POST["rid"];
	$is_like = $_POST["like"];

	connect_db_and_define_access_token();

	if ($is_like==1) {
		$sql = "INSERT likes
				VALUES((SELECT id FROM members WHERE access_token='$access_token'),'$rid')";
	} elseif ($is_like==0) {
		$sql = "DELETE FROM likes
				WHERE uid=(SELECT id FROM members WHERE access_token='$access_token')
				AND rid='$rid'";
	}
	mysql_query($sql);
?>