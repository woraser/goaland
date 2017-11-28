package com.anosi.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.anosi.asset.component.WebSocketComponent;

/***
 * 测试webSocket
 * 
 * @author jinyao
 *
 */
@Controller
public class WebSocketController {
	
	@Autowired
	private WebSocketComponent webSocketComponent;

	@MessageMapping("/welcome")
	public void welcome(String msg) throws Exception{
		webSocketComponent.sendByBroadcast("hello world,"+msg);
	}
	
	@MessageMapping("/welcomeQuene")
	public void welcomeQuene(String msg) throws Exception{
		webSocketComponent.sendByQuene("admin", "this is quene,"+msg);
	}

}
