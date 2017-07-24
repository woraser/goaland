/**
 * 
 */
$(document).ready(function() {
	var map = new BMap.Map('showIotxMap');//定义地图对象
	
	map.centerAndZoom(new BMap.Point(110.404269, 35.916042), 5);//初始化地图
	map.enableScrollWheelZoom();//允许使用鼠标滚轮进行放大和缩小
	//地图样式
	var iotxStyleJson=[
                 {
                           "featureType": "water",
                           "elementType": "all",
                           "stylers": {
                                     "color": "#021019"
                           }
                 },
                 {
                           "featureType": "highway",
                           "elementType": "labels",
                           "stylers": {
                                     "color": "#000000",
                                     "visibility": "off"
                           }
                 },
                 {
                           "featureType": "highway",
                           "elementType": "all",
                           "stylers": {
                                     "color": "#147a92",
                                     "visibility": "off"
                           }
                 },
                 {
                           "featureType": "arterial",
                           "elementType": "geometry.fill",
                           "stylers": {
                                     "color": "#000000"
                           }
                 },
                 {
                           "featureType": "arterial",
                           "elementType": "geometry.stroke",
                           "stylers": {
                                     "color": "#0b3d51"
                           }
                 },
                 {
                           "featureType": "local",
                           "elementType": "geometry",
                           "stylers": {
                                     "color": "#000000"
                           }
                 },
                 {
                           "featureType": "land",
                           "elementType": "all",
                           "stylers": {
                                     "color": "#08304b"
                           }
                 },
                 {
                           "featureType": "railway",
                           "elementType": "geometry.fill",
                           "stylers": {
                                     "color": "#000000"
                           }
                 },
                 {
                           "featureType": "railway",
                           "elementType": "geometry.stroke",
                           "stylers": {
                                     "color": "#08304b"
                           }
                 },
                 {
                           "featureType": "subway",
                           "elementType": "geometry",
                           "stylers": {
                                     "lightness": -70
                           }
                 },
                 {
                           "featureType": "building",
                           "elementType": "geometry.fill",
                           "stylers": {
                                     "color": "#000000"
                           }
                 },
                 {
                           "featureType": "all",
                           "elementType": "labels.text.fill",
                           "stylers": {
                                     "color": "#857f7f"
                           }
                 },
                 {
                           "featureType": "all",
                           "elementType": "labels.text.stroke",
                           "stylers": {
                                     "color": "#000000"
                           }
                 },
                 {
                           "featureType": "building",
                           "elementType": "geometry",
                           "stylers": {
                                     "color": "#022338"
                           }
                 },
                 {
                           "featureType": "green",
                           "elementType": "geometry",
                           "stylers": {
                                     "color": "#062032"
                           }
                 },
                 {
                           "featureType": "boundary",
                           "elementType": "all",
                           "stylers": {
                                     "color": "#1e1c1c"
                           }
                 },
                 {
                           "featureType": "manmade",
                           "elementType": "all",
                           "stylers": {
                                     "color": "#022338"
                           }
                 },
                 {
                           "featureType": "label",
                           "elementType": "all",
                           "stylers": {
                                     "visibility": "off"
                           }
                 }];	
       	map.setMapStyle({styleJson: iotxStyleJson });
       	
       	loadIotx();
       	
       	//添加点方法
       	function addMarker(longitude,latitude,id){
      	  var marker = new BMap.Marker(new BMap.Point(longitude,latitude));
      	  map.addOverlay(marker);
      	  
      	  marker.addEventListener("click", function(){    
      		  window.location.href='/iotx/management/detail/'+id;
      	  });
      	}
       	
       	//加载iotx节点
       	function loadIotx(){
       		$.ajax({
				url : '/iotx/jsonArray',
				data : {
					'company.code' : ($('#isAdmin').length > 0?null:$("#companyCode").val()),
					'showAttributes' : 'id,longitude,latitude',
				},
				type : 'get',
				dataType : 'json',
				success : function(datas) {
					var longitude;
					var latitude;
					var id;
					$.each(datas,function(i,value){
						longitude = this.longitude;
						latitude = this.latitude;
						id=this.id;
						//添加点
						addMarker(longitude,latitude,id);
					})
				},
				error : function(data){
					warning('节点加载失败，请联系管理员或刷新页面重试');
				}
			});
       	}
       	
      //地图按钮点击事件
	  $("#viewTable").click(function(){
		  window.location.href='/iotx/management/table';
	  });
})