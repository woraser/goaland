package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.anosi.asset.model.jpa.BaseEntity;

@RestController
public class ReportController extends BaseController<BaseEntity> {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	/***
	 * 进入查看<b>报表</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/report/management/view", method = RequestMethod.GET)
	public ModelAndView toViewReport() {
		logger.debug("view report");
		return new ModelAndView("report/report");
	}

	/***
	 * 查看统计图的详情
	 * 
	 * @param detail
	 * @return
	 */
	@RequestMapping(value = "/report/management/{detail}/view", method = RequestMethod.GET)
	public ModelAndView toViewReportDetail(@PathVariable String detail) {
		logger.debug("view report detail");
		return new ModelAndView("report/" + detail);
	}

}
