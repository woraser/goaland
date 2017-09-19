package com.anosi.asset.controller;

import java.util.Map;


import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.BaseProcess;
import com.anosi.asset.service.BaseProcessService;

@RestController
public abstract class BaseProcessController<T extends BaseProcess> extends BaseController<T> {

	private static final Logger logger = LoggerFactory.getLogger(BaseProcessController.class);

	@Autowired
	protected TaskService taskService;

	protected String definitionKey;

	public abstract BaseProcessService<T> getPorcessService();

	/***
	 * 进入查看当前account<b>发起的流程</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/startedProcess/view", method = RequestMethod.GET)
	public ModelAndView toViewStartedProcess() {
		logger.debug("view startedProcess");
		return new ModelAndView("process/" + definitionKey + "/startedProcess");
	}

	/***
	 * 获取由当前account<b>发起的流程</b>的数据
	 * 
	 * @param pageable
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/startedProcess/data/{showType}", method = RequestMethod.GET)
	public JSONObject findStartedProcess(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(getPorcessService().findStartedProcess(pageable), rowId, showAttributes, showType);
	}

	/***
	 * 进入<b>发起流程</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/startProcess/form", method = RequestMethod.GET)
	public ModelAndView toViewStartProcessForm(T process) throws Exception {
		logger.debug("view startProcess form");
		return new ModelAndView("process/" + definitionKey + "/startProcessForm", "process", process)
				.addAllObjects(getStartProcessObjects());
	}

	/***
	 * 发起流程所需的数据
	 * 
	 * @return
	 */
	protected abstract Map<String, Object> getStartProcessObjects();

	/***
	 * 进入查看当前account<b>待办的任务</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/runtimeTask/view", method = RequestMethod.GET)
	public ModelAndView toViewRuntimeTasks() {
		logger.debug("view runtime tasks");
		return new ModelAndView("process/" + definitionKey + "/runtimeTask");
	}

	/**
	 * 查询当前account<b>待办的任务</b>的数据
	 * 
	 * @param pageable
	 *            分页
	 * @param showAttributes
	 *            展示的列
	 * @param rowId
	 *            jqgrid每行的id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/runtimeTask/data/{showType}", method = RequestMethod.GET)
	public JSONObject findRuntimeTaskDatas(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find customerServiceProcess runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(getPorcessService().findTasksToDo(pageable), rowId, showAttributes, showType);
	}

	/***
	 * 进入办理页面 会根据task
	 * 
	 * @param process
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/runtimeTask/form", method = RequestMethod.GET)
	public ModelAndView toViewRunTimeTaskForm(T process, @RequestParam String taskId) throws Exception {
		logger.debug("view startProcess form");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return new ModelAndView("process/" + definitionKey + "/runtimeTask-" + task.getTaskDefinitionKey(), "process",
				process).addObject("task", task).addAllObjects(getRunTimeTaskObjects(task.getTaskDefinitionKey()));
	}

	/***
	 * 发起办理所需的数据
	 * 
	 * @param taskDefinitionKey
	 *            task定义的key
	 * @return
	 */
	protected abstract Map<String, Object> getRunTimeTaskObjects(String taskDefinitionKey);

	/***
	 * 进入查看当前account<b>办理过的任务</b>的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/historicTasks/view", method = RequestMethod.GET)
	public ModelAndView toViewHistoricTasks() {
		logger.debug("view historic tasks");
		return new ModelAndView("process/" + definitionKey + "/historicTasks");
	}

	/***
	 * 查询当前account<b>办理过的任务</b>的数据
	 * 
	 * @param pageable
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/historicTasks/data/{showType}", method = RequestMethod.GET)
	public JSONObject findHistoricTasks(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId) throws Exception {
		logger.info("find customerServiceProcess runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return parseToJson(getPorcessService().findHistoricTasks(pageable), rowId, showAttributes, showType);
	}

}
