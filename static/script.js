function like(rid) {
	new Ajax.Request("backend/is_like.php",
	  {
		parameters: {rid: rid, like: 1},
		onSuccess: function() {
			//$(rid).className += " like_dull";
			$(rid).setAttribute("src", "static/liked.png");
			$(rid).parentNode.removeAttribute('href');
			$(rid).parentNode.removeAttribute('onclick');
		}
	  }
	);
}

function unlike(rid) {
	new Ajax.Request("backend/is_like.php",
	  {
		parameters: {rid: rid, like: 0},
		onSuccess: function() {
			$(rid).remove();
		}
	  }
	);
}

function redirect() {
	window.location = "login.php";
}

function mapInitialize() {
	// map
	var lat = document.getElementById("latitude").innerHTML;
	var lng = document.getElementById("longitude").innerHTML;
	var myLatlng = new google.maps.LatLng(lat, lng);
	var myOptions = {
		center: myLatlng,
		mapTypeControl: false,
		mapTypeId: google.maps.MapTypeId.ROADMAP,
		streetViewControl: false,
		zoom: 14
	};
	var map = new google.maps.Map(document.getElementById("map_canvas"),
		myOptions);
	
	// marker
	var marker = new google.maps.Marker({
		position: myLatlng
	});
	marker.setMap(map);
	
	// info window
	var contentString = '<div id="info_windwo_content" style="width:190px;height:172px;"></div>';
	var infowindow = new google.maps.InfoWindow({
		content: contentString
	});
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map,marker);
	});
	
	// street view
	var pano = null;
	google.maps.event.addListener(infowindow, 'domready', function() {
		if (pano != null) {
			pano.unbind("position");
			pano.setVisible(false);
		}
		pano = new google.maps.StreetViewPanorama(document.getElementById("info_windwo_content"), {
			// options
			navigationControl: true,
			navigationControlOptions: {style: google.maps.NavigationControlStyle.ANDROID},
			enableCloseButton: false,
			addressControl: false,
			linksControl: false
		});
		pano.bindTo("position", marker);
		pano.setVisible(true);
	});
	google.maps.event.addListener(infowindow, 'closeclick', function() {
		pano.unbind("position");
		pano.setVisible(false);
		pano = null;
	});
}
google.maps.event.addDomListener(window, 'load', mapInitialize);