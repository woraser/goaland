package com.anosi.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.DocumentType;
import com.anosi.asset.service.DocumentTypeService;
import com.google.common.collect.ImmutableMap;

@RestController
public class DocumentTypeController extends BaseController<DocumentType> {

	@Autowired
	private DocumentTypeService documentTypeService;

	/**
	 * save文档类型
	 * 
	 * @param documentType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/documentType/save", method = RequestMethod.POST)
	public JSONObject saveType(DocumentType documentType) throws Exception {
		documentTypeService.save(documentType);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/****
	 * 在执行update前，先获取持久化的documentType对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getIox(@RequestParam(value = "documentTypeId", required = false) Long id, Model model) {
		if (id != null) {
			model.addAttribute("documentType", documentTypeService.getOne(id));
			model.addAttribute("lastTypeName", documentTypeService.getOne(id).getName());
		}
	}

	/***
	 * 更新文档类型
	 * 
	 * @param documentType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/documentType/update", method = RequestMethod.POST)
	public JSONObject updateType(@ModelAttribute("documentType") DocumentType documentType,
			@ModelAttribute("lastTypeName") String lastTypeName) throws Exception {
		documentTypeService.updateDocumentTypeName(documentType, lastTypeName);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

}
