<?php

	include 'b_signupck.php';
	
	session_start();
	
	connect_db();

	?>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
	  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

	<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
	  <title>RevMiner Likeness</title>
	</head>

	<body>
	<?php
	
	get_q();

	validation();

	if(isset($todo) and $todo=="post") {
		
		if($status) {
			echo "<font face='Verdana' size='2' color=red>$msg</font><br><input type='button' value='Retry' onClick='history.go(-1)'>";
		} else {
			
			$success = insert_into_db();
			
			if($success){
				// welcome message
				?><font face='Verdana' size='2' color=green>Welcome! You have succesfully signed up.</font>
				 <br />
				 Redirect in 3 seconds...
				<?php
				
				// register sessions
				$_SESSION['SESS_USERNAME'] = $username;
				$_SESSION['SESS_ACCESS_TOKEN'] = $access_token;
				
				// redirect
				$seconds = 3;
				$url = "index.php";
				echo "<script language=\"JavaScript\">window.setTimeout(\"window.location.href=\'$url\'\", $seconds*1000); </script>";
			}else{
				echo "Database Problem, please contact Site admin"; //echo mysql_error();
			}
		}
	}
?>
</body>
</html>
