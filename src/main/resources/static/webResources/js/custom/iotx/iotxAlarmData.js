/**
 * 
 */
$(document).ready(function(){
	var size = 4
	
	var alarmDatas = new Vue({
	   el: '#alarmDatas',
	   data: {
		   alarms : [],
	   },
	   methods:{
		   
	   },
	   filters: {
		  yearFormat: function (value) {
			  return value.split(" ")[0];
		  },
		  timeFormat: function (value) {
			  return value.split(" ")[1];
		  }
	   }
	})
	
	// websocket
	var stompClient = null;
	
	var socket = new SockJS('/endpointWisely'); //链接SockJS 的endpoint 名称为"/endpointWisely"
	stompClient = Stomp.over(socket);//使用stomp子协议的WebSocket 客户端
	stompClient.connect('guest', 'guest', function(frame) {//链接Web Socket的服务端。
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/broadcast/alarmData', function(respnose){ //订阅/user/queue/private/message目标发送的消息。
			var responseMessage = JSON.parse(respnose.body).responseMessage
			console.info(responseMessage)
			if(responseMessage=='alarm'){
				getAlarmData();//重新加载数据
			}
	    });
	});
    
    // 从服务器获取最近的告警信息
    function getAlarmData(){
    	$.ajax({
			url : '/alarmData/management/data/REMOTE',
			data : {
				'alarm' : true,
				'page' : 0,
				'size' : size,
				'sort' : 'collectTime,desc',
				'showAttributes' : 'collectTime,sensor.dust.iotx.serialNo,level',
			},
			type : 'get',
			dataType : 'json',
			success : function(datas) {
				alarmDatas.alarms = datas.content
				$.each(alarmDatas.alarms,function(){
					this['level'] = $.i18n.prop('iotxData.' + this['level']) 
				})
			},
			error : function(data){
				warning('节点分布加载失败，请联系管理员或刷新页面重试');
			}
		});
    }
	
	getAlarmData();
    
})