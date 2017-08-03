package com.anosi.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.CustomerServiceProcess.EvaluatingDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.RepairDetail;
import com.anosi.asset.model.jpa.CustomerServiceProcess.StartDetail;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.util.JqgridUtil;
import com.google.common.collect.ImmutableMap;

@RestController
public class CustomerServiceProcessController extends BaseController<CustomerServiceProcess> {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceProcessController.class);

	@Autowired
	private CustomerServcieProcessService customerServcieProcessService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private JqgridUtil<CustomerServiceProcess> jqgridUtil;

	/***
	 * 获取由当前account发起的流程
	 * 
	 * @param pageable
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerServiceProcess/startedProcess/data", method = RequestMethod.GET)
	public JSONObject findStartedProcess(
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes, @RequestParam(value = "rowId") String rowId)
			throws Exception {
		logger.info("find customerServiceProcess runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return jqgridUtil.parsePageToJqgridJson(customerServcieProcessService.findStartedProcess(pageable), rowId,
				showAttributes.split(","));
	}

	/**
	 * 查询当前account待办的任务
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
	@RequestMapping(value = "/customerServiceProcess/runtimeTask/data", method = RequestMethod.GET)
	public JSONObject findRuntimeTaskDatas(
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes, @RequestParam(value = "rowId") String rowId)
			throws Exception {
		logger.info("find customerServiceProcess runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return jqgridUtil.parsePageToJqgridJson(customerServcieProcessService.findTasksToDo(pageable), rowId,
				showAttributes.split(","));
	}

	/***
	 * 查询当前account办理过的任务
	 * 
	 * @param pageable
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customerServiceProcess/historicTasks/data", method = RequestMethod.GET)
	public JSONObject findHistoricTasks(
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@RequestParam(value = "showAttributes") String showAttributes, @RequestParam(value = "rowId") String rowId)
			throws Exception {
		logger.info("find customerServiceProcess runtimeTask");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		return jqgridUtil.parsePageToJqgridJson(customerServcieProcessService.findHistoricTasks(pageable), rowId,
				showAttributes.split(","));
	}

	/***
	 * 发起流程
	 * 
	 * @return
	 */
	@RequestMapping(value = "/customerServiceProcess/startProcess", method = RequestMethod.POST)
	public JSONObject startProcess() {
		logger.debug("customerServiceProcess -> start process");
		customerServcieProcessService.startProcess();
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 完成发起流程的表单
	 * 
	 * @param taskId
	 * @param engineeDep
	 * @param startDetail
	 * @return
	 */
	@RequestMapping(value = "/customerServiceProcess/completeStartDetail", method = RequestMethod.POST)
	public JSONObject completeStartDetail(@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "engineeDep") String engineeDep, StartDetail startDetail) {
		logger.debug("customerServiceProcess -> completeStartDetail");
		customerServcieProcessService.completeStartDetail(accountService.findByLoginId(engineeDep), taskId,
				startDetail);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 工程部问题评估
	 * 
	 * @param taskId
	 * @param servicer
	 * @param evaluatingDetail
	 * @return
	 */
	@RequestMapping(value = "/customerServiceProcess/evaluating", method = RequestMethod.POST)
	public JSONObject evaluating(@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "servicer") String servicer, EvaluatingDetail evaluatingDetail) {
		logger.debug("customerServiceProcess -> evaluating");
		customerServcieProcessService.evaluating(accountService.findByLoginId(servicer), taskId, evaluatingDetail);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 售后服务组派单
	 * 
	 * @param taskId
	 * @param engineer
	 * @return
	 */
	@RequestMapping(value = "/customerServiceProcess/distribute", method = RequestMethod.POST)
	public JSONObject distribute(@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "engineer") String engineer) {
		logger.debug("customerServiceProcess -> distribute");
		customerServcieProcessService.distribute(accountService.findByLoginId(engineer), taskId);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 工程师上门维修
	 * 
	 * @param taskId
	 * @param repairDetail
	 * @return
	 */
	@RequestMapping(value = "/customerServiceProcess/repair", method = RequestMethod.POST)
	public JSONObject repair(@RequestParam(value = "taskId") String taskId, RepairDetail repairDetail) {
		logger.debug("customerServiceProcess -> repair");
		customerServcieProcessService.repair(taskId, repairDetail);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 委托
	 * 
	 * @param taskId
	 * @param mandatary
	 * @return
	 */
	@RequestMapping(value = "/customerServiceProcess/entrust", method = RequestMethod.POST)
	public JSONObject entrust(@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "mandatary") String mandatary, @RequestParam(value = "reason") String reason) {
		logger.debug("customerServiceProcess -> entrust");
		customerServcieProcessService.entrust(taskId, accountService.findByLoginId(mandatary), reason);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

}
