function removeLike(rid) {
	new Ajax.Request("removelike.php",
	  {
		parameters: {rid: rid},
		onSuccess: function() {
			$(rid).remove();
		}
	  }
	);
}
