<?php

	include 'b_detail.php';
	
	define('B_NAME', 'Business Name');
	define('ADD', 'Address');
	define('PHONE_NUM', 'Phone number');
	define('CATEGORY', 'Category');
	define('B_NAME', 'Business Name');
	
	$r_info = getR();
	
	print_info($r_info);
	
	
	function print_info($r_info){
	
		?>
		<?= $r_info[B_NAME]?> <br/>
		<?= $r_info[CATEGORY]?> <br/>
		<?= $r_info[ADD]?> <br/>
		<?= $r_info[PHONE_NUM]?> <br/>
		<?php
		foreach($r_info as $entry=>$e_detail){
			
			if($entry != B_NAME && $entry != CATEGORY && $entry != ADD && $entry != PHONE_NUM){
				?>
					<?= $entry?>: <?= $e_detail?> <br/>
				<?php
			}
				
		}
		
	}

?>
