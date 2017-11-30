package com.anosi.asset.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Materiel;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.service.MaterielService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;

@RestController
public class MaterielController extends BaseController<Materiel> {

	private static final Logger logger = LoggerFactory.getLogger(MaterielController.class);

	@Autowired
	private MaterielService materielService;
	@Autowired
	private DeviceService deviceService;

	/***
	 * 获取物料数据
	 * 
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/materiel/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findMaterielManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = Materiel.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@RequestParam(value = "searchContent", required = false) String searchContent,
			@RequestParam(value = "deviceSN", required = false) String deviceSN,
			@RequestParam(value = "beginTime", required = false) Date beginTime,
			@RequestParam(value = "endTime", required = false) Date endTime) throws Exception {
		logger.info("find materiel");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		Page<Materiel> materiels;
		if (StringUtils.isNoneBlank(searchContent) && StringUtils.isNoneBlank(deviceSN)) {
			materiels = materielService.findBySearchContent(searchContent, deviceSN, pageable);
		} else {
			materiels = materielService.findAll(predicate, pageable);
		}

		return parseToJson(materiels, rowId, showAttributes, showType);
	}

	/***
	 * 进入save/update device的页面
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/materiel/save", method = RequestMethod.GET)
	public ModelAndView toSaveMaterielPage(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "deviceId") Long deviceId) throws Exception {
		Materiel materiel;
		if (id == null) {
			materiel = new Materiel();
		} else {
			materiel = materielService.getOne(id);
		}
		return new ModelAndView("materiel/save").addObject("device", deviceService.getOne(deviceId))
				.addObject("materiel", materiel);
	}

	/****
	 * 在执行update前，先获取持久化的materiel对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getMateriel(@RequestParam(value = "materielId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("materiel", materielService.getOne(id));
		}
	}

	/***
	 * save/update materiel
	 * 
	 * @param device
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/materiel/save", method = RequestMethod.POST)
	@Transactional
	public JSONObject saveMateriel(@ModelAttribute("materiel") Materiel materiel) throws Exception {
		logger.debug("saveOrUpdate materiel");
		materiel.needRemind();
		materielService.save(materiel);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}
	
	/***
	 * 删除物料
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/materiel/delete", method = RequestMethod.POST)
	public JSONObject deleteMateriel(@RequestParam(value = "id") Long id) throws Exception {
		logger.debug("delete materiel");
		materielService.delete(id);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}
	
	/***
	 * 进入批量导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/materiel/save/batch/view", method = RequestMethod.GET)
	public ModelAndView deviceDocumentUpload() {
		logger.debug("materiel batch save view");
		return new ModelAndView("materiel/upload");
	}
	
	/***
	 * bat save materiel
	 * 
	 * @param device
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/materiel/save/batch", method = RequestMethod.POST)
	@Transactional
	public JSONObject batSaveMateriel(@RequestParam(value = "excel") MultipartFile excel) throws Exception {
		logger.debug("bat save materiel");
		materielService.batchSave(excel.getInputStream());
		return new JSONObject(ImmutableMap.of("result", "success"));
	}
	
	/**
	 * 按照materiel某些属性判断是否存在
	 * 
	 * @param predicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/materiel/checkExist", method = RequestMethod.GET)
	public JSONObject checkExist(@QuerydslPredicate(root = Materiel.class) Predicate predicate) throws Exception {
		return new JSONObject(ImmutableMap.of("result", materielService.exists(predicate)));
	}

}
