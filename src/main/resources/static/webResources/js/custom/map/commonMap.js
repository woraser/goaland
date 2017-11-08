/**
 * 
 */
function getStyle(){
	return [{
        'featureType': 'land', //调整土地颜色
        'elementType': 'geometry',
        'stylers': {
            'color': '#FFFFFF'
        }
    }, {
        'featureType': 'building', //调整建筑物颜色
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'building', //调整建筑物标签是否可视
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'highway', //调整高速道路颜色
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'highway', //调整高速名字是否可视
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'arterial', //调整一些干道颜色
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'arterial',
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'green',
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'water',
        'elementType': 'geometry',
        'stylers': {
            'color': '#DBDBDA'
        }
    }, {
        'featureType': 'subway', //调整地铁颜色
        'elementType': 'geometry.stroke',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'subway',
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'railway',
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'railway',
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    },  {
        'featureType': 'manmade',
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'manmade',
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'local',
        'elementType': 'geometry',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'local',
        'elementType': 'labels',
        'stylers': {
            'visibility': 'off'
        }
    }, {
        'featureType': 'subway',
        'elementType': 'geometry',
        'stylers': {
            'lightness': -65
        }
    }, {
        'featureType': 'railway',
        'elementType': 'all',
        'stylers': {
            'lightness': -40
        }
    }, {
        'featureType': 'boundary',
        'elementType': 'geometry',
        'stylers': {
            'color': '#e25921',
            'weight': '0',
            'lightness': -29
        }
    },  {
        "featureType": "all",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#a6a4a3",
            "weight": "6",
            "saturation": -65
        }
    }]
}