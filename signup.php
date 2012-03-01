<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
  "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>RevMiner Likeness</title>
  <script type="text/javascript">
  //<![CDATA[

  function validate(form) { 
	  if (!document.form1.agree.checked) { alert("Please Read the guidlines and check the box below  ."); 
	  return false; } 
	  return true;
  }
  //]]>
  </script>
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">
  <form name="form1" method="post" action="backend/signupck.php" onsubmit='return validate(this)'
  id="form1">
  <input type="hidden" name="todo" value="post" />

  <table border='0' width='50%' cellspacing='0' cellpadding='0' align="center">
    <tr bgcolor='#F1F1F1'>
      <td align="center" colspan="2"><font face='Verdana' size=
      '2'><b>Sign Up</b></font></td>
    </tr>

    <tr>
      <td>&nbsp;<font face='Verdana' size='2'>Username</font></td>

      <td><font face='Verdana' size='2'><input type="text" name="username" /></font></td>
    </tr>

    <tr bgcolor='#F1F1F1'>
      <td>&nbsp;<font face='Verdana' size='2'>Password</font></td>

      <td><font face='Verdana' size='2'><input type="password" name="password" /></font></td>
    </tr>

    <tr>
      <td>&nbsp;<font face='Verdana' size='2'>Re-enter Password</font></td>

      <td><font face='Verdana' size='2'><input type="password" name=
      "password2" /></font></td>
    </tr>
	
    <tr bgcolor='#F1F1F1'>
      <td><font face='Verdana' size='2'>&nbsp;Email</font></td>

      <td><input type="text" name="email" /></td>
    </tr>
	
	<!--
    <tr>
      <td>&nbsp;<font face='Verdana' size='2'>Name</font></td>

      <td><font face='Verdana' size='2'><input type="text" name="name" /></font></td>
    </tr>
	-->
	
	<!--
    <tr bgcolor='#F1F1F1'>
      <td>&nbsp;<font face='Verdana' size='2'>Sex</font></td>

      <td><font face='Verdana' size='2'><input type='radio' value="male" checked=
      "checked" name='sex' />Male <input type='radio' value="female" name=
      'sex' />Female</font></td>
    </tr>
	-->

    <tr>
      <td>&nbsp;<font face='Verdana' size='2'>I agree to terms and conditions</font></td>

      <td><font face='Verdana' size='2'><input type="checkbox" name="agree" value=
      'yes' /></font></td>
    </tr>

    <tr bgcolor='#F1F1F1'>
      <td align="center" colspan="2"><input type="submit" value="Create Account" /></td>
    </tr>
  </table>
  </form>
</body>
</html>