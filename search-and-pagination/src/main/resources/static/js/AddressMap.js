// https://openlayers.org/en/latest/examples/
// https://tsauerwein.github.io/ol3/mapbox-gl-js/examples/
// view-source:http://viglino.github.io/ol-ext/examples/style/map.style.font.html

var existingMapId = null;

//Longitude	E W '-1.2120773',
//Latitude	N S '48.1224966'


class AddressMap {

    constructor (mapId, desc, longitude, latitude) {
        window.event.preventDefault();
        if (existingMapId !== mapId) {

            rebuildHtmlBlock(mapId);

            var layers = createMapLayers(latitude, longitude, desc);

            var map = createMap(mapId, latitude, longitude, layers);

            createPopUp(map);

            existingMapId = mapId;
        }
    }
}

function rebuildHtmlBlock(mapId){
    removeMap(existingMapId)

    var newMapDiv = document.createElement("div");
    newMapDiv.innerHTML = "<div id='" + mapId + "' style='height:250px;'>" +
        "<a href='#' id='mapClose' class='btn btn-default btn-close' onclick=\"removeMap('" + mapId + "')\" style='position: absolute; right: 20px; z-index: 1000;'>&times;</a>" +
        "<div id='popup'/>" +
        "<div/>";

    var mapBlockDiv = document.getElementById("mapBlock");
    mapBlockDiv.appendChild(newMapDiv.firstChild);
}


function createMapLayers(latitude, longitude, desc) {
    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.transform([latitude, longitude], 'EPSG:4326', 'EPSG:900913')),
        name: desc,
        population: 4000,
        rainfall: 500
    });

    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 1],
            anchorXUnits: 'fraction',
            anchorYUnits: 'fraction',
            opacity: 0.75,
            // src: 'https://cdnjs.cloudflare.com/ajax/libs/openlayers/2.12/img/marker.png'
            src: '../static/img/fa_map_marker_red.png'
        }))
    });

    iconFeature.setStyle(iconStyle);

    var vectorSource = new ol.source.Vector({
        features: [iconFeature]
    });

    var vectorLayer = new ol.layer.Vector({
        source: vectorSource
    });

    var osmLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
    })

    return [osmLayer, vectorLayer]
}

function createMap(mapId, latitude, longitude, layers) {
    var map = new ol.Map({
        // устанавливает вид на заданное место и масштаб
        view: new ol.View({
            center: ol.proj.transform([latitude, longitude], 'EPSG:4326', 'EPSG:900913'),
            zoom: 18
        }),

        layers: layers,
        target: mapId
    });
    // disable zoom
    map.getInteractions().forEach(function(interaction) {
        if (interaction instanceof ol.interaction.MouseWheelZoom) {
            interaction.setActive(false);
        }
    }, this);

    return map;
}

function createPopUp(map) {
    var element = document.getElementById('popup');

    var popup = new ol.Overlay({
        element: element,
        positioning: 'bottom-center',
        stopEvent: false,
        offset: [0, -20]
    });
    map.addOverlay(popup);

    // display popup on click
    map.on('click', function(evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel,
            function(feature, layer) {
                return feature;
            });
        if (feature) {
            var geometry = feature.getGeometry();
            var coord = geometry.getCoordinates();
            popup.setPosition(coord);
            $(element).popover({
                'placement': 'top',
                'html': true,
                'content': feature.get('name')
            });
            $(element).popover('show');
        } else {
            $(element).popover('dispose');
        }
    });

    map.on('pointermove', function(evt) {
        map.getTargetElement().style.cursor =
            map.hasFeatureAtPixel(evt.pixel) ? 'pointer' : '';

        if (evt.dragging) {
            $(element).popover('dispose');
            return;
        }
    });


}

function removeMap(mapId) {
    var divId = "#" + mapId;
    var first = $(divId).firstElementChild;
    while (first) {
        first.remove();
        first = e.firstElementChild;
    }
    $(divId).remove();
    existingMapId = null
}
