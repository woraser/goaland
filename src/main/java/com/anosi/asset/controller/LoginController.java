package com.anosi.asset.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.component.LoginComponent;
import com.anosi.asset.component.SessionComponent;
import com.anosi.asset.component.WebSocketComponent;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.util.CodeUtil;
import com.google.common.collect.ImmutableMap;

@RestController
public class LoginController extends BaseController<Account> {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginComponent loginComponent;
	@Autowired
	private WebSocketComponent webSocketComponent;
	@Value("${local.domainName}")
	private String domainName;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginForm() {
		return new ModelAndView("login", "uuid", UUID.randomUUID().toString());
	}

	/***
	 * 登录提交地址和shiro配置文件中配置的loginurl一致
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, Account account, RedirectAttributes redirectAttributes,
			boolean rememberMe) throws Exception {
		logger.debug("...login...");

		String result = loginComponent.login(account, rememberMe);
		if (result == "success") {
			return new ModelAndView("redirect:/index");
		} else {
			redirectAttributes.addFlashAttribute("message", result);
			return new ModelAndView("redirect:/login");
		}
	}

	/***
	 * 移动端登录
	 * 
	 * @param request
	 * @param account
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/remote", method = RequestMethod.POST)
	public JSONObject loginRemote(HttpServletRequest request, Account account) throws Exception {
		logger.debug("...remote login...");

		JSONObject jsonObject = new JSONObject();
		String result = loginComponent.login(account, false);
		if (result == "success") {
			jsonObject.put("result", "success");
		} else {
			jsonObject.put("result", "error");
			jsonObject.put("message", result);
		}
		return jsonObject;
	}

	/***
	 * 获取登录二维码
	 * 
	 * @param message
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/QRCode/{uuid}", method = RequestMethod.GET)
	public void fileDownload(@PathVariable String uuid, HttpServletResponse response) throws Exception {
		BufferedImage image = CodeUtil.getRQ(domainName + "/login/remote/" + uuid, 200);
		// image转inputStream
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "gif", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());

		response.setHeader("Content-disposition",
				"attachment;filename=" + new String(UUID.randomUUID().toString().getBytes("gbk"), "iso-8859-1"));
		response.setContentType("application/force-download;charset=utf-8");
		// 获取文件流
		try (BufferedInputStream bis = new BufferedInputStream(is);
				OutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
			IOUtils.copy(bis, bos);
		}

	}

	/***
	 * 发送给浏览器，让js发送登录的请求
	 * 
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/remote/QRCode/{uuid}", method = RequestMethod.POST)
	public void sendLoginWithQRCode(@PathVariable String uuid) throws Exception {
		logger.debug("...QRCode login...");

		Session session = SessionComponent.getSession();
		Object loginId = session.getAttribute("loginId");
		if (loginId != null) {
			// 向订阅这个topic的页面发送sessionId
			webSocketComponent.sendByBroadcast("/topic/login/" + uuid,
					new JSONObject(
							ImmutableMap.of("sessionId", session.getId().toString(), "loginId", loginId.toString()))
									.toString());
		}
	}

	/***
	 * 扫描二维码登录
	 * 
	 * @param loginId
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login/remote/QRCode", method = RequestMethod.POST)
	public ModelAndView loginWithQRCode(@RequestParam(value = "loginId") String loginId,
			@RequestParam(value = "sessionId") String sessionId) throws Exception {
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(sessionId, loginId, false);
		// 登录验证
		currentUser.login(token);
		loginComponent.setSession(loginId);
		return new ModelAndView("redirect:/index");
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("dashboard");
		return mv;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前的Subject
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return new ModelAndView("redirect:/login");
	}

	/***
	 * rememberMe 以后的默认路径
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("redirect:/index");
	}

}
