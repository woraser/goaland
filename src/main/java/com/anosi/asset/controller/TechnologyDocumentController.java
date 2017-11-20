package com.anosi.asset.controller;

import org.apache.commons.lang3.StringUtils;
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
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.DocumentTypeService;
import com.anosi.asset.service.TechnologyDocumentService;

@RestController
public class TechnologyDocumentController extends BaseController<TechnologyDocument> {

	private static final Logger logger = LoggerFactory.getLogger(TechnologyDocumentController.class);

	@Autowired
	private TechnologyDocumentService technologyDocumentService;
	@Autowired
	private DocumentTypeService documentTypeService;
	@Autowired
	private AccountService accountService;

	/***
	 * 进去文档检索页面
	 */
	@RequestMapping(value = "/technologyDocument/manage/view", method = RequestMethod.GET)
	public ModelAndView toTechnologyDocumentManage() {
		logger.debug("technologyDocument manage");
		return new ModelAndView("document/documentManage").addObject("types", documentTypeService.findAll())
				.addObject("uploaders", accountService.findByIsUploadDocument(true));
	}

	/***
	 * 进去文档上传页面
	 */
	@RequestMapping(value = "/technologyDocument/upload/view", method = RequestMethod.GET)
	public ModelAndView technologyDocumentUpload() {
		logger.debug("technologyDocument upload");
		return new ModelAndView("document/upload").addObject("types", documentTypeService.findAll());
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
	public JSONObject fileUpload(@RequestParam("fileUpLoad") MultipartFile[] multipartFiles,
			@RequestParam("type") String type,
			@RequestParam(value = "identification", required = false) String identification) throws Exception {
		logger.info("technologyDocument upload");

		if (StringUtils.isBlank(identification)) {
			identification = type;
		}
		
		JSONObject jsonObject = new JSONObject();
		if (multipartFiles != null && multipartFiles.length > 0) {
			logger.debug("is uploading");
			technologyDocumentService.createTechnologyDocument(multipartFiles, type, identification);
			jsonObject.put("result", "success");
		} else {
			jsonObject.put("result", "error");
			jsonObject.put("message", "file is null");
		}
		logger.info(jsonObject.toString());
		return jsonObject;
	}

	/***
	 * 根据条件查找技术文档
	 * 
	 * @param showType
	 * @param technologyDocument
	 *            查询条件已经参数绑定至这个bean里
	 * @param showAttributes
	 * @param rowId
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/technologyDocument/search/{showType}", method = RequestMethod.GET)
	public JSONObject fileDownloadList(@PathVariable ShowType showType, TechnologyDocument technologyDocument,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@PageableDefault(sort = {
					"uploadTime" }, direction = Sort.Direction.DESC, page = 0, value = 20) Pageable pageable)
			throws Exception {
		logger.info("to view file list");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

		return parseToJson(technologyDocumentService.getHighLight(technologyDocument, pageable), rowId, showAttributes,
				showType);
	}

}
