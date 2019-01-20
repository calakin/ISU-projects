var customLabel = {
    1: {
        label: 'F'
    },
    0: {
        label: 'E'
    }
}

function getData(map, infoWindow) {

    //get the search options from the document
    var ownerOption = document.getElementById('owner').value;
    var fullnessOption = document.getElementById('fullness').value;
    if (!ownerOption || ownerOption == "") {
        console.log("No owner selected");
        ownerOption = 'All';
    }
    if (!fullnessOption || ownerOption == "") {
        console.log("No fullness level selected");
        fullnessOption = 'Both';
    }

    var request = window.ActiveXObject ?
        new ActiveXObject('Microsoft.XMLHTTP') :
        new XMLHttpRequest;

    // receiving response from getMapLocations.php
    request.onreadystatechange = function () {

        if (this.readyState == 4 && this.status == 200) {

            var xml = request.responseXML;
            console.log(xml);

            //turn the xml into map markers
            var markers = xml.documentElement.getElementsByTagName('marker');
            Array.prototype.forEach.call(markers, function (markerElem) {
                var UnitId = markerElem.getAttribute('UnitId');
                var OwnerId = markerElem.getAttribute('OwnerId');
                var Address = markerElem.getAttribute('Address');
                var Latitude = markerElem.getAttribute('Latitude');
                var Longitude = markerElem.getAttribute('Longitude');
                var Fullness = markerElem.getAttribute('Fullness');

                var point = new google.maps.LatLng(
                    parseFloat(markerElem.getAttribute('Latitude')),
                    parseFloat(markerElem.getAttribute('Longitude'))
                );

                //add a new div for marker info, which shows upon a click
                var infowincontent = document.createElement('div');
                
                //add the html elements to the div
                var strong = document.createElement('strong');
                strong.textContent = "UnitID: " + UnitId;
                var address = document.createElement('address');
                address.textContent = "Address: " + Address;
                var owner = document.createElement('owner');
                owner.textContent = "Owner: " + OwnerId;
                var lat = document.createElement('lat');
                lat.textContent = "Latitude: " + Latitude;
                var lng = document.createElement('lng');
                lng.textContent = "Longitude: " + Longitude;
                var fullness = document.createElement('fullness');
                fullness.textContent = "Full (E/F): " + customLabel[Fullness].label;

                infowincontent.appendChild(strong);
                infowincontent.appendChild(address);
                infowincontent.appendChild(owner);
                infowincontent.appendChild(document.createElement('br'));
                infowincontent.appendChild(lat);
                infowincontent.appendChild(document.createElement('br'));
                infowincontent.appendChild(lng);
                infowincontent.appendChild(document.createElement('br'));
                infowincontent.appendChild(fullness);
                
                var icon = customLabel[Fullness];

                //add the marker to the map
                var marker = new google.maps.Marker({
                    map: map,
                    position: point,
                    label: icon.label
                });

                //add the info window to the marker
                marker.addListener('click', function () {
                    infoWindow.setContent(infowincontent);
                    infoWindow.open(map, marker);
                });
            });
		//getOwnerList();
        }
    }

    var url = "getMapLocations.php?ownerOption=" + ownerOption + "&" + "fullnessOption=" + fullnessOption;
    request.open("GET", url , true);
    request.send();
}

function initMap() {

    var center = { lat: 39.8283, lng: -98.5795 };

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 4,
        center: center
    });

    var infoWindow = new google.maps.InfoWindow;

    getData(map, infoWindow);
}

var maps_api  = document.createElement('script');
maps_api.type = 'text/javascript';
maps_api.src  = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyAOrf-sZxhUN6Te4D2SnbGq2eVNjbhTjKA&callback=initMap';
document.getElementsByTagName('head')[0].appendChild(maps_api);
