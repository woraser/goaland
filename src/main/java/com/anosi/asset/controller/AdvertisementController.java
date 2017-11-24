package com.anosi.asset.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Advertisement;
import com.anosi.asset.service.AdvertisementService;
import com.anosi.asset.util.StringUtil;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class AdvertisementController extends BaseController<Advertisement> {

	private static final Logger logger = LoggerFactory.getLogger(AdvertisementController.class);

	@Autowired
	private AdvertisementService advertisementService;

	/***
	 * 进入查看<b>所有广告信息</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/advertisement/management/view", method = RequestMethod.GET)
	public ModelAndView toViewAdvertisementManage() {
		logger.debug("view advertisement manage");
		return new ModelAndView("advertisement/manage");
	}
	
	/***
	 * 根据条件查询某个广告
	 * 
	 * @param showType
	 * @param predicate
	 * @param showAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/advertisement/management/data/one", method = RequestMethod.GET)
	public JSONObject findDeviceManageDataOne(@QuerydslPredicate(root = Advertisement.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes) throws Exception {
		logger.info("find advertisement one");
		return jsonUtil.parseAttributesToJson(StringUtil.splitAttributes(showAttributes),
				advertisementService.findOne(predicate));
	}

	/***
	 * 获取广告数据
	 * 
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/advertisement/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findAdvertisementManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Advertisement.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@RequestParam(value = "searchContent", required = false) String searchContent) throws Exception {
		logger.info("find advertisement");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		Page<Advertisement> advertisements;
		if (StringUtils.isNoneBlank(searchContent)) {
			advertisements = advertisementService.findByContentSearch(searchContent, pageable);
		} else {
			advertisements = advertisementService.findAll(predicate, pageable);
		}

		return parseToJson(advertisements, rowId, showAttributes, showType);
	}

	/***
	 * 进入save/update advertisement的页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/advertisement/save", method = RequestMethod.GET)
	public ModelAndView toSaveAdvertisementPage(@RequestParam(value = "id", required = false) Long id)
			throws Exception {
		Advertisement advertisement;
		if (id == null) {
			advertisement = new Advertisement();
		} else {
			advertisement = advertisementService.getOne(id);
		}
		return new ModelAndView("advertisement/save").addObject("advertisement", advertisement);
	}

	/****
	 * 在执行update前，先获取持久化的advertisement对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getAdvertisement(@RequestParam(value = "advertisementId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("advertisement", advertisementService.getOne(id));
		}
	}

	/***
	 * save/update advertisement
	 * 
	 * @param advertisement
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/advertisement/save", method = RequestMethod.POST)
	public JSONObject saveadvertisement(@ModelAttribute("advertisement") Advertisement advertisement) throws Exception {
		logger.debug("saveOrUpdate advertisement");
		if (advertisement.getId() == null) {
			advertisement.setCreater(sessionComponent.getCurrentUser());
		}
		advertisementService.save(advertisement);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 删除advertisement
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/advertisement/delete", method = RequestMethod.POST)
	public JSONObject deleteAdvertisement(@RequestParam(value = "id") Long id) throws Exception {
		logger.debug("delete advertisement");
		advertisementService.delete(id);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 进入填写广告内容的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/advertisement/content/edit", method = RequestMethod.GET)
	public ModelAndView toEditAdvertisementContent(@RequestParam(value = "id") Long id) {
		logger.debug("edit advertisement content");
		return new ModelAndView("advertisement/edit").addObject("id", id);
	}

}
