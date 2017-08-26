package com.anosi.asset.controller;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.model.jpa.QAccount;
import com.anosi.asset.service.AccountService;
import com.anosi.asset.service.BaseProcessService;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping("/customerServiceProcess")
public class CustomerServiceProcessController extends BaseProcessController<CustomerServiceProcess> {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceProcessController.class);

	@Autowired
	private CustomerServcieProcessService customerServcieProcessService;
	@Autowired
	private AccountService accountService;

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
		Iterable<Account> accounts = accountService.findAll(QAccount.account.role.code.eq("engineerManager"));
		return ImmutableMap.of("accounts", accounts);
	}

	/***
	 * 发起流程
	 * 
	 * @return
	 */
	@RequestMapping(value = "/startProcess", method = RequestMethod.POST)
	public JSONObject startProcess(@RequestParam(value = "engineeDep") String engineeDep,
			CustomerServiceProcess process, MultipartFile[] multipartFiles) {
		logger.debug("customerServiceProcess -> start process");
		try {
			customerServcieProcessService.startProcess(accountService.findByLoginId(engineeDep),
					process.getStartDetail(), multipartFiles);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONObject(
					ImmutableMap.of("result", "error", "message", Objects.requireNonNull(e.getMessage(), "error")));
		}
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

	@Override
	protected Map<String, Object> getRunTimeTaskObjects(String taskDefinitionKey) {
		logger.debug("taskDefinitionKey:{},process:customerService",taskDefinitionKey);
		Iterable<Account> accounts;
		switch (taskDefinitionKey) {
		case "evaluating":
			accounts = accountService.findAll(QAccount.account.role.depGroup.code.eq("customerServiceGroup"));
			return ImmutableMap.of("accounts", accounts);
		case "distribute":
			accounts = accountService.findAll(QAccount.account.role.code.eq("engineer"));
			return ImmutableMap.of("accounts", accounts);
		case "repair":
			accounts = accountService.findAll(QAccount.account.role.code.eq("engineer"));
			return ImmutableMap.of("accounts", accounts);
		}
		return null;
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
			@RequestParam(value = "engineeDep") String engineeDep, CustomerServiceProcess process) {
		logger.debug("customerServiceProcess -> completeStartDetail");
		customerServcieProcessService.completeStartDetail(accountService.findByLoginId(engineeDep), taskId,
				process.getStartDetail());
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
			@RequestParam(value = "servicer") String servicer, CustomerServiceProcess process) {
		logger.debug("customerServiceProcess -> evaluating");
		customerServcieProcessService.evaluating(accountService.findByLoginId(servicer), taskId,
				process.getEvaluatingDetail());
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
	@RequestMapping(value = "/repair", method = RequestMethod.POST)
	public JSONObject repair(@RequestParam(value = "taskId") String taskId, CustomerServiceProcess process) {
		logger.debug("customerServiceProcess -> repair");
		customerServcieProcessService.repair(taskId, process.getRepairDetail());
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
			@RequestParam(value = "mandatary") String mandatary, @RequestParam(value = "reason") String reason) {
		logger.debug("customerServiceProcess -> entrust");
		customerServcieProcessService.entrust(taskId, accountService.findByLoginId(mandatary), reason);
		return new JSONObject(ImmutableMap.of("result", "success"));
	}

}
