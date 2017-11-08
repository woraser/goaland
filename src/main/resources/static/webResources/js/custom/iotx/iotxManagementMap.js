/**
 * 
 */
$(document).ready(
		function() {
			var map = document.getElementById("showIotxMap");
			var myChart = echarts.init(map);

			myChart.showLoading();
			
			var pointData=[];

			var option = {
				bmap : {
					center : [ 110.404269, 35.916042 ],
					zoom : 5,
					roam : true,
					mapStyle : {
						styleJson : getStyle()
					}
				},
				legend : {
					data : [ 'iotx分布' ],
					textStyle : {
						color : '#fff'
					},

				},
				series : [ {
					//name : 'iotx分布',
					type : 'effectScatter',
					mapType : 'china',
					coordinateSystem : 'bmap',
					zlevel : 2, /* 圆点大小 */
					data : pointData,
					rippleEffect : {
						period : 6,
						scale : 3,
						brushType : 'fill'
					},
				} ]
			}
			myChart.hideLoading();
			myChart.setOption(option);

			var bmap = myChart.getModel().getComponent('bmap').getBMap(); // 百度地图实例
			bmap.addControl(new BMap.NavigationControl()); // 缩放控件
			bmap.addControl(new BMap.ScaleControl()); // 比例尺

			loadIotx();

			// 添加点方法
			function addMarker(longitude, latitude, id) {
				pointData.push({'name':id,'value':[longitude,latitude]})
			}
			
			// 加载iotx节点
			function loadIotx() {
				$.ajax({
					url : '/iotx/management/data/REMOTE',
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
				        	window.location.href = '/iotx/management/detail/' + param.name+"/view";
				        });        
					},
					error : function(data) {
						warning('节点加载失败，请联系管理员或刷新页面重试');
					}
				});
			}

		})