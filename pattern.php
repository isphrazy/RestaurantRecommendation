<?php
	function print_head() {
		session_start();
	?>
		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN""http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
				<title>RevMiner Likeness</title>
				<link href="index.css" type="text/css" rel="stylesheet" />
				<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.1.0/prototype.js" type="text/javascript"></script>
				<!--<script src="index.js" type="text/javascript"></script>-->
			</head>

			<body>
	<?php		
	}
	
	function print_login(){
		if ( isset($_SESSION['SESS_USERNAME']) ) {
			?><span id="hello">Hello, <?=$_SESSION['SESS_USERNAME']?>.</span>
			<div id="login">
				<a href="logout.php">Log out</a> 
			</div>
		<?php
		} else {
		?>
			<div id="login">
				<a href="./main_login.php">Sign In</a> 
			</div>
		<?php
		}
	}

	function print_search_bar(){
	?>
		<div id="search">
			<a href="index.php"><img src="logo.png" alt="logo"/></a>
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