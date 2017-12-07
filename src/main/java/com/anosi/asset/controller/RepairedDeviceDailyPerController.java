package com.anosi.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.RepairedDeviceDailyPer;
import com.anosi.asset.service.RepairedDeviceDailyPerService;

@RestController
public class RepairedDeviceDailyPerController extends BaseController<RepairedDeviceDailyPer>{
	
	@Autowired
	private RepairedDeviceDailyPerService repairedDeviceDailyPerService;

	/***
	 * 获取显示给报表的数据
	 * 
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/repairedDeviceDailyPer/report/data/", method = RequestMethod.GET)
	public JSONObject findToReport(@PageableDefault(sort = { "countDate" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable) throws Exception{
		return repairedDeviceDailyPerService.findToReport(pageable);
	}
	
}
