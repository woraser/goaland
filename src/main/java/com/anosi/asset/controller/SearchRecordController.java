package com.anosi.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.model.elasticsearch.SearchRecord;
import com.anosi.asset.service.SearchRecordService;

@RestController
public class SearchRecordController extends BaseController<SearchRecord> {

	@Autowired
	private SearchRecordService searchRecordService;

	/***
	 * 获取autocomplete的source
	 * 
	 * @param predicate
	 * @param label
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchRecord/autocomplete", method = RequestMethod.GET)
	public JSONArray autocomplete(@RequestParam(value = "searchContent") String searchContent,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pageable)
			throws Exception {
		return jsonUtil.parseAttributesToAutocomplete("searchContent", "searchContent",
				searchRecordService.findBySearchContent(searchContent, pageable));
	}

}
