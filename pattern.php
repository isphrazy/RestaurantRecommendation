<?php

	function print_head() {
		session_start();
		?>
		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN""http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
		<html xmlns="http://www.w3.org/1999/xhtml">
		
			<head>
				<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
				<title>RevMiner Likeness</title>
				<link href="static/index.css" type="text/css" rel="stylesheet" />
				<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyD3zWUpfvRWkOcH0HAvGSD4J5y7YFEvpXA&sensor=false"></script>
				<script src="static/script.js" type="text/javascript"></script>
				<script src="http://ajax.googleapis.com/ajax/libs/scriptaculous/1.8.3/scriptaculous.js" type="text/javascript"></script>
				<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.1.0/prototype.js" type="text/javascript"></script>
			</head>

			<body>
		<?php	
	}
	
	function print_login(){
		#if ( isset($_SESSION['SESS_USERNAME']) ) {
		if ( isset($_SESSION['SESS_ACCESS_TOKEN']) ) {
			?>
			<div id="login">
				<a id="hello" href="profile.php">Hello, <?=$_SESSION['SESS_USERNAME']?>.</a>
				<a href="logout.php">Log out</a> 
			</div>
		<?php
		} else {
		?>
			<div id="login">
				<a href="login.php">Sign In</a> 
			</div>
		<?php
		}
	}

	function print_search_bar(){
	?>
		<div id="search">
			<a href="index.php"><img src="static/logo.png" alt="logo"/></a>
			<form action="result.php">
				<input type="text" name="restaurant_name" autofocus="" id="searchBox">
				<button class="myButton" type="submit">Stumble!</button>
			</form>
		</div>
	<?php
	}

	function print_bottom(){
		?>
		</body>
	</html>
	<?php
	}

?>