function like(rid) {
	new Ajax.Request("is_like.php",
	  {
		parameters: {rid: rid, like: 1},
		onSuccess: function() {
			$(rid).remove();
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