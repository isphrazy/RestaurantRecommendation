<?php
	include 'background_profile.php';
	session_start();

	$rid = $_REQUEST["rid"];
	$is_like = $_REQUEST["like"];

	connect_db_and_define_access_token();

	if ($is_like===1) {
		$sql = "INSERT likes
				VALUES((SELECT id FROM members WHERE access_token='$access_token'),'$rid')";
	} elseif ($is_like===0) {
		$sql = "DELETE FROM likes
				WHERE uid=(SELECT id FROM members WHERE access_token='$access_token')
				AND rid='$rid'";
	}
<<<<<<< HEAD
	mysql_query($sql);
?>
=======
	return mysql_query($sql);
?>
>>>>>>> 872712b74cffd057965f7eec9928e5c061f326ab
