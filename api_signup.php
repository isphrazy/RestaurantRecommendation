<?php

	include 'b_signupck.php';
	
	connect_db();
	
	get_q();

	validation();
	
	if(isset($todo) and $todo=="post") {
		
		if(!$status) {
			foreach($msg as $m){
				print($msg);
			} 
		} else {
			
			print(insert_into_db() ? $access_token : "database error");
		}
	}

?>
