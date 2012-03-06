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
$geocode_arr = array();
foreach ($relevant_restaurants_list as $relevant_restaurant) {
	if ( !array_key_exists($relevant_restaurant->name, $restaurants) ) {
		$filted_restaurant_list[] = $relevant_restaurant;
		$geocode_arr[$relevant_restaurant->name] =
			array($relevant_restaurant->business_name,$relevant_restaurant->lat,$relevant_restaurant->lon);
		$count++;
	}
	if ($count==10)
		break;
}
?>
<br />
&nbsp;&nbsp;&nbsp;<a id="listview" href="#mapview" style="font-size:20px;font-weight:bold">Show map<img src="static/new_yellow.png"></a>
<?php
//prints the relevant restaurants list.
print_relevant_restaurants_list($filted_restaurant_list);
?>

<br />
&nbsp;&nbsp;&nbsp;<a id="mapview" href="#listview" style="font-size:20px;font-weight:bold">Show recommendation list</a>
<span id="geocode" style="display:none"><?=json_encode($geocode_arr)?></span>
<table style="padding-top:0">
	<tr style="background-color:white"><td>
	<div id="map" style="width: 100%; height: 600px;margin: 25px auto 0;display:inline-block"></div>
	</td></tr>
</table>

<script type="text/javascript">
var str = document.getElementById("geocode").innerHTML;
var obj = JSON.parse(str);

var locations = new Array( Object.keys(obj).length );
var j = 0;
for(var key in obj){
	locations[j] = new Array('<a href="detail.php?name='+key+'" target="_blank">'+obj[key][0]+'</a>', obj[key][1], obj[key][2])
	j++;
}

var map = new google.maps.Map(document.getElementById('map'), {
  zoom: 10,
  center: new google.maps.LatLng(47.607205, -122.30916), // Seattle
  mapTypeId: google.maps.MapTypeId.ROADMAP
});

var infowindow = new google.maps.InfoWindow();
var marker, i;
for (i = 0; i < locations.length; i++) {
  marker = new google.maps.Marker({
	position: new google.maps.LatLng(locations[i][1], locations[i][2]),
	map: map
  });

  google.maps.event.addListener(marker, 'click', (function(marker, i) {
	return function() {
	  infowindow.setContent(locations[i][0]);
	  infowindow.open(map, marker);
	}
  })(marker, i));
}
</script>

<?php
print_bottom();
?>