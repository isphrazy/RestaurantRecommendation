<?
session_start();
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
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <title>RevMiner Likeness</title>
</head>

<body>
<?
$username=$_POST['username'];
$password=$_POST['password'];
$password2=$_POST['password2'];
$agree=$_POST['agree'];
$todo=$_POST['todo'];
$email=$_POST['email'];

if(isset($todo) and $todo=="post") {
	$status = "OK";
	$msg="";
	
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

	if($status<>"OK") {
		echo "<font face='Verdana' size='2' color=red>$msg</font><br><input type='button' value='Retry' onClick='history.go(-1)'>";
	} else {
		if(mysql_query("INSERT INTO members(username,password,email) VALUES('$username','$password','$email')")) {
			// insert access_token for Android app			
			$id = "SELECT id FROM members WHERE username='$username' and password='$password'";
			$access_token = md5($id);
			mysql_query("UPDATE members SET access_token='$access_token' WHERE username='$username' and password='$password'");
			
			// welcome message
			echo "<font face='Verdana' size='2' color=green>Welcome! You have succesfully signed up.</font>";
			echo "<br />";
			echo "Redirect in 3 seconds...";
			
			// register sessions
			$_SESSION['SESS_USERNAME'] = $username;
			$_SESSION['SESS_ACESS_TOKEN'] = $access_token;
			
			// redirect
			$seconds = 3;
			$url = "index.php";
			echo "<script language=\"JavaScript\">window.setTimeout(\"window.location.href=\'$url\'\", $seconds*1000); </script>";
		} else {
			echo "Database Problem, please contact Site admin"; //echo mysql_error();
		}
	}
}
?>
</body>
</html>