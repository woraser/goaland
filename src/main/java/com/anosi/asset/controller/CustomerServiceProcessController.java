package com.anosi.asset.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.AgreementStatus.Agreement;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.Device;
import com.anosi.asset.model.jpa.FaultCategory;
import com.anosi.asset.model.jpa.QAccount;
import com.anosi.asset.model.jpa.QRole;
import com.anosi.asset.model.jpa.StartDetail.Belong;
import com.anosi.asset.model.jpa.StartDetail.ProductType;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.BaseProcessService;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.DeviceService;
import com.anosi.asset.service.FaultCategoryService;
import com.anosi.asset.service.RoleService;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RestController
@RequestMapping("/customerServiceProcess")
public class CustomerServiceProcessController extends BaseProcessController<CustomerServiceProcess> {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceProcessController.class);

	@Autowired
	private CustomerServcieProcessService customerServcieProcessService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private FaultCategoryService faultCategoryService;

	public CustomerServiceProcessController() {
		super();
		// 在构造方法中对流程定义赋值
		definitionKey = "customerService";
	}

	@Override
	public BaseProcessService<CustomerServiceProcess> getProcessService() {
		return customerServcieProcessService;
	}

	@Override
	public Map<String, Object> getStartProcessObjects() {
		String loginId = sessionComponent.getCurrentUser().getLoginId();
		Account currentAccount = accountService.findByLoginId(loginId);
		String code = currentAccount.getDepartment().getCode();
		Iterable<Account> accounts = null;
		QAccount qAccount = QAccount.account;
		switch (code) {
		case "engineerDep":
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("engineerManager")));
			break;
		case "salesDep":
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("salesManager")));
			break;
		case "qualityCheckingDep":
			accounts = accountService
					.findAll(qAccount.roleList.contains(roleService.findByCode("qualityCheckingManager")));
			break;
		default:
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("engineerManager")));
			break;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("accounts", accounts);
		map.put("belongs", Belong.values());
		map.put("productTypes", ProductType.values());
		return map;
	}

	@Override
	public JSONObject getStartProcessObjectsRemote() {
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> startProcessObjects = getStartProcessObjects();
		@SuppressWarnings("unchecked")
		Iterable<Account> accounts = (Iterable<Account>) startProcessObjects.get("accounts");
		JSONArray jsonArray = new JSONArray();
		for (Account account : accounts) {
			JSONObject nestJson = new JSONObject();
			nestJson.put("id", account.getId());
			nestJson.put("name", account.getName());
			nestJson.put("loginId", account.getLoginId());
			jsonArray.add(nestJson);
		}
		jsonObject.put("accounts", jsonArray);
		return jsonObject;
	}

	/***
	 * 发起流程
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/startProcess", method = RequestMethod.POST)
	public JSONObject startProcess(@ModelAttribute("process") CustomerServiceProcess process,
			@RequestParam(value = "fileUpLoad", required = false) MultipartFile[] multipartFiles) throws Exception {
		logger.debug("customerServiceProcess -> start process");
		customerServcieProcessService.startProcess(process, multipartFiles);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	@Override
	public Map<String, Object> getRunTimeTaskObjects(String taskDefinitionKey, String processInstanceId) {
		logger.debug("taskDefinitionKey:{},process:customerService", taskDefinitionKey);
		Iterable<Account> accounts;
		QAccount qAccount = QAccount.account;
		QRole qRole = QRole.role;

		switch (taskDefinitionKey) {
		case "examine":
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("engineerManager")));
			return ImmutableMap.of("accounts", accounts);

		case "evaluating":
			JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
			accounts = queryFactory.select(qAccount).from(qAccount, qRole)
					.where(qRole.depGroup.code.eq("customerServiceGroup"), qAccount.roleList.contains(qRole)).fetch();
			return ImmutableMap.of("accounts", accounts);

		case "distribute":
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("engineer")));
			return ImmutableMap.of("accounts", accounts);

		case "repair":
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("engineer")));
			return ImmutableMap.of("accounts", accounts, "devices", deviceService.findIdAndSN(), "faultCategorys",
					faultCategoryService.findAll());

		case "entrust":
			accounts = accountService.findAll(qAccount.roleList.contains(roleService.findByCode("engineer")));
			CustomerServiceProcess customerServiceProcess = customerServcieProcessService
					.findByProcessInstanceId(processInstanceId);
			return ImmutableMap.of("accounts", accounts, "devices", deviceService.findIdAndSN(), "faultCategorys",
					faultCategoryService.findAll(), "faultCategoryIds",
					customerServiceProcess.getEntrustDetail().getFaultCategoryList().stream().map(FaultCategory::getId)
							.collect(Collectors.toList()),
					"deviceIds", customerServiceProcess.getEntrustDetail().getDeviceList().stream().map(Device::getId)
							.collect(Collectors.toList()));
		}
		throw new CustomRunTimeException("taskDefinitionKey is illegal");
	}

	@Override
	public JSONObject getRunTimeTaskObjectsRemote(String taskDefinitionKey, String processInstanceId) {
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> startProcessObjects = getRunTimeTaskObjects(taskDefinitionKey, processInstanceId);
		@SuppressWarnings("unchecked")
		Iterable<Account> accounts = (Iterable<Account>) startProcessObjects.get("accounts");
		JSONArray jsonArray = new JSONArray();
		for (Account account : accounts) {
			JSONObject nestJson = new JSONObject();
			nestJson.put("id", account.getId());
			nestJson.put("name", account.getName());
			nestJson.put("loginId", account.getLoginId());
			jsonArray.add(nestJson);
		}
		jsonObject.put("accounts", jsonArray);
		return jsonObject;
	}

	/***
	 * 完成发起流程的表单
	 * 
	 * @param taskId
	 * @param engineeDep
	 * @param startDetail
	 * @return
	 * @throws Exception
	 * @deprecated 已经与startProcess合并为一步
	 */
	@RequestMapping(value = "/completeStartDetail", method = RequestMethod.POST)
	public JSONObject completeStartDetail(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) throws Exception {
		logger.debug("customerServiceProcess -> completeStartDetail");
		customerServcieProcessService.completeStartDetail(taskId, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 其他部门领导审批
	 * 
	 * @param taskId
	 * @param evaluatingDetail
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/examine", method = RequestMethod.POST)
	public JSONObject examine(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) throws Exception {
		logger.debug("customerServiceProcess -> examine");
		logger.debug("processId,{}", process.getId());
		logger.debug("suggestion,{}", process.getExamineDetail().getSuggestion());
		customerServcieProcessService.examine(taskId, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 工程部问题评估
	 * 
	 * @param taskId
	 * @param servicer
	 * @param evaluatingDetail
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/evaluating", method = RequestMethod.POST)
	public JSONObject evaluating(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) throws Exception {
		logger.debug("customerServiceProcess -> evaluating");
		customerServcieProcessService.evaluating(taskId, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 售后服务组派单
	 * 
	 * @param taskId
	 * @param engineer
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/distribute", method = RequestMethod.POST)
	public JSONObject distribute(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) throws Exception {
		logger.debug("customerServiceProcess -> distribute");
		customerServcieProcessService.distribute(taskId, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 工程师上门维修
	 * 
	 * @param taskId
	 * @param process
	 * @param multipartFiles
	 * @param fellows
	 * @param devices
	 * @param faultCategorys
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/repair", method = RequestMethod.POST)
	public JSONObject repair(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process, @RequestParam(name = "device") Long[] devices,
			@RequestParam(name = "fellow", required = false) Long[] fellows,
			@RequestParam(name = "faultCategory") Long[] faultCategorys,
			@RequestParam(value = "fileUpLoad", required = false) MultipartFile[] multipartFiles) throws Exception {
		logger.debug("customerServiceProcess -> repair");
		customerServcieProcessService.repair(taskId, process, multipartFiles, devices, fellows, faultCategorys);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 委托
	 * 
	 * @param taskId
	 * @param process
	 * @param multipartFiles
	 * @param fellows
	 * @param devices
	 * @param faultCategorys
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/entrust", method = RequestMethod.POST)
	public JSONObject entrust(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process, @RequestParam(name = "device") Long[] devices,
			@RequestParam(name = "fellow", required = false) Long[] fellows,
			@RequestParam(name = "faultCategory") Long[] faultCategorys,
			@RequestParam(value = "fileUpLoad", required = false) MultipartFile[] multipartFiles) throws Exception {
		logger.debug("customerServiceProcess -> entrust");
		customerServcieProcessService.entrust(taskId, process, multipartFiles, devices, fellows, faultCategorys);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 进入填写合同状态的页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fillInAgreement/view", method = RequestMethod.GET)
	public ModelAndView toViewFillInAgreement() {
		return new ModelAndView("process/customerService/fillInAgreement");
	}

	/***
	 * 填写合同状态
	 * 
	 * @return
	 */
	@RequestMapping(value = "/fillInAgreement", method = RequestMethod.POST)
	public JSONObject toFillInAgreement(@ModelAttribute("process") CustomerServiceProcess process) {
		process.getAgreementStatus().setAgreement(process.getAgreementStatus().checkAgreement());
		customerServcieProcessService.save(process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 获取运行中任务的节点分布
	 */
	@RequestMapping(value = "/task/distribute", method = RequestMethod.GET)
	public JSONArray getTaskDistribute() throws Exception {
		return customerServcieProcessService.getTaskDistribute();
	}

	/***
	 * 获取每日工单发起数量
	 * 
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/started/daily", method = RequestMethod.GET)
	public JSONArray getTaskDistribute(@QuerydslPredicate(root = CustomerServiceProcess.class) Predicate predicate,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 6) Pageable pageable)
			throws Exception {
		return customerServcieProcessService.getDailyStartedProcess(predicate, pageable);
	}

	/***
	 * 获取流程数据
	 * 
	 * @param showType
	 * @param pageable
	 * @param predicate
	 * @param showAttributes
	 * @param rowId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/management/data/{showType}", method = RequestMethod.GET)
	public JSONObject findProcessManageData(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = CustomerServiceProcess.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@RequestParam(value = "timeType", required = false, defaultValue = "start") String timeType,
			@RequestParam(value = "beginTime", required = false) Date beginTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agreement", required = false) Agreement agreement) throws Exception {
		logger.info("find process");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		predicate = customerServcieProcessService.convertPredicate(predicate, timeType, beginTime, endTime, agreement);
		return parseToJson(customerServcieProcessService.findAll(predicate, pageable), rowId, showAttributes, showType);
	}

	/***
	 * 获取所有发起的流程
	 * 
	 * @param showType
	 * @param pageable
	 * @param showAttributes
	 * @param rowId
	 * @param searchContent
	 * @param timeType
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/allProcesses/data/override/{showType}", method = RequestMethod.GET)
	public JSONObject findAllProcesses(@PathVariable ShowType showType,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable,
			@QuerydslPredicate(root = CustomerServiceProcess.class) Predicate predicate,
			@RequestParam(value = "showAttributes", required = false) String showAttributes,
			@RequestParam(value = "rowId", required = false, defaultValue = "id") String rowId,
			@RequestParam(value = "searchContent", required = false) String searchContent,
			@RequestParam(value = "timeType", required = false, defaultValue = "start") String timeType,
			@RequestParam(value = "beginTime", required = false) Date beginTime,
			@RequestParam(value = "endTime", required = false) Date endTime,
			@RequestParam(value = "agreement", required = false) Agreement agreement) throws Exception {
		logger.info("find allProcesses");
		logger.debug("page:{},size{},sort{}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
		logger.debug("rowId:{},showAttributes:{}", rowId, showAttributes);

		Page<CustomerServiceProcess> page;
		predicate = customerServcieProcessService.convertPredicate(predicate, timeType, beginTime, endTime, agreement);
		if (StringUtils.isNoneBlank(searchContent)) {
			page = customerServcieProcessService.findbyContent(searchContent, pageable);
		} else {
			page = customerServcieProcessService.findAll(predicate, pageable);
		}
		page.getContent().forEach(cs -> customerServcieProcessService.setHistoricValueForProcess(cs));
		return parseToJson(page, rowId, showAttributes, showType);
	}

}
