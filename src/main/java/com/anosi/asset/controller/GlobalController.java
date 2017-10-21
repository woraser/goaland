package com.anosi.asset.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@RestController
public class GlobalController<T> {

	private static final Logger logger = LoggerFactory.getLogger(GlobalController.class);

	/***
	 * 全局异常处理
	 * 
	 * @param ex
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(HttpServletRequest request, Exception ex) throws IOException {
		ex.printStackTrace();

		String servletPath = request.getServletPath();

		if (ex instanceof UnauthenticatedException) {
			// 没有进行认证
			ModelAndView mv = new ModelAndView("redirect:/login");
			return mv;
		}

		// 如果是跳转页面报的错，那么跳转到error页面
		if (servletPath.endsWith("view")) {
			ModelAndView mv = new ModelAndView("error");
			StringBuilder sb = new StringBuilder();
			StackTraceElement[] stackTrace = ex.getStackTrace();
			for (StackTraceElement stackTraceElement : stackTrace) {
				sb.append(stackTraceElement + "\n");
			}
			logger.debug(sb.toString());
			return mv;
		} else {
			ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
			String message = ex.getMessage();
			mv.addObject("result", "error");
			mv.addObject("message", message);
			return mv;
		}
	}

}
