package com.anosi.asset.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.QAccount;
import com.anosi.asset.model.jpa.QRole;
import com.anosi.asset.model.jpa.CustomerServiceProcess.StartDetail.Belong;
import com.anosi.asset.model.jpa.CustomerServiceProcess.StartDetail.ProductType;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.BaseProcessService;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.service.RoleService;
import com.google.common.collect.ImmutableMap;
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

	public CustomerServiceProcessController() {
		super();
		// 在构造方法中对流程定义赋值
		definitionKey = "customerService";
	}

	@Override
	public BaseProcessService<CustomerServiceProcess> getPorcessService() {
		return customerServcieProcessService;
	}

	@Override
	protected Map<String, Object> getStartProcessObjects() {
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
	protected Map<String, Object> getRunTimeTaskObjects(String taskDefinitionKey) {
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
			return ImmutableMap.of("accounts", accounts);
		}
		throw new CustomRunTimeException("taskDefinitionKey is illegal");
	}

	/***
	 * 完成发起流程的表单
	 * 
	 * @param taskId
	 * @param engineeDep
	 * @param startDetail
	 * @return
	 * @deprecated 已经与startProcess合并为一步
	 */
	@RequestMapping(value = "/completeStartDetail", method = RequestMethod.POST)
	public JSONObject completeStartDetail(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) {
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
	 */
	@RequestMapping(value = "/evaluating", method = RequestMethod.POST)
	public JSONObject evaluating(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) {
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
	 */
	@RequestMapping(value = "/distribute", method = RequestMethod.POST)
	public JSONObject distribute(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) {
		logger.debug("customerServiceProcess -> distribute");
		customerServcieProcessService.distribute(taskId, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 工程师上门维修
	 * 
	 * @param taskId
	 * @param repairDetail
	 * @return
	 */
	@RequestMapping(value = "/repair", method = RequestMethod.POST)
	public JSONObject repair(@RequestParam(value = "taskId") String taskId,
			@ModelAttribute("process") CustomerServiceProcess process) {
		logger.debug("customerServiceProcess -> repair");
		customerServcieProcessService.repair(taskId, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	/***
	 * 委托
	 * 
	 * @param taskId
	 * @param mandatary
	 * @return
	 */
	@RequestMapping(value = "/entrust", method = RequestMethod.POST)
	public JSONObject entrust(@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "mandatary") String mandatary, @RequestParam(value = "reason") String reason,
			@ModelAttribute("process") CustomerServiceProcess process) {
		logger.debug("customerServiceProcess -> entrust");
		customerServcieProcessService.entrust(taskId, accountService.findByLoginId(mandatary), reason, process);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

}
