package com.anosi.asset.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Project;
import com.anosi.asset.service.ProjectService;
import com.google.common.collect.ImmutableMap;

@RestController
public class ProjectController extends BaseController<Project> {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;

	/****
	 * 在执行update前，先获取持久化的project对象
	 * 
	 * @param id
	 * @param model
	 * 
	 */
	@ModelAttribute
	public void getAccount(@RequestParam(value = "number", required = false) String number, Model model) {
		if (StringUtils.isNoneBlank(number)) {
			model.addAttribute("project",
					projectService.findByNumber(number) == null ? new Project() : projectService.findByNumber(number));
		}
	}

	/***
	 * save or update project
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/save", method = RequestMethod.POST)
	public JSONObject save(@ModelAttribute("project") Project project) throws Exception {
		logger.debug("saveOrUpdate project");
		projectService.save(project);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

}
