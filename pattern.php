<?php
	function print_head(){
	?>
		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN""http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<title>RevMiner Likeness</title>
				<link href="index.css" type="text/css" rel="stylesheet" />
				<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.1.0/prototype.js" type="text/javascript"></script>
				<script src="index.js" type="text/javascript"></script>
			</head>

			<body>
	<?php
		
	}

	function print_bottom(){
	?>
			</body>
		</html>
	<?php
	}
	
	function print_search_bar(){
	?>
		<div id="search">
			<div>
				<img src="logo.png" alt="logo"/>
			<div>
			<form action="http://www.pingyang.me/454/result.php">
				<input type="text" name="restaurant_name" autofocus="" id="searchBox">
				<button type="submit">Stumble!</button>
			</form>
		</div>
	<?php	
		
	}

?>
