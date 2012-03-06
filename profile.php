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
initiate_vars();
set_reviews_weigth();

if(!empty($restaurants)){//user has some favorite restaurants
	foreach ($restaurants as $rid => $restaurant) {
		
		?>
		<!-- print each favorite (liked) restaurant -->
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
	}
}else{
	
}

?>
</table>
<?php
$filted_restaurant_list = generate_users_recommendation();

?>
<br />
&nbsp;&nbsp;&nbsp;<a id="listview" href="#mapview" style="font-size:20px;font-weight:bold">Show map<img src="static/new_yellow.png"></a>
<?php
//prints the relevant restaurants list.
print_relevant_restaurants_list($filted_restaurant_list);
var_dump($geocode_arr);
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