/**
 * 
 */
$(document).ready(function() {
	var login = new Vue({
	  el: '#login',
	  data: {
		  isQR : false,
	  },
	  methods : {
		  
	  },
	})
	
	// websocket
	var stompClient = null;
	
	var socket = new SockJS('/endpointWisely'); //链接SockJS 的endpoint 名称为"/endpointWisely"
	stompClient = Stomp.over(socket);//使用stomp子协议的WebSocket 客户端
	stompClient.connect('guest', 'guest', function(frame) {//链接Web Socket的服务端。
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/login/' + $("#uuid").val(), function(respnose){ //订阅/user/queue/private/message目标发送的消息。
			var responseMessage = JSON.parse(respnose.body).responseMessage
			console.info(responseMessage)
			responseMessage = JSON.parse(responseMessage)
			
			// 登录
			customPost('/login/remote/QRCode',responseMessage)
	    });
	});
	
	App.setPage("login");  //Set current page
	App.init(); //Initialise plugins and elements
	
})