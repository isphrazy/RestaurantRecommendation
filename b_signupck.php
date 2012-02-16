<?php
	
	$username;
	$password;
	$password2;
	$agree;
	$todo;
	$email;
	
	$status;
	$msg;

	function connect_db(){
		
		$dbservertype='mysql';
		$servername='mysql17.000webhost.com';
		// username and password to log onto db server
		$dbusername='a6591147_jinghao';
		$dbpassword='admin123';
		// name of database
		$dbname='a6591147_mydata';

		connecttodb($servername,$dbname,$dbusername,$dbpassword);
		function connecttodb($servername,$dbname,$dbuser,$dbpassword)
		{
			global $link;
			$link=mysql_connect("$servername","$dbuser","$dbpassword");
			if(!$link){die("Could not connect to MySQL");}
			mysql_select_db("$dbname",$link) or die ("could not open db".mysql_error());
		}
			
	}


	function get_q(){
		
		global $username;
		global $password;
		global $password2;
		global $agree;
		global $todo;
		global $email;
		
		$username=$_POST['username'];
		$password=$_POST['password'];
		$password2=$_POST['password2'];
		$agree=$_POST['agree'];
		$todo=$_POST['todo'];
		$email=$_POST['email'];
			
	}

	function validation(){
		
		global $status;
		global $msg;
		
		// if username is less than 3 char then status is not ok
		if(!isset($username) or strlen($username) <3) {
			$msg=$msg."Username should be =3 or more than 3 char length<BR>";
			$status= "NOTOK";
		}					

		if(mysql_num_rows(mysql_query("SELECT username FROM members WHERE username = '$username'"))) {
			$msg=$msg."Username already exists. Please try another one<BR>";
			$status= "NOTOK";
		}			

		if ( strlen($password) < 3 ) {
			$msg=$msg."Password must be more than 3 char length<BR>";
			$status= "NOTOK";
		}			

		if ( $password <> $password2 ) {
			$msg=$msg."Passwords are not the same<BR>";
			$status= "NOTOK";
		}				

		if ($agree<>"yes") {
			$msg=$msg."You must agree to terms and conditions<BR>";
			$status= "NOTOK";
		}
	}
	
	function insert_into_db(){
		
		if(mysql_query("INSERT INTO members(username,password,email) VALUES('$username','$password','$email')")) {
			// insert access_token for Android app			
			$getid = "SELECT id FROM members WHERE username='$username' and password='$password'";
			$result = mysql_query($getid);
			$row = mysql_fetch_assoc($result);
			$id = $row['id'];
			$access_token = md5($id);
			mysql_query("UPDATE members SET access_token='$access_token' WHERE username='$username' and password='$password'");
			
			return true;
		} else {
			return false;
		}
	}
?>
