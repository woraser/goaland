/**
 * 
 */
$(document).ready(function(){
	var pointData=[];
	
	var dom = document.getElementById("allmap");
    var myChart = echarts.init(dom);
    var planePath = 'arrow';
    myChart.showLoading();
    var option = {
            bmap: {
                center: [82.23 ,23.08],
                zoom: 1,
                roam: true,
                mapStyle: {
                    styleJson: style,
                }
            },
            legend: {
                data:['设备分布'],
                textStyle: {
                    color: '#A8A8A8',
                },

            },
            series: [{
                name:'设备分布',
                type: 'effectScatter',
                mapType: 'china',
                coordinateSystem: 'bmap',
                zlevel: 4, /*圆点大小 */
                data: pointData,
                rippleEffect: {
                    period: 10,
                    scale: 5,
                    brushType: 'fill'
                },
            }]
        };
    myChart.hideLoading();
    myChart.setOption(option);
    
    // 添加点方法
	function addMarker(longitude, latitude, id) {
		pointData.push({'name':id,'value':[longitude,latitude]})
	}
    
    // 加载device节点
	function loadDevice() {
		$.ajax({
			url : '/device/management/data/REMOTE',
			data : {
				'showAttributes' : 'id,baiduLongitude,baiduLatitude',
			},
			type : 'get',
			dataType : 'json',
			success : function(datas) {
				var longitude;
				var latitude;
				var id;
				$.each(datas.content, function(i, value) {
					longitude = this.baiduLongitude;
					latitude = this.baiduLatitude;
					id = this.id;
					// 添加点
					addMarker(longitude, latitude, id)
				});
				myChart.setOption(option);
				// 在这里做一个点击事件的监听
		        myChart.on('click', function(param){
		        	window.location.href = '/device/management/detail/' + param.name+"/view";
		        });       
			},
			error : function(data) {
				warning('节点加载失败，请联系管理员或刷新页面重试');
			}
		});
	}
	
	loadDevice();
    
    var style=[{
        'featureType': 'land', //调整土地颜色
        'elementType': 'geometry',
        'stylers': {
            'color': '#FFFFFF'
        }
    }, {
        'featureType': 'building', //调整建筑物颜色
        'elementType': 'geometry',
        'stylers': {
            'color': '#04406F'
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
            'color': '#015B99'
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
            'color': '#003051'
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
            'color': '#003051'
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
    }
    ]
})