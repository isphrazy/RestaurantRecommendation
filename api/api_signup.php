<?php

	include '../backend/b_signupck.php';
	include '../backend/connect_db.php';
	
	connect_db();
	
	$username=$_REQUEST['username'];
	$password=$_REQUEST['password'];
	$password2=$_REQUEST['password2'];
	$agree=$_REQUEST['agree'];
	$email=$_REQUEST['email'];

	validation();
	
	
	if(!$status) {
		print(json_encode($msg));
	} else {
		print(insert_into_db() ? $access_token : "-1");
	}

?>