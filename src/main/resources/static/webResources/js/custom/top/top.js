/**
 * 
 */
$(document).ready(function() {
	var topLine = new Vue({
	   el: '#top',
	   data: {
		   messages : {},
	   },
	   methods: {
		   viewMessageInfo : function(id){
			   createModalPageToView("消息","/messageInfo/"+id+"/view")
		   }
	   },
	   filters: {
		   
	   },
	})
	
	// websocket
	var stompClient = null;
	
	var socket = new SockJS('/endpointWisely'); //链接SockJS 的endpoint 名称为"/endpointWisely"
	stompClient = Stomp.over(socket);//使用stomp子协议的WebSocket 客户端
	stompClient.connect({}, function(frame) {//链接Web Socket的服务端。
		console.log('Connected: ' + frame);
		stompClient.subscribe('/user/queue/private/message/', function(respnose){ //订阅/user/queue/private/message目标发送的消息。
			console.info(JSON.parse(respnose.body).responseMessage)
			$.toaster({ priority : 'info', title : $.i18n.prop('message.new'), message : JSON.parse(respnose.body).responseMessage});
			getMessage();
	    });
	});
	
	var getMessage = function(){
		$.ajax({
			url : "/messageInfo/management/data/REMOTE",
			data : {'showAttributes':'id,from.name,title,content,sendTime','to.loginId':$("#loginId").val(),'messageStatus':'UNREAD'},
			type : 'get',
			dataType : 'json',
			success : function( data ) {
				topLine.messages = data
			}
		})
	}
	
	getMessage();
	
})