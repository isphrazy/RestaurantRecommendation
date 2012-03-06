<?php
//include 'backend/b_profile.php';
include 'pattern.php';
include 'backend/search.php';

print_head();
print_login();
print_search_bar();
?>

<h1 id="profile">My Profile:</h1>
<table>
<tr><td class='didyou'>You liked the following restaurants:</td></tr>
<?php
get_restaurants();
$favorite_restaurants_list = array();

$favorite_restaurants_weight[0]=0.3; // TEST
$favorite_restaurants_weight[1]=0.4;
$favorite_restaurants_weight[2]=0.3;
$new_restaurant_name = "";
$restaurants_basic_info_json = json_decode(file_get_contents(RESTAURANT_BASIC_DATA_FILE), true);

foreach ($restaurants as $rid => $restaurant) {
	$new_f_restaurant = generate_favorite_restaurant($restaurant);
	$favorite_restaurants_list[] = $new_f_restaurant;
	?>
	<!-- print all favorite (liked) restaurants -->
	<tr id="<?=$rid?>">
		<td>
			<a href="detail.php?name=<?=$rid?>" target="_blank">
				<b><?=$restaurant["Business Name"] . ', '?></b>
				<?=$restaurant["Address"]?>
			</a>
			<a href="javascript:void(0)" onclick="unlike('<?=$rid?>');">
				<img src="static/b_drop.png" alt="static/b_drop.png" class="like" />
			</a>
		</td>
	</tr>
	<?php
}?>
</table>

<?php
//the list contains relevant restaurants
$relevant_restaurants_list = generate_relevant_restaurants_list($favorite_restaurants_list);

//sorts the relevant restaurants list based on relevance, qualities, and price 
usort($relevant_restaurants_list, 'cmp');

$count = 0;
$filted_restaurant_list = array();
foreach ($relevant_restaurants_list as $relevant_restaurant) {
	if ( !array_key_exists($relevant_restaurant->name, $restaurants) ) {
		$filted_restaurant_list[] = $relevant_restaurant;
		$count++;
	}
	if ($count==10)
		break;
}

//prints the relevant restaurants list.
print_relevant_restaurants_list($filted_restaurant_list);
?>

<?php
print_bottom();
?>