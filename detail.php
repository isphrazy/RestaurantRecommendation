<?php
session_start();

include 'backend/b_detail.php';
include 'pattern.php';

print_head();
print_login();

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
	<div id="detail">
		<table>
		<tr>
			<td class="didyou detail_top">
				<h1><?= $r_info[B_NAME]?></h1>
				<span class="metadataAttr">Categories:</span>
				<span class="metadataValue"><?= $r_info[CATEGORY]?></span><br />
				<span class="metadataValue"><?= $r_info[ADD]?></span><br />
				<span class="metadataValue"><?= $r_info[PHONE_NUM]?></span>
				<br />
				<br />
				<span id="overview">Overview</span><br />
				<span class="metadataAttr">Food: </span>
				<span class="metadataValue"><?php $reviews[0] > 0 ? print round($reviews[0], 1) : ''?></span><br />
				
				<span class="metadataAttr">Service: </span>
				<span class="metadataValue"><?php $reviews[1] > 0 ? print round($reviews[1], 1) : ''?></span><br />
				
				<span class="metadataAttr">Decor: </span>
				<span class="metadataValue"><?php $reviews[2] > 0 ? print round($reviews[2], 1) : ''?></span>
			</td>
		</tr>
		<?php
		foreach($r_info as $entry=>$e_detail){	
			if($entry != B_NAME && $entry != CATEGORY && $entry != ADD && $entry != PHONE_NUM 
			&& $entry != ID && $entry != REVIEWS){
				?>
					<tr><td>
						<span class="metadataAttr"><?= $entry?>:</span>
						<span class="metadataValue right"><?= $e_detail?></span>
					</td></tr>
				<?php
			}		
		}
		?>
		</table>
	</div>
	<?php
}
print_bottom();
?>