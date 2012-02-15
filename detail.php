<?php

	include 'b_detail.php';
	
	define('B_NAME', 'Business Name');
	define('ADD', 'Address');
	define('PHONE_NUM', 'Phone number');
	define('CATEGORY', 'Category');
	define('PRICE', 'Price Range');
	define('REVIEWS', 'review');
	define('ID', 'id');
	
	$r_info = getR();
	
	print_info($r_info);
	
	
	function print_info($r_info){
		$reviews = $r_info[REVIEWS];
		?>
		<?= $r_info[B_NAME]?> <br/>
		<?= $r_info[CATEGORY]?> <br/>
		<?= $r_info[ADD]?> <br/>
		<?= $r_info[PHONE_NUM]?> <br/>
		Price Range: <?= $r_info[PRICE]?> <br/>
		Reviews:Food: <?php $reviews[0] > 0 ? print round($reviews[0], 1) : ''?>
				Service: <?php $reviews[1] > 0 ? print round($reviews[1], 1) : ''?>
				Decor: <?php $reviews[2] > 0 ? print round($reviews[2], 1) : ''?>
		<?php
		foreach($r_info as $entry=>$e_detail){
			
			if($entry != B_NAME && $entry != CATEGORY && $entry != ADD && $entry != PHONE_NUM 
			&& $entry != PRICE && $entry != ID && $entry != REVIEWS){
				?>
					<?= $entry?>: <?= $e_detail?> <br/>
				<?php
			}
				
		}
		
	}

?>
