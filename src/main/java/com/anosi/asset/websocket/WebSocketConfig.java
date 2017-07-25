package com.anosi.asset.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/***
 * webSocket配置
 * @author jinyao
 *
 */
@Configuration
@EnableWebSocketMessageBroker
//通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //endPoint 注册协议节点,并映射指定的URl
        registry.addEndpoint("/endpointWisely").withSockJS();//注册一个Stomp 协议的endpoint,并指定 SockJS协议。
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(message broker)
        registry.enableSimpleBroker("/topic"); //广播式应配置一个/topic 消息代理
        //点对点式增加一个/queue 消息代理
        registry.enableSimpleBroker("/queue","/topic");
    }
    
}
