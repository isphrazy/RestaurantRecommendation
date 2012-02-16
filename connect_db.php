<?php
  function connect_db(){
		$host="mysql17.000webhost.com"; // Host name
		$username="a6591147_jinghao"; // Mysql username
		$password="admin123"; // Mysql password
		$db_name="a6591147_mydata"; // Database name

		// Connect to server
		mysql_connect("$host", "$username", "$password")or die("cannot connect");
		mysql_select_db("$db_name")or die("cannot select DB");
	}
?>