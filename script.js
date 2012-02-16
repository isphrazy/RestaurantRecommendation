function like(rid) {
	new Ajax.Request("is_like.php",
	  {
		parameters: {rid: rid, like: 1},
		onSuccess: function() {
			//$(rid).className += " like_dull";
			$(rid).setAttribute("src", "liked.png");
			$(rid).parentNode.removeAttribute('href');
			$(rid).parentNode.removeAttribute('onclick');
		}
	  }
	);
}

function unlike(rid) {
	new Ajax.Request("is_like.php",
	  {
		parameters: {rid: rid, like: 0},
		onSuccess: function() {
			$(rid).remove();
		}
	  }
	);
}

function redirect() {
	window.location = "main_login.php";
}