/**
 * Client of a demo web application which shows how
 * the API can be used as the server which solves fuzzy
 * prefix search query tasks.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
 
 /**
 * The map object to use.
 */
var map;
/**
 * The location marker to use.
 */
var marker;
/**
 * Whether the map object is ready or not.
 */
var isMapReady = false;
/**
 * Whether the map object is currently hidden or not.
 */
var isMapHidden = true;

/**
 * Callback method which is used by the Google Maps API
 * once the API has been loaded. It initializes the service.
 */
function initMap() {
	// Create the map object and hook it to the canvas in the DOM
	var initialPos = new google.maps.LatLng(49.23299, 6.97633);
	var mapOptions = {
		zoom: 11,
		center: initialPos,
		mapTypeId: google.maps.MapTypeId.MAP
	};
	map = new google.maps.Map(document.getElementById('mapCanvas'), mapOptions);
	
	// Create the initial location marker
	marker = new google.maps.Marker({
		map: map,
		draggable: false,
		position: initialPos
	});
	// Set the map state to ready
	isMapReady = true;
}

/**
 * Extracts and sends the query to the server application
 * which performs a fuzzy prefix search.
 */
function queryRoutine() {
	var query = $("#query").val();
    var host = window.location.host;
    var url = "http://" + host + "/?q=" + query;
	$.ajax(url, {dataType: 'jsonp'});
}

/**
 * Displays the given position on the map.
 */
function displayPosition(latitude, longitude) {
	// Only do something when the map is ready
	if (!isMapReady) {
		return;
	}
	
	// Change the marker position
	position = new google.maps.LatLng(latitude, longitude);
	marker.setPosition(position);
	
	// Display the map
	if (isMapHidden) {
		$('#mapCanvas').show();
	}
	google.maps.event.trigger(map, 'resize');
	map.panTo(position);
}

/**
 * Callback used by the server application to accept the computed
 * resulting location and displaying it.
 */
function queryServerCallback(json) {
	var firstMatch = json.matches[0];
	var latPos = firstMatch[1];
	var longPos = firstMatch[2];
	
	// TODO Change to drop-down build-call
	displayPosition(latPos, longPos);
}

// Hook the routine to an event
$(document).ready(function() {
  $('#query').keyup(function() {
	  queryRoutine();
  });
  $('#mapCanvas').hide();
});