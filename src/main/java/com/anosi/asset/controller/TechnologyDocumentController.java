package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.elasticsearch.TechnologyDocument;
import com.anosi.asset.service.TechnologyDocumentService;

@RestController
public class TechnologyDocumentController extends BaseController<TechnologyDocument> {

	private static final Logger logger = LoggerFactory.getLogger(TechnologyDocumentController.class);

	@Autowired
	private TechnologyDocumentService technologyDocumentService;

	/***
	 * 进去文档检索页面
	 */
	@RequestMapping(value = "/technologyDocument/search", method = RequestMethod.GET)
	public ModelAndView toTechnologyDocumentSearch() {
		logger.debug("technologyDocument search");
		return new ModelAndView("document/search");
	}

	/***
	 * 上传技术文档
	 * 
	 * @param multipartFiles
	 * @param type
	 *            文档的具体种类
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/technologyDocument/upload", method = RequestMethod.POST)
	public JSONObject fileUpload(@RequestParam("file_upload") MultipartFile[] multipartFiles,
			@RequestParam("type") String type) throws Exception {
		logger.info("technologyDocument upload");
		JSONObject jsonObject = new JSONObject();
		if (multipartFiles != null && multipartFiles.length > 0) {
			logger.debug("is uploading");
			technologyDocumentService.createTechnologyDocument(multipartFiles, type);
			jsonObject.put("result", "upload success");
		} else {
			jsonObject.put("result", "file is null");
		}
		logger.info(jsonObject.toString());
		return jsonObject;
	}

	/***
	 * 根据条件查找技术文档
	 * 
	 * @param showType
	 * @param technologyDocument 查询条件已经参数绑定至这个bean里
	 * @param showAttributes
	 * @param rowId
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/technologyDocument/search/{showType}", method = RequestMethod.GET)
	public JSONObject fileDownloadList(@PathVariable ShowType showType, TechnologyDocument technologyDocument,
			@RequestParam(value = "showAttributes") String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, value = 20) Pageable pageable)
			throws Exception {
		logger.info("to view file list");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		
		return parseToJson(technologyDocumentService.getHighLight(technologyDocument, pageable), rowId, showAttributes, showType);
	}

}
