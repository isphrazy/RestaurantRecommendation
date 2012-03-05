<?php

session_start();

include 'backend/b_detail.php';
include 'pattern.php';
include 'backend/search.php';

print_head();
print_login();
print_search_bar();

// define('B_NAME', 'Business Name');
// define('ADD', 'Address');
// define('PHONE_NUM', 'Phone number');
// define('CATEGORY', 'Category');
// define('PRICE', 'Price Range');
// define('REVIEWS_D', 'review');
// define('ID', 'id');

define('LAT', 'Latitude');
define('LNG', 'Longitude');

$r_info = getR();
print_info($r_info);

function print_info($r_info){
	$reviews_d = $r_info['review'];
	?>
	<div id="detail">
		<table>
			<tr>
				<td class="didyou detail_top">
					<h1><?= $r_info['Business Name']?><?php print_like($r_info["id"]); ?></h1>
					<span class="metadataAttr">Categories:</span>
					<span class="metadataValue"><?=$r_info['Category']?></span><br />
					<span class="metadataValue"><?=$r_info['Address']?></span><br />
					<span class="metadataValue"><?=$r_info['Phone number']?></span>
					<br />
					<br />
					<span id="overview">Overview</span><br />
					<span class="metadataAttr">Food: </span>
					<span class="metadataValue"><?php $reviews_d[0] > 0 ? print round($reviews_d[0], 1) : ''?></span><br />
					
					<span class="metadataAttr">Service: </span>
					<span class="metadataValue"><?php $reviews_d[1] > 0 ? print round($reviews_d[1], 1) : ''?></span><br />
					
					<span class="metadataAttr">Decor: </span>
					<span class="metadataValue"><?php $reviews_d[2] > 0 ? print round($reviews_d[2], 1) : ''?></span>
					
					<span id="latitude" style="visibility:hidden"><?=$r_info[LAT]?></span>
					<span id="longitude" style="visibility:hidden"><?=$r_info[LNG]?></span>
					<div id="map_canvas" style="width:290px; height:300px"></div>
				</td>
			</tr>
			<?php
			foreach($r_info as $entry=>$e_detail){	
				if($entry != 'Business Name' && $entry != 'Category' && $entry != 'Address'
				&& $entry != 'Phone number' && $entry != "id" && $entry != 'review'
				&& $entry != 'Latitude' && $entry != 'Longitude'){
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
		
		<?php
		// recommendations
		global $new_restaurant_name;
		global $restaurants_basic_info_json;
		
		$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);
		
		$new_restaurant_name = $r_info["id"];

		$restaurant_basic_info = $restaurants_basic_info_json[$new_restaurant_name];
		
		$new_f_restaurant = generate_favorite_restaurant($restaurant_basic_info);
					
		//this list contains user's favorite restaurants
		$favorite_restaurants_list = generate_f_list($new_f_restaurant);
		
		//the list contains relevant restaurants
		$relevant_restaurants_list = generate_relevant_restaurants_list($favorite_restaurants_list);

		//sorts the relevant restaurants list based on relevance, qualities, and price 
		usort($relevant_restaurants_list, 'cmp');

		//we only want the first MAX_RELEVANT_RESTAURANTS restaurants
		array_splice($relevant_restaurants_list, MAX_RELEVANT_RESTAURANTS);
		
		//prints the relevant restaurants list.
		print_relevant_restaurants_list($relevant_restaurants_list);
		?>
		
	</div>
	<?php
}
print_bottom();

?>