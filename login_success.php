<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<?
session_start();
if ( !isset($_SESSION['SESS_USERNAME']) ) {
	header("location:main_login.php");
}
?>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>RevMiner Likeness</title>
</head>

<body>
	<?php header("location:index.php"); ?>
</body>
</html>