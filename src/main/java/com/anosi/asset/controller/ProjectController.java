package com.anosi.asset.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.alibaba.fastjson.JSONArray;
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

	/***
	 * batch save or update project
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/save/batch", method = RequestMethod.POST)
	public JSONObject saveBatch(@ModelAttribute("projectVO") ProjectVO projectVO) throws Exception {
		logger.debug("batch saveOrUpdate project");
		List<Project> projects = projectVO.getProjects().parallelStream()
				.map(project -> projectService.findByNumber(project.getNumber()) == null ? project
						: projectService.findByNumber(project.getNumber()))
				.collect(Collectors.toList());
		projectService.save(projects);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 获取根据number模糊搜索的project
	 * 
	 * @param number
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/project/autocomplete", method = RequestMethod.GET)
	public JSONArray autocomplete(@RequestParam(value = "number") String number,
			@RequestParam(value = "remote", required = false) boolean remote) throws Exception {
		JSONArray jsonArray = new JSONArray();
		List<Project> projects = projectService.findByNumberStartsWith(number);
		for (Project project : projects) {
			JSONObject jsonObject = new JSONObject();
			if (remote) {
				jsonObject.put("id", project.getId());
				jsonObject.put("number", project.getNumber());
				jsonObject.put("name", project.getName());
				jsonObject.put("location", project.getLocation());
			} else {
				jsonObject.put("label", project.getNumber() + "-" + project.getName() + "-" + project.getLocation());
				jsonObject.put("value", project.getId());
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public static class ProjectVO {

		private List<Project> projects;

		public List<Project> getProjects() {
			return projects;
		}

		public void setProjects(List<Project> projects) {
			this.projects = projects;
		}

	}

}
