package com.anosi.asset.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketComponent {

	@Autowired
	private SimpMessagingTemplate template;

	// 默认的广播地址
	private String broadcast = "/topic/broadcast";

	// 默认的点对点发送地址
	private String quene = "/queue/private";

	/***
	 * 广播式推送，使用默认广播地址
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void sendByBroadcast(String message) throws Exception {
		template.convertAndSend(broadcast, new WebSocketResponse(message));
	}

	/***
	 * 广播式推动，使用自定义广播地址
	 * 
	 * @param destination
	 * @param message
	 * @throws Exception
	 */
	public void sendByBroadcast(String destination, String message) throws Exception {
		template.convertAndSend(destination, new WebSocketResponse(message));
	}

	/***
	 * 发送给特定用户,使用默认点对点发送地址
	 * 
	 * @param loginId
	 * @param message
	 * @throws Exception
	 */
	public void sendByQuene(String loginId, String message) throws Exception {
		template.convertAndSendToUser(loginId, quene, new WebSocketResponse(message));
	}

	/***
	 * 发送给特定用户
	 * 
	 * @param loginId
	 * @param message
	 * @param destination
	 *            目的地路径
	 * @throws Exception
	 */
	public void sendByQuene(String loginId, String destination, String message) throws Exception {
		template.convertAndSendToUser(loginId, destination, new WebSocketResponse(message));
	}

	/***
	 * 发送给一组特定用户，使用默认点对点发送地址
	 * 
	 * @param loginIds
	 * @param message
	 * @throws Exception
	 */
	public void sendByQuene(List<String> loginIds, String message) throws Exception {
		loginIds.forEach(loginId -> template.convertAndSendToUser(loginId, quene, new WebSocketResponse(message)));
	}

	/***
	 * 发送给一组特定用户
	 * 
	 * @param loginIds
	 * @param message
	 * @param destination
	 *            目的地路径
	 * @throws Exception
	 */
	public void sendByQuene(List<String> loginIds, String destination, String message) throws Exception {
		loginIds.forEach(
				loginId -> template.convertAndSendToUser(loginId, destination, new WebSocketResponse(message)));
	}

	/***
	 * 内部类,向浏览器返回的时候使用此类
	 * @author jinyao
	 *
	 */
	class WebSocketResponse {

		public WebSocketResponse(String responseMessage) {
			this.responseMessage = responseMessage;
		}

		private String responseMessage;

		public String getResponseMessage() {
			return responseMessage;
		}

		public void setResponseMessage(String responseMessage) {
			this.responseMessage = responseMessage;
		}
	}

}
