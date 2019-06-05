// https://openlayers.org/en/latest/examples/
// https://tsauerwein.github.io/ol3/mapbox-gl-js/examples/

var iconFeature = new ol.Feature({
  geometry: new ol.geom.Point(ol.proj.transform([-1.2120773, 48.1224966], 'EPSG:4326', 'EPSG:900913')),
  name: 'Null Island',
  population: 4000,
  rainfall: 500
});

var iconStyle = new ol.style.Style({
  image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
    anchor: [0.5, 1],
    anchorXUnits: 'fraction',
    anchorYUnits: 'fraction',
    opacity: 0.75,
    src: 'https://cdnjs.cloudflare.com/ajax/libs/openlayers/2.12/img/marker.png'
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

//var rasterLayer = new ol.layer.Tile({
//        source: new ol.source.TileJSON({
//          url: 'https://api.tiles.mapbox.com/v3/mapbox.geography-class.json?secure',
//          crossOrigin: ''
//        })
//      });

var map = new ol.Map({
    // устанавливает вид на заданное место и масштаб
    view: new ol.View({
        center: ol.proj.transform([-1.2120773, 48.1224966], 'EPSG:4326', 'EPSG:900913'),
        zoom: 18
    }),

    layers: [osmLayer, vectorLayer],
    target: 'map'
});
// disable zoom
map.getInteractions().forEach(function(interaction) {
    if (interaction instanceof ol.interaction.MouseWheelZoom) {
        interaction.setActive(false);
    }
}, this);

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

//// change mouse cursor when over marker
//$(map.getViewport()).on('mousemove', function(e) {
//  var pixel = map.getEventPixel(e.originalEvent);
//  var hit = map.forEachFeatureAtPixel(pixel, function(feature, layer) {
//    return true;
//  });
//  if (hit) {
//    map.getTarget().style.cursor = 'pointer';
//  } else {
//    map.getTarget().style.cursor = '';
//  }
//});

map.on('pointermove', function(evt) {
  map.getTargetElement().style.cursor =
      map.hasFeatureAtPixel(evt.pixel) ? 'pointer' : '';

  if (evt.dragging) {
    $(element).popover('dispose');
    return;
  }
});