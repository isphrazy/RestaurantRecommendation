function like(rid) {
	new Ajax.Request("is_like.php",
	  {
		parameters: {rid: rid, like: 1},
		onSuccess: function() {
			//$(rid).style.opacity = '0.2';
			$(rid).className += " like_dull";
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
	window.location = "signup.php";
}