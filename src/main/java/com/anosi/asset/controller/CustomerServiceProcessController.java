package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.util.JqgridUtil;

@RestController
public class CustomerServiceProcessController extends BaseController<CustomerServiceProcess>{

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceProcessController.class);
	
	@Autowired
	private CustomerServcieProcessService customerServcieProcessService;
	@Autowired
	private JqgridUtil<CustomerServiceProcess> jqgridUtil;
	
	/**
	 * 查询运行中的任务
	 * @param pageable	分页
	 * @param showAttributes	展示的列
	 * @param rowId	jqgrid每行的id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerServiceProcess/runtimeTask/data", method = RequestMethod.GET)
	public JSONObject findRuntimeTaskDatas(
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes, @RequestParam(value = "rowId") String rowId)
					throws Exception {
		logger.info("find customerServiceProcess runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);
		
		return jqgridUtil.parsePageToJqgridJson(customerServcieProcessService.findRuntimeTasks(pageable), rowId,
				showAttributes.split(","));
	}
	
}
