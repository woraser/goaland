package com.anosi.asset.service.impl;

import javax.transaction.Transactional;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anosi.asset.dao.jpa.CustomerServiceProcessDao;
import com.anosi.asset.model.jpa.Account;
import com.anosi.asset.model.jpa.CustomerServiceProcess;
import com.anosi.asset.service.CustomerServcieProcessService;
import com.anosi.asset.util.SessionUtil;
import com.google.common.collect.ImmutableMap;

@Service("customerServcieProcessService")
@Transactional
public class CustomerServcieProcessServiceImpl extends BaseProcessServiceImpl<CustomerServiceProcess>
		implements CustomerServcieProcessService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServcieProcessServiceImpl.class);

	@Autowired
	private CustomerServiceProcessDao customerServiceProcessDao;

	public CustomerServcieProcessServiceImpl() {
		super();
		definitionKey = "customerService";
	}

	@Override
	public CustomerServiceProcess findByProcessInstanceId(String processInstanceId) {
		logger.debug("findByProcessInstanceId:{}", processInstanceId);
		return customerServiceProcessDao.findByProcessInstanceId(processInstanceId);
	}

	@Override
	public void startProcess() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(getDefinitionKey(), ImmutableMap.of("assignee", SessionUtil.getCurrentUser().getLoginId()));
		CustomerServiceProcess customerServiceProcess = new CustomerServiceProcess();
		customerServiceProcess.setProcessInstanceId(processInstance.getId());
		customerServiceProcessDao.save(customerServiceProcess);
	}

	@Override
	public void completeProcess(Account engineeDep,String taskId) {
		completeTask(taskId, ()->taskService.complete(taskId, ImmutableMap.of("engineeDep", engineeDep.getLoginId())));
	}

	@Override
	public void evaluating(Account servicer, String taskId) {
		completeTask(taskId, ()->taskService.complete(taskId, ImmutableMap.of("servicer", servicer.getLoginId())));
	}

	@Override
	public void distribute(Account engineer, String taskId) {
		completeTask(taskId, ()->taskService.complete(taskId, ImmutableMap.of("engineer", engineer.getLoginId())));
	}

	@Override
	public void repair(String taskId) {
		completeTask(taskId, ()->taskService.complete(taskId));
	}

	@Override
	public void entrust(String taskId, Account mandatary) {
		//TODO acitviti委托
	}
	
}
